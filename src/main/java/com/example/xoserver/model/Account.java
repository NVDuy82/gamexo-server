package com.example.xoserver.model;

/**
 * Lớp Account đại diện cho tài khoản người dùng trong hệ thống.
 * Lớp này lưu trữ thông tin liên quan đến tài khoản như tên người dùng, mật khẩu và email.
 */
public class Account {
    private String username; // Tên người dùng
    private String password; // Mật khẩu
    private String email; // Địa chỉ email

    /**
     * Khởi tạo một tài khoản mới với tên người dùng, mật khẩu và email.
     *
     * @param username Tên người dùng
     * @param password Mật khẩu
     * @param email Địa chỉ email
     */
    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * Lấy tên người dùng.
     *
     * @return Tên người dùng
     */
    public String getUsername() {
        return username;
    }

    /**
     * Thiết lập tên người dùng.
     *
     * @param username Tên người dùng
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Lấy mật khẩu.
     *
     * @return Mật khẩu
     */
    public String getPassword() {
        return password;
    }

    /**
     * Thiết lập mật khẩu.
     *
     * @param password Mật khẩu
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Lấy địa chỉ email.
     *
     * @return Địa chỉ email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Thiết lập địa chỉ email.
     *
     * @param email Địa chỉ email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
