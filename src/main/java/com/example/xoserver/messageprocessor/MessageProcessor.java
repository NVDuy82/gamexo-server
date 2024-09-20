package com.example.xoserver.messageprocessor;

import com.example.shared.Message;
import com.example.xoserver.connection.SqlConnection;
import com.example.xoserver.dao.DataAccessObject;
import com.example.xoserver.dao.ProfileDAO;
import com.example.xoserver.data.Cache;
import com.example.xoserver.data.Game;
import com.example.xoserver.model.Profile;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.sql.SQLException;

/**
 * Lớp trừu tượng MessageProcessor dùng để xử lý các thông điệp từ người dùng.
 * Lớp này định nghĩa các phương thức cần thiết để giải mã, thực hiện công việc
 * và gửi phản hồi về cho người dùng.
 */
public abstract class MessageProcessor {
    protected String token; // Mã token của người dùng

    /**
     * Xử lý thông điệp nhận được từ người dùng.
     *
     * @param receivedMessage Thông điệp đã nhận
     * @param status Trạng thái hiện tại của người dùng
     * @return Thông điệp phản hồi
     */
    public Message processMessage(String receivedMessage, String status) {
        // Giải mã và thực hiện xử lý công việc
        String decryptedContent = decrypt(receivedMessage);
        // Thực hiện công việc
        performTask(decryptedContent, status);
        // Gửi phản hồi
        return generateResponse(decryptedContent);
    }

    /**
     * Giải mã nội dung thông điệp đã nhận.
     *
     * @param encryptedContent Nội dung đã mã hóa
     * @return Nội dung đã giải mã
     */
    protected String decrypt(String encryptedContent) {
        return encryptedContent; // Placeholder cho việc giải mã thực sự
    }

    /**
     * Thực hiện các công việc cần thiết dựa trên nội dung đã giải mã.
     *
     * @param decryptedContent Nội dung đã giải mã
     * @param status Trạng thái hiện tại của người dùng
     */
    protected abstract void performTask(String decryptedContent, String status);

    /**
     * Tạo và trả về thông điệp phản hồi dựa trên nội dung đã giải mã.
     *
     * @param decryptedContent Nội dung đã giải mã
     * @return Thông điệp phản hồi
     */
    protected abstract Message generateResponse(String decryptedContent);

    /**
     * Lấy mã token của người dùng.
     *
     * @return Mã token
     */
    public String getToken() {
        return token;
    }

    /**
     * Thiết lập mã token cho người dùng.
     *
     * @param token Mã token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Lấy hồ sơ người dùng từ cache dựa trên mã token.
     *
     * @param token Mã token của người dùng
     * @return Hồ sơ người dùng, hoặc null nếu không tìm thấy
     */
    protected Profile getProfile(String token) {
        Object retrievedData = Cache.token_profile.retrieveData(token);
        if (retrievedData == null) return null;

        return (Profile) retrievedData;
    }

    /**
     * Lấy hồ sơ người dùng từ cơ sở dữ liệu dựa trên tên người dùng.
     *
     * @param username Tên người dùng
     * @return Hồ sơ người dùng, hoặc null nếu không tìm thấy hoặc có lỗi
     */
    protected Profile getProfileFromDB(String username) {
        DataAccessObject<String, Profile> accessProfile = new ProfileDAO(SqlConnection.connection);
        try {
            return accessProfile.get(username);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Tạo chuỗi JSON chứa thông tin về trận đấu và người chơi.
     *
     * @param game Trận đấu
     * @param idMatch ID của trận đấu
     * @param profile Hồ sơ của người chơi
     * @param firstPlayer Tên người chơi đầu tiên
     * @return Chuỗi JSON chứa thông tin trận đấu
     */
    protected String getContentCreateMatch(Game game, String idMatch, Profile profile, String firstPlayer) {
        // Tạo chuỗi JSON
        JsonObject json = new Gson().toJsonTree(profile).getAsJsonObject();

        json.addProperty("idMatch", idMatch);
        json.addProperty("firstPlayer", firstPlayer);
        json.addProperty("timeLimit", Game.TIME_LIMIT);
        json.addProperty("fSmall", game.getPocketFirst().getNumberOfSize(0));
        json.addProperty("fMedium", game.getPocketFirst().getNumberOfSize(1));
        json.addProperty("fLarge", game.getPocketFirst().getNumberOfSize(2));
        json.addProperty("sSmall", game.getPocketSecond().getNumberOfSize(0));
        json.addProperty("sMedium", game.getPocketSecond().getNumberOfSize(1));
        json.addProperty("sLarge", game.getPocketSecond().getNumberOfSize(2));

        return json.toString();
    }
}
