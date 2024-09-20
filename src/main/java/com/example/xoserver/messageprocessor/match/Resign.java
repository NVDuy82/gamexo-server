package com.example.xoserver.messageprocessor.match;

import com.example.shared.Message;
import com.example.xoserver.ClientHandler;
import com.example.xoserver.data.Cache;
import com.example.xoserver.data.Game;
import com.example.xoserver.data.Match;
import com.example.xoserver.messageprocessor.MessageProcessor;
import com.example.xoserver.model.Profile;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Lớp Resign chịu trách nhiệm xử lý yêu cầu đầu hàng trong trận đấu.
 * Khi một người chơi quyết định đầu hàng, lớp này sẽ xác định người chiến thắng và gửi thông báo đến người chơi còn lại.
 */
public class Resign extends MessageProcessor {

    /** Thể hiện trận đấu cho trận đấu hiện tại. */
    private Game game;

    /** Tên người chiến thắng. */
    private String winner;

    /**
     * Xử lý yêu cầu đầu hàng từ người chơi.
     * Phương thức này phân tích thông điệp đến, xác định trận đấu và xử lý đầu hàng,
     * đồng thời gửi kết quả cho người chơi còn lại.
     *
     * @param decryptedContent Nội dung đã được giải mã của thông điệp đến.
     * @param status Trạng thái của thông điệp (được sử dụng để ghi log hoặc theo dõi).
     */
    @Override
    protected void performTask(String decryptedContent, String status) {
        // Phân tích thông điệp
        JsonObject jsonObject = JsonParser.parseString(decryptedContent).getAsJsonObject();
        String idMatch = jsonObject.get("idMatch").getAsString();
        String token = jsonObject.get("token").getAsString();

        // Lấy dữ liệu game từ idMatch
        Object object = Match.game.retrieveData(idMatch);
        if (object == null) return;
        this.game = (Game) object;

        // Kiểm tra hợp lệ
        if (game.isGameOver()) return;
        if (game.getOpponent(token) == null) return;

        // Xử lí đầu hàng
        String winnerToken = game.getOpponent(token);
        object = Cache.token_profile.retrieveData(winnerToken);
        if (object != null) {
            game.endGame(winnerToken);
            this.winner = ((Profile) object).getUsername();

            // Tạo chuỗi JSON
            JsonObject json = new JsonObject();
            json.addProperty("winner", this.winner);
            json.addProperty("score", game.getScore(game.getOpponent(token)));
            String contentMessage = json.toString();

            // Gửi kết quả cho đối thủ
            ClientHandler opponentClientHandler = Cache.clientHandlerMap.get(game.getOpponent(token));
            Message endGameMessage = new Message("end_game", contentMessage, "OK");
            if (opponentClientHandler != null) {
                opponentClientHandler.send(endGameMessage);
            }
        }
    }

    /**
     * Tạo phản hồi cho người chơi sau khi đầu hàng.
     * Phương thức này tạo ra một thông điệp chứa thông tin về người chiến thắng
     * và điểm số, để gửi về cho người chơi đã đầu hàng.
     *
     * @param decryptedContent Nội dung đã được giải mã của thông điệp đến.
     * @return Một đối tượng Message chứa thông tin về người chiến thắng và điểm số.
     */
    @Override
    protected Message generateResponse(String decryptedContent) {
        // Tạo chuỗi JSON
        JsonObject json = new JsonObject();
        json.addProperty("winner", this.winner);
        json.addProperty("score", game.getScore(game.getOpponent(token)));
        String contentMessage = json.toString();

        return new Message("end_game", contentMessage, "OK");
    }
}
