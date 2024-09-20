package com.example.xoserver.data;

import com.example.shared.Message;
import com.example.xoserver.ClientHandler;
import com.example.xoserver.connection.SqlConnection;
import com.example.xoserver.dao.DataAccessObject;
import com.example.xoserver.dao.ProfileDAO;
import com.example.xoserver.model.Profile;
import com.google.gson.JsonObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Game đại diện cho một trò chơi trong hệ thống.
 * Lớp này quản lý thông tin về người chơi, điểm số, các nước đi và trạng thái của trò chơi.
 */
public class Game {
    public static final int TIME_LIMIT = 30; // Giới hạn thời gian cho mỗi nước đi
    private final String tokenFirst; // Mã token của người chơi thứ nhất
    private final String tokenSecond; // Mã token của người chơi thứ hai
    private final List<Move> moves; // Danh sách các nước đi đã thực hiện
    private final Move[][] squares = new Move[3][3]; // Bảng cờ
    private final Pocket pocketFirst; // Túi quân cờ của người chơi thứ nhất
    private final Pocket pocketSecond; // Túi quân cờ của người chơi thứ hai
    private int scoreGap; // Chênh lệch điểm số giữa hai người chơi
    private int score; // Điểm số của người thắng
    private boolean isGameOver; // Trạng thái của trò chơi
    private String winnerToken; // Mã token của người thắng

    /**
     * Khởi tạo một phiên chơi mới với hai người chơi.
     *
     * @param tokenFirst Mã token của người chơi thứ nhất
     * @param tokenSecond Mã token của người chơi thứ hai
     * @param scoreA Điểm số ban đầu của người chơi thứ nhất
     * @param scoreB Điểm số ban đầu của người chơi thứ hai
     */
    public Game(String tokenFirst, String tokenSecond, int scoreA, int scoreB) {
        moves = new ArrayList<>();
        this.isGameOver = false;
        this.tokenFirst = tokenFirst;
        this.tokenSecond = tokenSecond;
        this.pocketFirst = new Pocket();
        this.pocketSecond = new Pocket();
        this.scoreGap = scoreA - scoreB;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                squares[i][j] = null;
            }
        }
    }

    /**
     * Kết thúc trò chơi và cập nhật thông tin về người thắng và người thua.
     *
     * @param winnerToken Mã token của người thắng
     */
    public void endGame(String winnerToken) {
        if (this.isGameOver) return;

        this.isGameOver = true;
        this.winnerToken = winnerToken;
        this.scoreGap = winnerToken.equals(tokenFirst) ? this.scoreGap : -this.scoreGap;
        this.score = winnerToken.equals("N/A") ? 5 : computeScore(scoreGap);

        Profile winner, loser;
        if (winnerToken.equals("N/A")) {
            winner = (Profile) Cache.token_profile.retrieveData(tokenFirst);
            loser = (Profile) Cache.token_profile.retrieveData(tokenSecond);

            winner.setDraw(winner.getDraw() + 1);
            loser.setDraw(loser.getDraw() + 1);
        } else {
            winner = (Profile) Cache.token_profile.retrieveData(winnerToken);
            loser = (Profile) Cache.token_profile.retrieveData(getOpponent(winnerToken));

            winner.setWin(winner.getWin() + 1);
            loser.setLose(loser.getLose() + 1);
        }

        winner.setScore(winner.getScore() + this.score);
        winner.setMatches(winner.getMatches() + 1);

        loser.setScore(Math.max(loser.getScore() - this.score, 1000));
        loser.setMatches(loser.getMatches() + 1);

        try {
            DataAccessObject<String, Profile> profileDataAccessObject = new ProfileDAO(SqlConnection.connection);
            profileDataAccessObject.update(winner);
            profileDataAccessObject.update(loser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tính toán điểm số dựa trên chênh lệch điểm.
     *
     * @param scoreGap Chênh lệch điểm giữa hai người chơi
     * @return Điểm số tính được
     */
    private int computeScore(int scoreGap) {
        if (scoreGap > 0) return 10;
        if (scoreGap < -80) return 30;
        return -scoreGap / 4 + 10;
    }

    /**
     * Lấy điểm số của người chơi dựa trên mã token của họ.
     *
     * @param token Mã token của người chơi
     * @return Điểm số của người chơi
     */
    public int getScore(String token) {
        return !token.equals(this.winnerToken) ? (this.score / 2) : this.score;
    }

    /**
     * Lấy mã token của người chơi thứ nhất.
     *
     * @return Mã token của người chơi thứ nhất
     */
    public String getTokenFirst() {
        return tokenFirst;
    }

    /**
     * Lấy mã token của người chơi thứ hai.
     *
     * @return Mã token của người chơi thứ hai
     */
    public String getTokenSecond() {
        return tokenSecond;
    }

    /**
     * Lấy mã token của đối thủ dựa trên mã token của người chơi.
     *
     * @param token Mã token của người chơi
     * @return Mã token của đối thủ
     */
    public String getOpponent(String token) {
        if (token.equals(tokenFirst)) return tokenSecond;
        if (token.equals(tokenSecond)) return tokenFirst;

        return null;
    }

    /**
     * Lấy túi quân cờ của người chơi thứ nhất.
     *
     * @return Túi quân cờ của người chơi thứ nhất
     */
    public Pocket getPocketFirst() {
        return pocketFirst;
    }

    /**
     * Lấy túi quân cờ của người chơi thứ hai.
     *
     * @return Túi quân cờ của người chơi thứ hai
     */
    public Pocket getPocketSecond() {
        return pocketSecond;
    }

    /**
     * Kiểm tra xem trò chơi đã kết thúc hay chưa.
     *
     * @return true nếu trò chơi đã kết thúc, false nếu không
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Lấy số bước đi đã thực hiện trong trò chơi.
     *
     * @return Số bước đi đã thực hiện
     */
    public int getStep() {
        return moves.size();
    }

    /**
     * Thực hiện một nước đi trong trò chơi.
     *
     * @param m Nước đi cần thực hiện
     * @return true nếu nước đi hợp lệ, false nếu không
     */
    public boolean move(Move m) {
        if (isGameOver) return false;
        if (!correctMove(m)) return false;

        Pocket pocket = m.getToken().equals(tokenFirst) ? pocketFirst : pocketSecond;
        if (pocket.useSize(m.getSize())) {
            this.moves.add(m);
            this.squares[m.getRow()][m.getCol()] = m;
            return true;
        }

        return false;
    }

    /**
     * Kiểm tra tính hợp lệ của một nước đi.
     *
     * @param move Nước đi cần kiểm tra
     * @return true nếu nước đi hợp lệ, false nếu không
     */
    private boolean correctMove(Move move) {
        if (move.getStep() != this.moves.size() + 1) return false;

        if (!move.getToken().equals(this.tokenFirst) && !move.getToken().equals(this.tokenSecond)) return false;
        if (!move.getToken().equals((this.moves.size() % 2 == 0) ? tokenFirst : tokenSecond)) return false;

        int row = move.getRow();
        int col = move.getCol();

        Move square = squares[row][col];
        if (square == null) return true;

        return !move.getToken().equals(square.getToken()) && move.getSize() > square.getSize();
    }

    /**
     * Xác định người thắng cuộc trong trò chơi.
     *
     * @return Mã token của người thắng, "N/A" nếu hòa, hoặc null nếu chưa có người thắng
     */
    public String getWinner() {
        String winner = checkRow();
        if (winner == null) winner = checkCol();
        if (winner == null) winner = checkCross();
        if (winner != null) return winner;

        return (pocketFirst.isEmpty() && pocketSecond.isEmpty()) ? "N/A" : null;
    }

    /**
     * Kiểm tra hàng để tìm người thắng cuộc.
     *
     * @return Mã token của người thắng hoặc null nếu không có
     */
    private String checkRow() {
        for (int i = 0; i < 3; i++) {
            int count = 1;
            if (squares[i][0] == null) continue;

            for (int j = 1; j < 3; j++) {
                if (squares[i][j] == null) break;

                String prev = squares[i][j - 1].getToken();
                String curr = squares[i][j].getToken();

                if (prev.equals(curr)) {
                    count++;
                    if (count == 3) return curr;
                }
            }
        }
        return null;
    }
    /**
     * Kiểm tra cột để tìm người thắng cuộc.
     *
     * @return Mã token của người thắng hoặc null nếu không có
     */
    private String checkCol() {
        for (int j = 0; j < 3; j++) {
            int count = 1;
            if (squares[0][j] == null) continue;

            for (int i = 1; i < 3; i++) {
                if (squares[i][j] == null) break;

                String prev = squares[i - 1][j].getToken();
                String curr = squares[i][j].getToken();

                if (prev.equals(curr)) {
                    count++;
                    if (count == 3) return curr;
                }
            }
        }
        return null;
    }

    /**
     * Kiểm tra đường chéo để tìm người thắng cuộc.
     *
     * @return Mã token của người thắng hoặc null nếu không có
     */
    private String checkCross() {
        Move square = squares[1][1];
        if (square == null) return null;

        String token = square.getToken();

        if (squares[0][0] != null && squares[2][2] != null) {
            if (squares[0][0].getToken().equals(token) && squares[2][2].getToken().equals(token)) return token;
        }

        if (squares[0][2] != null && squares[2][0] != null) {
            if (squares[0][2].getToken().equals(token) && squares[2][0].getToken().equals(token)) return token;
        }

        return null;
    }

    /**
     * Xử lý đồng hồ đếm ngược cho mỗi nước đi.
     * Nếu thời gian hết mà người chơi chưa thực hiện nước đi, trò chơi sẽ kết thúc.
     *
     * @param username Tên người dùng của người chơi
     * @param move Nước đi đang được thực hiện
     */
    public void countdownHandler(String username, Move move) {
        new Thread(() -> {
            try {
                // Chờ
                Thread.sleep(TIME_LIMIT * 1000);

                // Kiểm tra đã có nước đi mới chưa
                if (moves.size() == move.getStep()) {
                    if (username != null) {
                        if (this.isGameOver) return;

                        // Kết thúc game
                        this.endGame(move.getToken());

                        new Thread(() -> {
                            timeLimitHandler(this.tokenFirst, username, move);
                        }).start();

                        new Thread(() -> {
                            timeLimitHandler(this.tokenSecond, username, move);
                        }).start();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * Xử lý khi thời gian hết cho một người chơi.
     * Gửi thông báo kết thúc trò chơi cho người chơi.
     *
     * @param token Mã token của người chơi
     * @param username Tên người dùng của người chơi
     * @param move Nước đi hiện tại
     */
    private void timeLimitHandler(String token, String username, Move move) {
        ClientHandler clientHandler = Cache.clientHandlerMap.get(token);
        if (clientHandler == null) return;

        // Tạo chuỗi JSON
        JsonObject json = new JsonObject();
        json.addProperty("winner", username);
        json.addProperty("score", this.getScore(token));
        String contentMessage = json.toString();

        // Gửi kết quả cho người chơi
        Message endGameMessage = new Message("end_game", contentMessage, "OK");
        clientHandler.send(endGameMessage);
    }
}