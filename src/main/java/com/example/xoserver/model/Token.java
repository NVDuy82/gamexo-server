package com.example.xoserver.model;

/**
 * Lớp Token đại diện cho thông tin về một mã token của người dùng trong hệ thống.
 * Lớp này lưu trữ tên người dùng và mã token tương ứng.
 */
public class Token {
    private String username; // Tên người dùng
    private String token; // Mã token

    /**
     * Khởi tạo một đối tượng Token với tên người dùng và mã token.
     *
     * @param username Tên người dùng
     * @param token Mã token
     */
    public Token(String username, String token) {
        this.username = username;
        this.token = token;
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
     * Lấy mã token.
     *
     * @return Mã token
     */
    public String getToken() {
        return token;
    }

    /**
     * Thiết lập mã token.
     *
     * @param token Mã token
     */
    public void setToken(String token) {
        this.token = token;
    }
}
