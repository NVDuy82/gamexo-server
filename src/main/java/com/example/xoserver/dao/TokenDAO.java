package com.example.xoserver.dao;

import com.example.xoserver.model.Token;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này thực hiện các thao tác CRUD cho đối tượng Token trong cơ sở dữ liệu.
 */
public class TokenDAO implements DataAccessObject<String, Token> {
    private Connection connection;

    /**
     * Khởi tạo TokenDAO với kết nối cơ sở dữ liệu.
     *
     * @param connection Kết nối đến cơ sở dữ liệu.
     */
    public TokenDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Lấy tất cả các token từ cơ sở dữ liệu.
     *
     * @return Danh sách tất cả các Token.
     * @throws SQLException Nếu có lỗi xảy ra khi truy vấn cơ sở dữ liệu.
     */
    @Override
    public List<Token> getAll() throws SQLException {
        List<Token> tokens = new ArrayList<>();

        String query = "SELECT * FROM xo_schema.token";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            String username = resultSet.getString("username");
            String tokenCode = resultSet.getString("token");

            Token token = new Token(username, tokenCode);
            tokens.add(token);
        }

        return tokens;
    }

    /**
     * Lấy token theo tên người dùng.
     *
     * @param key Tên người dùng của token cần tìm.
     * @return Đối tượng Token tương ứng hoặc null nếu không tìm thấy.
     * @throws SQLException Nếu có lỗi xảy ra khi truy vấn cơ sở dữ liệu.
     */
    @Override
    public Token get(String key) throws SQLException {
        String query = "SELECT * FROM xo_schema.token WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, key);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String username = resultSet.getString("username");
            String tokenCode = resultSet.getString("token");

            return new Token(username, tokenCode);
        }

        return null;
    }

    /**
     * Thêm một token mới vào cơ sở dữ liệu.
     *
     * @param token Đối tượng Token cần thêm.
     * @throws SQLException Nếu có lỗi xảy ra khi thực hiện thêm token.
     */
    @Override
    public void add(Token token) throws SQLException {
        String query = "INSERT INTO xo_schema.token VALUES (?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, token.getUsername());
        preparedStatement.setString(2, token.getToken());
        preparedStatement.executeUpdate();
    }

    /**
     * Cập nhật token trong cơ sở dữ liệu.
     *
     * @param token Đối tượng Token cần cập nhật.
     * @throws SQLException Nếu có lỗi xảy ra khi thực hiện cập nhật token.
     */
    @Override
    public void update(Token token) throws SQLException {
        String query = "UPDATE xo_schema.token SET token = ? WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(2, token.getUsername());
        preparedStatement.setString(1, token.getToken());
        preparedStatement.executeUpdate();
    }

    /**
     * Xóa token khỏi cơ sở dữ liệu.
     *
     * @param key Tên người dùng của token cần xóa.
     * @throws SQLException Nếu có lỗi xảy ra khi thực hiện xóa token.
     */
    @Override
    public void delete(String key) throws SQLException {
        String query = "DELETE FROM xo_schema.token WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, key);
        preparedStatement.executeUpdate();
    }
}
