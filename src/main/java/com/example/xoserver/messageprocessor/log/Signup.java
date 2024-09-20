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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lớp Signup chịu trách nhiệm xử lý yêu cầu đăng ký tài khoản của người dùng.
 * Khi người dùng gửi thông tin đăng ký, lớp này sẽ kiểm tra tính hợp lệ
 * và lưu trữ thông tin tài khoản cũng như hồ sơ vào cơ sở dữ liệu.
 */
public class Signup extends MessageProcessor {

    /** Đối tượng tài khoản của người dùng. */
    private Account account;

    /** Đối tượng hồ sơ của người dùng. */
    private Profile profile;

    /** Thông báo lỗi nếu có. */
    private String wrongMessage;

    /**
     * Xử lý yêu cầu đăng ký từ người dùng.
     * Phương thức này phân tích thông điệp đến, xác thực thông tin đăng ký,
     * và lưu trữ thông tin tài khoản và hồ sơ nếu đăng ký thành công.
     *
     * @param decryptedContent Nội dung đã được giải mã của thông điệp đến.
     * @param status Trạng thái của thông điệp (được sử dụng để ghi log hoặc theo dõi).
     */
    @Override
    protected void performTask(String decryptedContent, String status) {
        // Phân tích thông điệp JSON
        JsonObject jsonObject = JsonParser.parseString(decryptedContent).getAsJsonObject();
        String usernameFromClient = jsonObject.get("username").getAsString();
        String passwordFromClient = jsonObject.get("password").getAsString();
        String nameFromClient = jsonObject.get("name").getAsString();
        String emailFromClient = jsonObject.get("email").getAsString();

        // Kiểm tra thông tin tài khoản
        DataAccessObject<String, Account> accessAccount = new AccountDAO(SqlConnection.connection);
        DataAccessObject<String, Profile> accessProfile = new ProfileDAO(SqlConnection.connection);
        try {
            Account accountFromDB = accessAccount.get(usernameFromClient);

            // Xử lý thông tin sai
            wrongMessage = null;
            if (usernameFromClient.equals("N/A") || accountFromDB != null) {
                wrongMessage = "Account already exists.";
                return;
            }
            if (!isValidEmail(emailFromClient)) {
                wrongMessage = "Email address has a wrong format error.";
                return;
            }

            // Thông tin đăng ký là đúng
            account = new Account(usernameFromClient, passwordFromClient, emailFromClient);
            profile = new Profile(usernameFromClient, nameFromClient, 1000, 0, 0, 0, 0);
            accessAccount.add(account);
            accessProfile.add(profile);
        } catch (SQLException e) {
            account = null;
            profile = null;
            e.printStackTrace();
        }
    }

    /**
     * Tạo phản hồi cho người dùng sau khi yêu cầu đăng ký.
     * Phương thức này sẽ trả về thông điệp chứa thông tin về việc
     * đăng ký thành công hay thất bại.
     *
     * @param decryptedContent Nội dung đã được giải mã của thông điệp đến.
     * @return Một đối tượng Message chứa thông tin phản hồi cho người dùng.
     */
    @Override
    protected Message generateResponse(String decryptedContent) {
        if (wrongMessage != null)
            return new Message("signup", wrongMessage, "WRONG");

        try {
            DataAccessObject<String, Token> accessToken = new TokenDAO(SqlConnection.connection);
            Token tokenCurrent = accessToken.get(account.getUsername());

            // Kiểm tra xem tài khoản đã được đăng nhập ở nơi khác chưa
            if (tokenCurrent != null)
                return new Message("login", "The account is already logged in elsewhere.", "WRONG");

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

    /**
     * Kiểm tra định dạng của địa chỉ email.
     * Sử dụng biểu thức chính quy để xác thực định dạng email hợp lệ.
     *
     * @param email Địa chỉ email cần kiểm tra.
     * @return true nếu email hợp lệ, false nếu không hợp lệ.
     */
    private boolean isValidEmail(String email) {
        // Biểu thức chính quy kiểm tra định dạng email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        // Kiểm tra xem chuỗi có khớp với biểu thức chính quy không
        return matcher.matches();
    }
}
