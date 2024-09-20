package com.example.xoserver;

import com.example.xoserver.connection.SqlConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Statement;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is running and waiting for connections...");
            
            try {
                String query = "DELETE FROM xo_schema.token";
                Statement statement = SqlConnection.connection.createStatement();
                statement.executeUpdate(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println();
                // Handle client connection in a new thread or process
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}