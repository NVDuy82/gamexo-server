package com.example.xoserver.messageprocessor.room;

import com.example.shared.Message;
import com.example.xoserver.ClientHandler;
import com.example.xoserver.data.Cache;
import com.example.xoserver.data.Match;
import com.example.xoserver.data.Room;
import com.example.xoserver.messageprocessor.MessageProcessor;
import com.example.xoserver.model.Profile;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Lớp JoinRoom kế thừa từ MessageProcessor, dùng để xử lý thông điệp gia nhập phòng chơi.
 */
public class JoinRoom extends MessageProcessor {
    private String idRoom; // ID của phòng
    private Room room; // Thông tin phòng
    private String wrongMessage; // Thông điệp lỗi nếu có

    /**
     * Thực hiện các tác vụ cần thiết để gia nhập phòng chơi.
     * Phân tích nội dung thông điệp và kiểm tra tính hợp lệ.
     *
     * @param decryptedContent Nội dung đã giải mã
     * @param status Trạng thái hiện tại của người dùng
     */
    @Override
    protected void performTask(String decryptedContent, String status) {
        // Phân tích thông điệp JSON
        JsonObject jsonObject = JsonParser.parseString(decryptedContent).getAsJsonObject();
        this.idRoom = jsonObject.get("idRoom").getAsString();
        String password = jsonObject.get("password").getAsString();

        // Kiểm tra hợp lệ
        if (Match.token_idRoom.containsKey(this.token)) {
            wrongMessage = "You have been in a room before.";
            return;
        }
        this.room = Match.room.get(idRoom);
        if (room == null) {
            wrongMessage = "Room ID does not exist.";
            return;
        }
        if (!room.idCorrectPassword(password)) {
            wrongMessage = "Incorrect room password.";
            return;
        }

        // Người chơi vào phòng
        room.setTokenB(this.token);
        Match.token_idRoom.put(this.token, idRoom);
    }

    /**
     * Tạo và trả về thông điệp phản hồi sau khi thực hiện gia nhập phòng.
     *
     * @param decryptedContent Nội dung đã giải mã
     * @return Thông điệp phản hồi với kết quả gia nhập phòng
     */
    @Override
    protected Message generateResponse(String decryptedContent) {
        if (wrongMessage != null) {
            return new Message("join_room", wrongMessage, "WRONG");
        }

        String contentMessage = createResponseContent(this.room.getProfileA(), this.room.getProfileB());
        ClientHandler clientHandler = Cache.clientHandlerMap.get(this.room.getTokenA());

        // Gửi thông điệp phản hồi đến người chơi đầu tiên
        Message responseMessage = new Message("join_room", contentMessage, "OK");
        clientHandler.send(responseMessage);

        return responseMessage;
    }

    /**
     * Tạo chuỗi JSON chứa thông tin của cả hai người chơi trong phòng.
     *
     * @param profileA Thông tin của người chơi A
     * @param profileB Thông tin của người chơi B
     * @return Chuỗi JSON chứa thông tin của cả hai người chơi
     */
    private String createResponseContent(Profile profileA, Profile profileB) {
        // Tạo chuỗi JSON
        JsonObject json = new JsonObject();

        json.addProperty("idRoom", this.idRoom);

        json.addProperty("usernameA", profileA.getUsername());
        json.addProperty("nameA", profileA.getName());
        json.addProperty("scoreA", profileA.getScore());
        json.addProperty("matchesA", profileA.getMatches());
        json.addProperty("winA", profileA.getWin());
        json.addProperty("loseA", profileA.getLose());
        json.addProperty("drawA", profileA.getDraw());

        json.addProperty("usernameB", profileB.getUsername());
        json.addProperty("nameB", profileB.getName());
        json.addProperty("scoreB", profileB.getScore());
        json.addProperty("matchesB", profileB.getMatches());
        json.addProperty("winB", profileB.getWin());
        json.addProperty("loseB", profileB.getLose());
        json.addProperty("drawB", profileB.getDraw());

        return json.toString();
    }
}
