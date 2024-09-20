package com.example.xoserver.data;

import com.example.xoserver.model.Profile;

/**
 * Lớp Room đại diện cho một phòng chơi trong hệ thống.
 * Lớp này quản lý thông tin về người chơi, mật khẩu phòng, và trạng thái phòng chơi.
 */
public class Room {
    private String password; // Mật khẩu của phòng chơi
    private String tokenA; // Mã token của người chơi A
    private String tokenB; // Mã token của người chơi B
    private Profile profileA; // Hồ sơ của người chơi A
    private Profile profileB; // Hồ sơ của người chơi B

    /**
     * Khởi tạo một phòng chơi mới với mật khẩu và token của người chơi A.
     *
     * @param password Mật khẩu của phòng
     * @param tokenA Mã token của người chơi A
     */
    public Room(String password, String tokenA) {
        this.password = password;
        this.setTokenA(tokenA);
    }

    /**
     * Kiểm tra xem phòng chơi có đầy đủ hai người chơi hay không.
     *
     * @return true nếu phòng đầy, false nếu chưa đầy
     */
    public boolean isFull() {
        return tokenA != null && tokenB != null;
    }

    /**
     * Kiểm tra mật khẩu của phòng chơi.
     *
     * @param password Mật khẩu cần kiểm tra
     * @return true nếu mật khẩu đúng, false nếu sai
     */
    public boolean idCorrectPassword(String password) {
        return password.equals(this.password);
    }

    /**
     * Xóa một người chơi ra khỏi phòng.
     *
     * @param token Mã token của người chơi cần xóa
     */
    public void remove(String token) {
        if (token.equals(tokenA)) {
            String idRoom = Match.token_idRoom.get(tokenA);
            Match.token_idRoom.remove(tokenA);
            Match.token_idRoom.remove(tokenB);
            Match.room.remove(idRoom);
        } else {
            Match.token_idRoom.remove(tokenB);
            tokenB = null;
            profileB = null;
        }
    }

    /**
     * Lấy mã token của đối thủ dựa trên mã token của người chơi hiện tại.
     *
     * @param token Mã token của người chơi
     * @return Mã token của đối thủ hoặc null nếu không tìm thấy
     */
    public String getOpponentToken(String token) {
        if (token.equals(tokenA)) return tokenB;
        if (token.equals(tokenB)) return tokenA;
        return null;
    }

    /**
     * Lấy mã token của người chơi A.
     *
     * @return Mã token của người chơi A
     */
    public String getTokenA() {
        return tokenA;
    }

    /**
     * Thiết lập mã token cho người chơi A và cập nhật hồ sơ của họ.
     *
     * @param tokenA Mã token của người chơi A
     */
    public void setTokenA(String tokenA) {
        this.tokenA = tokenA;

        Object object = Cache.token_profile.retrieveData(tokenA);
        if (object == null) return;
        profileA = (Profile) object;
    }

    /**
     * Lấy mã token của người chơi B.
     *
     * @return Mã token của người chơi B
     */
    public String getTokenB() {
        return tokenB;
    }

    /**
     * Thiết lập mã token cho người chơi B và cập nhật hồ sơ của họ.
     *
     * @param tokenB Mã token của người chơi B
     */
    public void setTokenB(String tokenB) {
        this.tokenB = tokenB;

        Object object = Cache.token_profile.retrieveData(tokenB);
        if (object == null) return;
        profileB = (Profile) object;
    }

    /**
     * Lấy hồ sơ của người chơi A.
     *
     * @return Hồ sơ của người chơi A
     */
    public Profile getProfileA() {
        return profileA;
    }

    /**
     * Lấy hồ sơ của người chơi B.
     *
     * @return Hồ sơ của người chơi B
     */
    public Profile getProfileB() {
        return profileB;
    }
}
