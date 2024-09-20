package com.example.xoserver;

import com.example.shared.Message;
import com.example.shared.Text;
import com.example.xoserver.connection.SqlConnection;
import com.example.xoserver.dao.DataAccessObject;
import com.example.xoserver.dao.TokenDAO;
import com.example.xoserver.data.Match;
import com.example.xoserver.data.Cache;
import com.example.xoserver.data.Room;
import com.example.xoserver.messageprocessor.MessageProcessor;
import com.example.xoserver.messageprocessor.factory.MessageProcessorFactory;
import com.example.xoserver.model.Profile;
import com.example.xoserver.model.Token;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.UUID;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    
    private final String token = UUID.randomUUID().toString();
    
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        Cache.clientHandlerMap.put(token, this);
    }
    
    @Override
    public void run() {
        try {
            
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            int count = 0;
            
            while (!clientSocket.isClosed()) {
                
                // Đọc và xử lý đối tượng Message từ client
                Message receivedMessage = (Message) objectInputStream.readObject();
                
                // Xử lý đối tượng Message
                String actionCode = receivedMessage.getActionCode();
                String content = receivedMessage.getContent();
                String status = receivedMessage.getStatus();
                
                // Status phải là OK
                if (!status.equals("OK")) {
                    ++count;
                    if (count == 5) {
                        clientSocket.close();
                        return;
                    }
                    continue;
                }
                count = 0;
                
                // Tạo đối tượng MessageProcessor theo actionCode và giải mã
                MessageProcessor messageProcessor = MessageProcessorFactory.createDecryption(actionCode);
                messageProcessor.setToken(token);
                Message responseMessage = messageProcessor.processMessage(content, status);
                
                // Nếu nội dung phản hồi trống thì không cần phản hồi lại client
                if (responseMessage == null) {
                    continue;
                }
                
                //
                System.out.println(clientSocket + " : ");
                System.out.println(receivedMessage);
                System.out.println();
                System.out.println(Text.blue("[__Server__]") + " to " + clientSocket + " : ");
                System.out.println(responseMessage);
                System.out.println();
                
                // Gửi phản hồi về cho client
                objectOutputStream.writeObject(responseMessage);
                
            }
            
        } catch (IOException | ClassNotFoundException e) {
            
            System.out.println(clientSocket + " : " + e.getMessage());
            
            // Xử lí sự kiện đối thủ thoát : Find Match
            if (e instanceof SocketException) {
                Object retrievedObject = Match.opponentToken.retrieveData(token);
                if (retrievedObject != null) {
                    String opponentToken = (String) retrievedObject;
                    ClientHandler clientHandler = Cache.clientHandlerMap.get(opponentToken);
                    
                    // Find match
                    if (clientHandler != null) {
                        clientHandler.send(new Message("find_match", "Đối thủ đã thoát", "WRONG"));
                    }
                }
            }
            
            // Room
            if (Match.token_idRoom.containsKey(this.token)) {
                String idRoom = Match.token_idRoom.get(this.token);
                Room room = Match.room.get(idRoom);
                
                String opponentTokenInRoom = room.getOpponentToken(this.token);
                room.remove(this.token);
                Match.token_idRoom.remove(this.token);
                
                ClientHandler opponentClientHandlerInRoom = Cache.clientHandlerMap.get(opponentTokenInRoom);
                if (opponentClientHandlerInRoom != null) {
                    Object object = Cache.token_profile.retrieveData(this.token);
                    if (object != null) {
                        String username = ((Profile) object).getUsername();
                        
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("username", username);
                        String contentMessage = jsonObject.toString();
                        
                        opponentClientHandlerInRoom.send(new Message("exit_room", contentMessage, "OK"));
                    }
                }
            }
            
            // remove token
            Object retrievedData = Cache.token_profile.retrieveData(token);
            if (retrievedData != null) {
                String username = ((Profile) retrievedData).getUsername();
                DataAccessObject<String, Token> accessToken = new TokenDAO(SqlConnection.connection);
                try {
                    accessToken.delete(username);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                Cache.clientHandlerMap.remove(token);
            }
        }
    }
    
    public void opponentExit() {
        try {
            Message responseMessage = new Message("find_match", "Đối thủ đã thoát", "WRONG");
            
            // Gửi phản hồi về cho client
            objectOutputStream.writeObject(responseMessage);
        } catch (IOException e) {
            System.out.println(clientSocket + " : exit : " + e.getMessage());
        }
    }
    
    public void send(Message message) {
        try {
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println(clientSocket + " : " + e.getMessage());
        }
    }
}