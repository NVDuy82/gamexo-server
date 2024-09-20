package com.example.xoserver.dao;

import com.example.xoserver.model.Profile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này thực hiện các thao tác CRUD cho đối tượng Profile trong cơ sở dữ liệu.
 */
public class ProfileDAO implements DataAccessObject<String, Profile> {
    private Connection connection;

    /**
     * Khởi tạo ProfileDAO với kết nối cơ sở dữ liệu.
     *
     * @param connection Kết nối đến cơ sở dữ liệu.
     */
    public ProfileDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Lấy tất cả các hồ sơ từ cơ sở dữ liệu.
     *
     * @return Danh sách tất cả các Profile.
     * @throws SQLException Nếu có lỗi xảy ra khi truy vấn cơ sở dữ liệu.
     */
    @Override
    public List<Profile> getAll() throws SQLException {
        List<Profile> profiles = new ArrayList<>();

        Statement statement = connection.createStatement();
        String query = "SELECT * FROM xo_schema.profile";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            String username = resultSet.getString("username");
            String name = resultSet.getString("name");
            int score = resultSet.getInt("score");
            int matches = resultSet.getInt("matches");
            int win = resultSet.getInt("win");
            int lose = resultSet.getInt("lose");
            int draw = resultSet.getInt("draw");

            Profile profile = new Profile(username, name, score, matches, win, lose, draw);
            profiles.add(profile);
        }

        return profiles;
    }

    /**
     * Lấy hồ sơ theo tên người dùng.
     *
     * @param key Tên người dùng của hồ sơ cần tìm.
     * @return Đối tượng Profile tương ứng hoặc null nếu không tìm thấy.
     * @throws SQLException Nếu có lỗi xảy ra khi truy vấn cơ sở dữ liệu.
     */
    @Override
    public Profile get(String key) throws SQLException {
        String query = "SELECT * FROM xo_schema.profile WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, key);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String username = resultSet.getString("username");
            String name = resultSet.getString("name");
            int score = resultSet.getInt("score");
            int matches = resultSet.getInt("matches");
            int win = resultSet.getInt("win");
            int lose = resultSet.getInt("lose");
            int draw = resultSet.getInt("draw");

            return new Profile(username, name, score, matches, win, lose, draw);
        }

        return null;
    }

    /**
     * Thêm một hồ sơ mới vào cơ sở dữ liệu.
     *
     * @param profile Đối tượng Profile cần thêm.
     * @throws SQLException Nếu có lỗi xảy ra khi thực hiện thêm hồ sơ.
     */
    @Override
    public void add(Profile profile) throws SQLException {
        String query = "INSERT INTO xo_schema.profile VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, profile.getUsername());
        preparedStatement.setString(2, profile.getName());
        preparedStatement.setInt(3, profile.getScore());
        preparedStatement.setInt(4, profile.getMatches());
        preparedStatement.setInt(5, profile.getWin());
        preparedStatement.setInt(6, profile.getLose());
        preparedStatement.setInt(7, profile.getDraw());
        preparedStatement.executeUpdate();
    }

    /**
     * Cập nhật thông tin hồ sơ trong cơ sở dữ liệu.
     *
     * @param profile Đối tượng Profile cần cập nhật.
     * @throws SQLException Nếu có lỗi xảy ra khi thực hiện cập nhật hồ sơ.
     */
    @Override
    public void update(Profile profile) throws SQLException {
        String query = "UPDATE xo_schema.profile SET name = ?, score = ?, matches = ?, win = ?, lose = ?, draw = ? WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(7, profile.getUsername());
        preparedStatement.setString(1, profile.getName());
        preparedStatement.setInt(2, profile.getScore());
        preparedStatement.setInt(3, profile.getMatches());
        preparedStatement.setInt(4, profile.getWin());
        preparedStatement.setInt(5, profile.getLose());
        preparedStatement.setInt(6, profile.getDraw());
        preparedStatement.executeUpdate();
    }

    /**
     * Xóa hồ sơ khỏi cơ sở dữ liệu.
     *
     * @param key Tên người dùng của hồ sơ cần xóa.
     * @throws SQLException Nếu có lỗi xảy ra khi thực hiện xóa hồ sơ.
     */
    @Override
    public void delete(String key) throws SQLException {
        String query = "DELETE FROM xo_schema.profile WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, key);
        preparedStatement.executeUpdate();
    }
}
