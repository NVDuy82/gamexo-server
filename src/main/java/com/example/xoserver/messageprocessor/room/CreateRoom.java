package com.example.xoserver.messageprocessor.room;

import com.example.shared.Message;
import com.example.xoserver.data.Game;
import com.example.xoserver.data.Match;
import com.example.xoserver.data.Room;
import com.example.xoserver.messageprocessor.MessageProcessor;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.UUID;

/**
 * Lớp CreateRoom kế thừa từ MessageProcessor, dùng để xử lý thông điệp tạo phòng chơi.
 */
public class CreateRoom extends MessageProcessor {
    private String wrongMessage; // Thông điệp lỗi nếu có
    private String idRoom; // ID của phòng được tạo

    /**
     * Thực hiện các tác vụ cần thiết để tạo phòng chơi.
     * Phân tích nội dung thông điệp và kiểm tra tính hợp lệ.
     *
     * @param decryptedContent Nội dung đã giải mã
     * @param status Trạng thái hiện tại của người dùng
     */
    @Override
    protected void performTask(String decryptedContent, String status) {
        // Phân tích thông điệp JSON
        JsonObject jsonObject = JsonParser.parseString(decryptedContent).getAsJsonObject();
        String password = jsonObject.get("password").getAsString();

        // Kiểm tra xem người dùng đã ở trong phòng nào chưa
        if (Match.token_idRoom.containsKey(this.token)) {
            wrongMessage = "You have been in a room before.";
            return;
        }

        // Tạo ID phòng ngẫu nhiên
        UUID uuid = UUID.randomUUID();
        this.idRoom = uuid.toString().substring(0, 8);

        // Lưu ID phòng và tạo đối tượng Room
        Match.token_idRoom.put(this.token, this.idRoom);
        Match.room.put(this.idRoom, new Room(password, this.token));
    }

    /**
     * Tạo và trả về thông điệp phản hồi sau khi thực hiện tạo phòng.
     *
     * @param decryptedContent Nội dung đã giải mã
     * @return Thông điệp phản hồi với kết quả tạo phòng
     */
    @Override
    protected Message generateResponse(String decryptedContent) {
        if (wrongMessage != null) {
            return new Message("create_room", wrongMessage, "WRONG");
        }

        // Tạo chuỗi JSON chứa ID phòng
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idRoom", this.idRoom);
        String contentMessage = jsonObject.toString();

        return new Message("create_room", contentMessage, "OK");
    }
}
