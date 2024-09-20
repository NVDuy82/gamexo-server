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
 * Lớp CancelFinding kế thừa từ MessageProcessor, dùng để xử lý yêu cầu hủy tìm kiếm đối thủ.
 */
public class CancelFinding extends MessageProcessor {
    /**
     * Thực hiện các tác vụ cần thiết để hủy tìm kiếm đối thủ.
     * Phân tích nội dung thông điệp và kiểm tra tính hợp lệ.
     *
     * @param decryptedContent Nội dung đã giải mã
     * @param status Trạng thái hiện tại của người dùng
     */
    @Override
    protected void performTask(String decryptedContent, String status) {
        // Phân tích thông điệp JSON
        JsonObject jsonObject = JsonParser.parseString(decryptedContent).getAsJsonObject();
        String token = jsonObject.get("token").getAsString();

        // Lấy thông tin người dùng từ cache
        Object retrievedData = Cache.token_profile.retrieveData(token);
        if (retrievedData == null) return;

        Profile profile = (Profile) retrievedData;
        String username = profile.getUsername();
        Profile myProfile = null;

        // Lấy thông tin profile từ cơ sở dữ liệu
        try {
            DataAccessObject<String, Profile> accessProfile = new ProfileDAO(SqlConnection.connection);
            myProfile = accessProfile.get(username);
        } catch (SQLException e) {
            // Xử lý lỗi nếu cần
        }

        if (myProfile == null) return;

        // Xử lý tìm đối thủ
        int rank = myProfile.getScore() / 100; // Tính hạng dựa trên điểm số
        if (Match.finder.containsKey(rank)) {
            WaitingPlayer waitingPlayer = Match.finder.get(rank);
            // Kiểm tra xem người dùng có trong danh sách chờ không
            if (waitingPlayer.getProfile().getUsername().equals(myProfile.getUsername())) {
                Match.finder.remove(rank); // Hủy tìm kiếm nếu đúng
            }
        }
    }

    /**
     * Tạo và trả về thông điệp phản hồi.
     *
     * @param decryptedContent Nội dung đã giải mã
     * @return Thông điệp phản hồi
     */
    @Override
    protected Message generateResponse(String decryptedContent) {
        // Không có phản hồi cụ thể cho hành động hủy
        return new Message("cancel_finding", "Finding cancelled.", "OK");
    }
}
