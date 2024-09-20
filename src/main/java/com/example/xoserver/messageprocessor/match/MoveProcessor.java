package com.example.xoserver.messageprocessor.match;

import com.example.shared.Message;
import com.example.xoserver.ClientHandler;
import com.example.xoserver.data.Cache;
import com.example.xoserver.data.Game;
import com.example.xoserver.data.Match;
import com.example.xoserver.data.Move;
import com.example.xoserver.messageprocessor.MessageProcessor;
import com.example.xoserver.model.Profile;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Lớp MoveProcessor chịu trách nhiệm xử lý các yêu cầu di chuyển trong trận đấu.
 * Nó thực hiện việc kiểm tra và gửi kết quả trận đấu đến các người chơi khi có một động thái mới.
 */
public class MoveProcessor extends MessageProcessor {

    /** Thể hiện trận đấu cho trận đấu hiện tại. */
    private Game game;

    /** Định danh duy nhất cho trận đấu. */
    private String idMatch;

    /** Đối tượng Move chứa thông tin di chuyển. */
    private Move move;

    /** Tên người chơi thực hiện di chuyển. */
    private String username = null;

    /** Client handler cho đối thủ. */
    private ClientHandler opponentClientHandler;

    /**
     * Xử lý yêu cầu di chuyển từ người chơi.
     * Phương thức này phân tích thông điệp đến, xác định trận đấu,
     * xử lý di chuyển và kiểm tra xem có người chiến thắng hay không.
     *
     * @param decryptedContent Nội dung đã được giải mã của thông điệp đến.
     * @param status Trạng thái của thông điệp (được sử dụng để ghi log hoặc theo dõi).
     */
    @Override
    protected void performTask(String decryptedContent, String status) {
        // Phân tích thông điệp
        JsonObject jsonObject = JsonParser.parseString(decryptedContent).getAsJsonObject();
        this.idMatch = jsonObject.get("idMatch").getAsString();
        this.move = new Gson().fromJson(jsonObject, Move.class);

        // Lấy dữ liệu game từ idMatch
        Object object = Match.game.retrieveData(idMatch);
        if (object == null) return;
        game = (Game) object;

        // Xử lí yêu cầu move
        if (game.move(this.move)) {
            // Lấy thông tin username
            object = Cache.token_profile.retrieveData(move.getToken());
            if (object == null) return;
            Profile myProfile = (Profile) object;
            this.username = myProfile.getUsername();

            // Lấy thông tin đối thủ
            String opponentToken = game.getOpponent(move.getToken());
            if (opponentToken == null) return;
            this.opponentClientHandler = Cache.clientHandlerMap.get(opponentToken);

            // Kiểm tra kết thúc game
            String winnerToken = game.getWinner();
            if (winnerToken != null) {
                game.endGame(winnerToken);
                String winner;

                // Xác định người chiến thắng
                if (!winnerToken.equals("N/A")) {
                    object = Cache.token_profile.retrieveData(winnerToken);
                    if (object == null) return;
                    winner = ((Profile) object).getUsername();
                } else {
                    winner = "N/A";
                }

                // Gửi kết quả cho người chơi
                ClientHandler myClientHandler = Cache.clientHandlerMap.get(move.getToken());
                if (myClientHandler != null) {
                    // Tạo chuỗi JSON
                    JsonObject json = new JsonObject();
                    json.addProperty("winner", winner);
                    json.addProperty("score", game.getScore(move.getToken()));
                    String contentMessage = json.toString();

                    Message endGameMessage = new Message("end_game", contentMessage, "OK");
                    myClientHandler.send(endGameMessage);
                }
                if (this.opponentClientHandler != null) {
                    // Tạo chuỗi JSON cho đối thủ
                    JsonObject json = new JsonObject();
                    json.addProperty("winner", winner);
                    json.addProperty("score", game.getScore(opponentToken));
                    String contentMessage = json.toString();

                    Message endGameMessage = new Message("end_game", contentMessage, "OK");
                    this.opponentClientHandler.send(endGameMessage);
                }
            }
        }
    }

    /**
     * Tạo phản hồi cho người chơi sau khi thực hiện di chuyển.
     * Phương thức này tạo ra một thông điệp chứa thông tin về di chuyển và gửi cho đối thủ.
     *
     * @param decryptedContent Nội dung đã được giải mã của thông điệp đến.
     * @return Một đối tượng Message chứa thông tin di chuyển, hoặc null nếu không có tên người chơi.
     */
    @Override
    protected Message generateResponse(String decryptedContent) {
        if (this.username == null) return null;

        // Tạo chuỗi JSON
        JsonObject json = new JsonObject();
        json.addProperty("username", this.username);
        json.addProperty("step", this.move.getStep());
        json.addProperty("row", this.move.getRow());
        json.addProperty("col", this.move.getCol());
        json.addProperty("size", this.move.getSize());
        json.addProperty("timeLimit", Game.TIME_LIMIT);
        String contentMessage = json.toString();

        // Gửi move cho đối thủ
        Message reponseMessage = new Message("move", contentMessage, "OK");
        if (this.opponentClientHandler != null) {
            opponentClientHandler.send(reponseMessage);
        }

        // Xử lí giới hạn thời gian
        this.game.countdownHandler(this.username, this.move);

        return reponseMessage;
    }
}
