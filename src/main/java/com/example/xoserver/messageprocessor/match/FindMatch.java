package com.example.xoserver.messageprocessor.match;

import com.example.shared.Message;
import com.example.xoserver.ClientHandler;
import com.example.xoserver.connection.SqlConnection;
import com.example.xoserver.dao.DataAccessObject;
import com.example.xoserver.dao.ProfileDAO;
import com.example.xoserver.data.*;
import com.example.xoserver.messageprocessor.MessageProcessor;
import com.example.xoserver.model.Profile;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.sql.SQLException;

/**
 * Lớp FindMatch chịu trách nhiệm xử lý các yêu cầu tìm kiếm trận đấu từ người chơi.
 * Nó quản lý logic ghép cặp người chơi dựa trên hạng của họ và khởi tạo một trận đấu.
 */
public class FindMatch extends MessageProcessor {

    /** Thông tin profile của người chơi hiện tại. */
    private Profile myProfile;

    /** Thông tin profile của đối thủ. */
    private Profile opponentProfile;

    /** Thể hiện trận đấu cho trận đấu hiện tại. */
    private Game game;

    /** Định danh duy nhất cho trận đấu. */
    private String idMatch;

    /** Client handler cho đối thủ. */
    private ClientHandler opponentClientHandler;

    /**
     * Xử lý yêu cầu tìm kiếm trận đấu từ người chơi.
     * Phương thức này cố gắng ghép cặp người chơi hiện tại với một đối thủ dựa trên hạng của họ.
     * Nếu tìm thấy đối thủ, một trận đấu mới sẽ được khởi tạo.
     *
     * @param decryptedContent Nội dung đã được giải mã của thông điệp đến.
     * @param status Trạng thái của thông điệp (được sử dụng để ghi log hoặc theo dõi).
     */
    @Override
    protected void performTask(String decryptedContent, String status) {
        this.idMatch = null;

        // Lấy thông tin profile của người chơi
        this.myProfile = this.getProfile(this.token);
        if (this.myProfile == null) return;

        // Tải lại profile từ cơ sở dữ liệu
        this.myProfile = this.getProfileFromDB(this.myProfile.getUsername());
        if (myProfile == null) return;

        // Tính toán hạng dựa trên điểm số
        int rank = myProfile.getScore() / 100;

        // Tìm kiếm đối thủ
        while (this.opponentProfile == null) {
            if (!Match.finder.containsKey(rank)) {
                // Người chơi 1: Đưa dữ liệu vào hàng chờ
                WaitingPlayer waitingPlayer = new WaitingPlayer(myProfile, token);
                Match.finder.put(rank, waitingPlayer);
                break;

            } else {
                // Người chơi 2: Lấy thông tin hàng chờ
                WaitingPlayer waitingPlayer = Match.finder.get(rank);

                if (!waitingPlayer.isFull()) {
                    // Đã đủ 2 người: Thiết lập thông tin đối thủ
                    waitingPlayer.setFull(true);
                    this.opponentClientHandler = Cache.clientHandlerMap.get(waitingPlayer.getToken());

                    // Kiểm tra xem đối thủ còn online không
                    if (opponentClientHandler == null) {
                        Match.finder.remove(rank);
                        waitingPlayer.setFull(false);
                        continue;
                    }

                    // Lấy thông tin đối thủ và khởi tạo trận đấu
                    this.opponentProfile = waitingPlayer.getProfile();
                    this.idMatch = waitingPlayer.getIdMatch();
                    Match.opponentToken.storeData(token, waitingPlayer.getToken());

                    this.game = new Game(waitingPlayer.getToken(), this.token,
                            waitingPlayer.getProfile().getScore(), this.myProfile.getScore());
                    Match.game.storeData(this.idMatch, this.game);

                    // Xóa dữ liệu ở hàng chờ
                    Match.finder.remove(rank);
                    waitingPlayer.setFull(false);

                } else {
                    // Nếu đã có người chơi khác trong hàng chờ, chờ đợi
                    while (waitingPlayer.isFull()) {
                        try {
                            Thread.sleep(1000); // Chờ một giây
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }
        }
    }

    /**
     * Tạo phản hồi cho người chơi khi tìm thấy trận đấu.
     * Phương thức này tạo ra các thông điệp cần thiết để thông báo cho cả hai người chơi về chi tiết trận đấu.
     *
     * @param decryptedContent Nội dung đã được giải mã của thông điệp đến.
     * @return Một đối tượng Message chứa chi tiết trận đấu, hoặc null nếu không tìm thấy trận đấu.
     */
    @Override
    protected Message generateResponse(String decryptedContent) {
        if (this.idMatch == null) return null;

        // Tạo nội dung phản hồi cho cả hai người chơi
        String contentMessage = getContentCreateMatch(this.game, this.idMatch, opponentProfile, opponentProfile.getUsername());
        String opponentContentMessage = getContentCreateMatch(this.game, this.idMatch, myProfile, opponentProfile.getUsername());

        // Gửi thông điệp cho đối thủ
        opponentClientHandler.send(new Message("find_match", opponentContentMessage, "OK"));

        // Xử lý giới hạn thời gian
        this.game.countdownHandler(myProfile.getUsername(), new Move(token, 0, -1, -1, -1));

        return new Message("find_match", contentMessage, "OK");
    }
}
