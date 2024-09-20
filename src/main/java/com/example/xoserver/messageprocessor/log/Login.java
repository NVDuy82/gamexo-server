package com.example.xoserver.messageprocessor.log;

import com.example.shared.Message;
import com.example.xoserver.connection.SqlConnection;
import com.example.xoserver.dao.AccountDAO;
import com.example.xoserver.dao.DataAccessObject;
import com.example.xoserver.dao.ProfileDAO;
import com.example.xoserver.dao.TokenDAO;
import com.example.xoserver.data.Cache;
import com.example.xoserver.messageprocessor.MessageProcessor;
import com.example.xoserver.model.Account;
import com.example.xoserver.model.Profile;
import com.example.xoserver.model.Token;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.sql.SQLException;

/**
 * Lớp Login chịu trách nhiệm xử lý yêu cầu đăng nhập của người dùng.
 * Khi người dùng gửi thông tin đăng nhập, lớp này sẽ kiểm tra tính hợp lệ
 * và tạo một token để xác thực người dùng trong các phiên làm việc sau.
 */
public class Login extends MessageProcessor {

    /** Đối tượng tài khoản của người dùng. */
    private Account account;

    /** Đối tượng hồ sơ của người dùng. */
    private Profile profile;

    /**
     * Xử lý yêu cầu đăng nhập từ người dùng.
     * Phương thức này phân tích thông điệp đến, xác thực thông tin đăng nhập,
     * và lưu trữ thông tin người dùng nếu đăng nhập thành công.
     *
     * @param decryptedContent Nội dung đã được giải mã của thông điệp đến.
     * @param status Trạng thái của thông điệp (được sử dụng để ghi log hoặc theo dõi).
     */
    @Override
    protected void performTask(String decryptedContent, String status) {
        // Kiểm tra xem token đã tồn tại trong bộ nhớ cache chưa
        if (Cache.token_profile.containsKey(this.token)) return;

        // Phân tích thông điệp JSON
        JsonObject jsonObject = JsonParser.parseString(decryptedContent).getAsJsonObject();
        String usernameFromClient = jsonObject.get("username").getAsString();
        String passwordFromClient = jsonObject.get("password").getAsString();

        // Kiểm tra thông tin tài khoản
        DataAccessObject<String, Account> accessAccount = new AccountDAO(SqlConnection.connection);
        DataAccessObject<String, Profile> accessInfo = new ProfileDAO(SqlConnection.connection);
        try {
            Account accountFromDB = accessAccount.get(usernameFromClient);

            // Xử lý thông tin sai
            if (accountFromDB == null || !passwordFromClient.equals(accountFromDB.getPassword())) {
                account = null;
                profile = null;
                return;
            }

            // Thông tin đăng nhập là đúng
            account = accountFromDB;
            profile = accessInfo.get(usernameFromClient);
        } catch (SQLException e) {
            account = null;
            profile = null;
            e.printStackTrace();
        }
    }

    /**
     * Tạo phản hồi cho người dùng sau khi yêu cầu đăng nhập.
     * Phương thức này sẽ trả về thông điệp chứa thông tin về việc
     * đăng nhập thành công hay thất bại.
     *
     * @param decryptedContent Nội dung đã được giải mã của thông điệp đến.
     * @return Một đối tượng Message chứa thông tin phản hồi cho người dùng.
     */
    @Override
    protected Message generateResponse(String decryptedContent) {
        // Kiểm tra xem tài khoản và hồ sơ có hợp lệ không
        if (account == null || profile == null)
            return new Message("login", "Tài khoản hoặc mật khẩu không đúng!", "WRONG");

        try {
            DataAccessObject<String, Token> accessToken = new TokenDAO(SqlConnection.connection);
            Token tokenCurrent = accessToken.get(account.getUsername());

            // Kiểm tra xem tài khoản đã được đăng nhập ở nơi khác chưa
            if (tokenCurrent != null)
                return new Message("login", "Tài khoản đã được đăng nhập ở nơi khác", "WRONG");

            // Lưu token cho tài khoản
            accessToken.add(new Token(account.getUsername(), token));
            Cache.token_profile.storeData(token, profile);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Tạo JsonObject từ profile
        JsonObject jsonObject = new Gson().toJsonTree(profile).getAsJsonObject();
        jsonObject.addProperty("email", account.getEmail());
        jsonObject.addProperty("token", token);

        // Tạo nội dung thông điệp
        String contentMessage = jsonObject.toString();

        // Tạo message để gửi thông điệp
        return new Message("login", contentMessage, "OK");
    }
}
