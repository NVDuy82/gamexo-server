package com.example.xoserver.dao;

import com.example.xoserver.model.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này thực hiện các thao tác truy cập dữ liệu liên quan đến tài khoản.
 */
public class AccountDAO implements DataAccessObject<String, Account> {
    private Connection connection;

    /**
     * Khởi tạo một AccountDAO với kết nối cơ sở dữ liệu.
     *
     * @param connection Kết nối đến cơ sở dữ liệu.
     */
    public AccountDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Lấy tất cả tài khoản từ cơ sở dữ liệu.
     *
     * @return Danh sách tất cả tài khoản.
     * @throws SQLException Nếu có lỗi xảy ra khi truy vấn cơ sở dữ liệu.
     */
    @Override
    public List<Account> getAll() throws SQLException {
        List<Account> accounts = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM xo_schema.account";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String email = resultSet.getString("email");

            Account account = new Account(username, password, email);
            accounts.add(account);
        }

        return accounts;
    }

    /**
     * Lấy tài khoản theo tên người dùng.
     *
     * @param key Tên người dùng.
     * @return Tài khoản tương ứng hoặc null nếu không tìm thấy.
     * @throws SQLException Nếu có lỗi xảy ra khi truy vấn cơ sở dữ liệu.
     */
    @Override
    public Account get(String key) throws SQLException {
        String query = "SELECT * FROM xo_schema.account WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, key);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String email = resultSet.getString("email");

            return new Account(username, password, email);
        }

        return null;
    }

    /**
     * Thêm tài khoản mới vào cơ sở dữ liệu.
     *
     * @param account Tài khoản cần thêm.
     * @throws SQLException Nếu có lỗi xảy ra khi thực hiện thêm tài khoản.
     */
    @Override
    public void add(Account account) throws SQLException {
        String query = "INSERT INTO xo_schema.account VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());
        preparedStatement.setString(3, account.getEmail());
        preparedStatement.executeUpdate();
    }

    /**
     * Cập nhật thông tin tài khoản trong cơ sở dữ liệu.
     *
     * @param account Tài khoản cần cập nhật.
     * @throws SQLException Nếu có lỗi xảy ra khi thực hiện cập nhật tài khoản.
     */
    @Override
    public void update(Account account) throws SQLException {
        String query = "UPDATE xo_schema.account SET password = ?, email = ? WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(3, account.getUsername());
        preparedStatement.setString(1, account.getPassword());
        preparedStatement.setString(2, account.getEmail());
        preparedStatement.executeUpdate();
    }

    /**
     * Xóa tài khoản khỏi cơ sở dữ liệu.
     *
     * @param key Tên người dùng của tài khoản cần xóa.
     * @throws SQLException Nếu có lỗi xảy ra khi thực hiện xóa tài khoản.
     */
    @Override
    public void delete(String key) throws SQLException {
        String query = "DELETE FROM xo_schema.account WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, key);
        preparedStatement.executeUpdate();
    }
}
