package com.example.xoserver.model;

/**
 * Lớp Profile đại diện cho hồ sơ của một người chơi trong hệ thống.
 * Lớp này lưu trữ thông tin về tên người dùng, tên thật, điểm số và thống kê trận đấu.
 */
public class Profile {
    private String username; // Tên người dùng
    private String name; // Tên thật của người chơi
    private int score; // Điểm số của người chơi
    private int matches; // Số trận đã chơi
    private int win; // Số trận thắng
    private int lose; // Số trận thua
    private int draw; // Số trận hòa

    /**
     * Khởi tạo một hồ sơ người chơi mới với các thông tin cần thiết.
     *
     * @param username Tên người dùng
     * @param name Tên thật của người chơi
     * @param score Điểm số
     * @param matches Số trận đã chơi
     * @param win Số trận thắng
     * @param lose Số trận thua
     * @param draw Số trận hòa
     */
    public Profile(String username, String name, int score, int matches, int win, int lose, int draw) {
        this.username = username;
        this.name = name;
        this.score = score;
        this.matches = matches;
        this.win = win;
        this.lose = lose;
        this.draw = draw;
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
     * Lấy tên thật của người chơi.
     *
     * @return Tên thật
     */
    public String getName() {
        return name;
    }

    /**
     * Thiết lập tên thật của người chơi.
     *
     * @param name Tên thật
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Lấy điểm số của người chơi.
     *
     * @return Điểm số
     */
    public int getScore() {
        return score;
    }

    /**
     * Thiết lập điểm số cho người chơi.
     *
     * @param score Điểm số
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Lấy số trận đã chơi.
     *
     * @return Số trận đã chơi
     */
    public int getMatches() {
        return matches;
    }

    /**
     * Thiết lập số trận đã chơi.
     *
     * @param matches Số trận đã chơi
     */
    public void setMatches(int matches) {
        this.matches = matches;
    }

    /**
     * Lấy số trận thắng.
     *
     * @return Số trận thắng
     */
    public int getWin() {
        return win;
    }

    /**
     * Thiết lập số trận thắng.
     *
     * @param win Số trận thắng
     */
    public void setWin(int win) {
        this.win = win;
    }

    /**
     * Lấy số trận thua.
     *
     * @return Số trận thua
     */
    public int getLose() {
        return lose;
    }

    /**
     * Thiết lập số trận thua.
     *
     * @param lose Số trận thua
     */
    public void setLose(int lose) {
        this.lose = lose;
    }

    /**
     * Lấy số trận hòa.
     *
     * @return Số trận hòa
     */
    public int getDraw() {
        return draw;
    }

    /**
     * Thiết lập số trận hòa.
     *
     * @param draw Số trận hòa
     */
    public void setDraw(int draw) {
        this.draw = draw;
    }
}
