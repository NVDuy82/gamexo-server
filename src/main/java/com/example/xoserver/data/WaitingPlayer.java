package com.example.xoserver.data;

import com.example.xoserver.model.Profile;

import java.util.UUID;

/**
 * Lớp WaitingPlayer đại diện cho một người chơi đang chờ trong hệ thống.
 * Lớp này chứa thông tin về hồ sơ của người chơi, trạng thái đầy phòng, và mã trận đấu.
 */
public class WaitingPlayer {
    private Profile profile; // Hồ sơ của người chơi
    private boolean isFull = false; // Trạng thái phòng đã đầy hay chưa
    private final String idMatch; // Mã định danh của trận đấu
    private String token; // Mã token của người chơi

    /**
     * Khởi tạo một người chơi đang chờ với hồ sơ và mã token.
     *
     * @param profile Hồ sơ của người chơi
     * @param token Mã token của người chơi
     */
    public WaitingPlayer(Profile profile, String token) {
        this.profile = profile;
        this.token = token;

        // Tạo idMatch
        UUID uuid = UUID.randomUUID();
        this.idMatch = uuid.toString();
    }

    /**
     * Lấy hồ sơ của người chơi.
     *
     * @return Hồ sơ của người chơi
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Thiết lập hồ sơ cho người chơi.
     *
     * @param profile Hồ sơ của người chơi
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * Kiểm tra xem phòng đã đầy hay chưa.
     *
     * @return true nếu phòng đã đầy, false nếu chưa đầy
     */
    public boolean isFull() {
        return isFull;
    }

    /**
     * Thiết lập trạng thái đầy cho phòng.
     *
     * @param full true nếu phòng đầy, false nếu không
     */
    public void setFull(boolean full) {
        isFull = full;
    }

    /**
     * Lấy mã định danh của trận đấu.
     *
     * @return Mã định danh của trận đấu
     */
    public String getIdMatch() {
        return idMatch;
    }

    /**
     * Lấy mã token của người chơi.
     *
     * @return Mã token của người chơi
     */
    public String getToken() {
        return token;
    }

    /**
     * Thiết lập mã token cho người chơi.
     *
     * @param token Mã token của người chơi
     */
    public void setToken(String token) {
        this.token = token;
    }
}
