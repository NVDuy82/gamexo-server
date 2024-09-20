package com.example.xoserver.messageprocessor.room;

import com.example.shared.Message;
import com.example.xoserver.ClientHandler;
import com.example.xoserver.data.*;
import com.example.xoserver.messageprocessor.MessageProcessor;
import com.example.xoserver.model.Profile;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.UUID;

/**
 * Lớp StartGame kế thừa từ MessageProcessor, dùng để xử lý thông điệp bắt đầu trận đấu trong phòng chơi.
 */
public class StartGame extends MessageProcessor {
    private Game game; // Trận đấu hiện tại
    private String idMatch; // ID của trận đấu
    private String opponentToken; // Token của đối thủ
    private String wrongMessage; // Thông điệp lỗi nếu có
    private Profile myProfile; // Thông tin người chơi hiện tại
    private Profile opponentProfile; // Thông tin đối thủ
    private ClientHandler opponentClientHandler; // ClientHandler của đối thủ

    /**
     * Thực hiện các tác vụ cần thiết để bắt đầu trận đấu.
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

        // Kiểm tra hợp lệ
        if (!Match.token_idRoom.containsKey(this.token) || !Match.token_idRoom.get(this.token).equals(idRoom)) {
            wrongMessage = "You haven't been in room before.";
            return;
        }
        Room room = Match.room.get(idRoom);
        opponentToken = room.getOpponentToken(this.token);
        if (!room.isFull()) {
            wrongMessage = "Room does not have enough players.";
            return;
        }
        try {
            this.myProfile = getProfileFromDB(this.getProfile(this.token).getUsername());
            this.opponentProfile = getProfileFromDB(this.getProfile(this.opponentToken).getUsername());
        } catch (Exception e) {
            wrongMessage = "Something went wrong.";
            return;
        }

        // Kiểm tra đối thủ còn online không
        this.opponentClientHandler = Cache.clientHandlerMap.get(opponentToken);
        if (opponentClientHandler == null) {
            room.remove(opponentToken);
            ClientHandler clientHandler = Cache.clientHandlerMap.get(this.token);
            if (clientHandler != null) {
                JsonObject json = new JsonObject();
                json.addProperty("username", opponentProfile.getUsername());
                String contentMessage = json.toString();

                Message responseMessage = new Message("exit_room", contentMessage, "OK");
                clientHandler.send(responseMessage);
            }
        }

        // Tạo idMatch
        UUID uuid = UUID.randomUUID();
        this.idMatch = uuid.toString();
        this.game = new Game(this.token, this.opponentToken, myProfile.getScore(), opponentProfile.getScore());
        Match.game.storeData(idMatch, this.game);
    }

    /**
     * Tạo và trả về thông điệp phản hồi sau khi thực hiện bắt đầu trận đấu.
     *
     * @param decryptedContent Nội dung đã giải mã
     * @return Thông điệp phản hồi với kết quả bắt đầu trận đấu
     */
    @Override
    protected Message generateResponse(String decryptedContent) {
        if (wrongMessage != null) {
            return new Message("start_game", wrongMessage, "WRONG");
        }

        String contentMessage = getContentCreateMatch(this.game, this.idMatch, opponentProfile, myProfile.getUsername());
        String opponentContentMessage = getContentCreateMatch(this.game, this.idMatch, myProfile, myProfile.getUsername());
        opponentClientHandler.send(new Message("find_match", opponentContentMessage, "OK"));

        // Xử lý giới hạn thời gian
        this.game.countdownHandler(myProfile.getUsername(), new Move(opponentToken, 0, -1, -1, -1));

        return new Message("find_match", contentMessage, "OK");
    }
}
