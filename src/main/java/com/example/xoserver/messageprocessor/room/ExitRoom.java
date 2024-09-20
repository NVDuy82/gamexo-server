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
 * Lớp ExitRoom kế thừa từ MessageProcessor, dùng để xử lý thông điệp rời khỏi phòng chơi.
 */
public class ExitRoom extends MessageProcessor {
    private String wrongMessage; // Thông điệp lỗi nếu có
    private String opponentToken; // Token của người chơi đối thủ

    /**
     * Thực hiện các tác vụ cần thiết để rời khỏi phòng chơi.
     * Phân tích nội dung thông điệp và kiểm tra tính hợp lệ.
     *
     * @param decryptedContent Nội dung đã giải mã
     * @param status Trạng thái hiện tại của người dùng
     */
    @Override
    protected void performTask(String decryptedContent, String status) {
        // Phân tích thông điệp JSON
        JsonObject jsonObject = JsonParser.parseString(decryptedContent).getAsJsonObject();
        String idRoom = jsonObject.get("idRoom").getAsString();

        // Kiểm tra xem người dùng có trong phòng không
        if (!Match.token_idRoom.containsKey(this.token) || !Match.token_idRoom.get(this.token).equals(idRoom)) {
            wrongMessage = "You haven't been in room before.";
            return;
        }

        Room room = Match.room.get(idRoom);
        opponentToken = room.getOpponentToken(this.token);

        // Rời phòng
        room.remove(this.token);
    }

    /**
     * Tạo và trả về thông điệp phản hồi sau khi thực hiện rời phòng.
     *
     * @param decryptedContent Nội dung đã giải mã
     * @return Thông điệp phản hồi với kết quả rời phòng
     */
    @Override
    protected Message generateResponse(String decryptedContent) {
        if (wrongMessage != null) {
            return new Message("exit_room", wrongMessage, "WRONG");
        }

        // Lấy thông tin người dùng từ cache
        Object object = Cache.token_profile.retrieveData(this.token);
        if (object == null) return null;
        String username = ((Profile) object).getUsername();

        // Tạo chuỗi JSON chứa tên người dùng
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        String contentMessage = jsonObject.toString();

        Message responseMessage = new Message("exit_room", contentMessage, "OK");

        // Gửi thông điệp đến người chơi đối thủ nếu họ đang trực tuyến
        if (Cache.clientHandlerMap.containsKey(opponentToken)) {
            ClientHandler clientHandler = Cache.clientHandlerMap.get(opponentToken);
            if (clientHandler != null) {
                clientHandler.send(responseMessage);
            }
        }

        return responseMessage;
    }
}
