package com.example.xoserver.data;

/**
 * Lớp Move đại diện cho một nước đi trong trò chơi.
 * Lớp này chứa thông tin về mã token của người chơi, số bước đi, vị trí trên bảng cờ và kích thước quân cờ.
 */
public class Move {
    private String token; // Mã token của người chơi thực hiện nước đi
    private int step; // Số bước đi hiện tại
    private int row; // Hàng trên bảng cờ
    private int col; // Cột trên bảng cờ
    private int size; // Kích thước của quân cờ

    /**
     * Khởi tạo một nước đi mới.
     *
     * @param token Mã token của người chơi
     * @param step Số bước đi hiện tại
     * @param row Hàng trên bảng cờ
     * @param col Cột trên bảng cờ
     * @param size Kích thước của quân cờ
     */
    public Move(String token, int step, int row, int col, int size) {
        this.token = token;
        this.step = step;
        this.row = row;
        this.col = col;
        this.size = size;
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

    /**
     * Lấy số bước đi hiện tại.
     *
     * @return Số bước đi hiện tại
     */
    public int getStep() {
        return step;
    }

    /**
     * Thiết lập số bước đi hiện tại.
     *
     * @param step Số bước đi hiện tại
     */
    public void setStep(int step) {
        this.step = step;
    }

    /**
     * Lấy hàng trên bảng cờ.
     *
     * @return Hàng trên bảng cờ
     */
    public int getRow() {
        return row;
    }

    /**
     * Thiết lập hàng trên bảng cờ.
     *
     * @param row Hàng trên bảng cờ
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Lấy cột trên bảng cờ.
     *
     * @return Cột trên bảng cờ
     */
    public int getCol() {
        return col;
    }

    /**
     * Thiết lập cột trên bảng cờ.
     *
     * @param col Cột trên bảng cờ
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Lấy kích thước của quân cờ.
     *
     * @return Kích thước của quân cờ
     */
    public int getSize() {
        return size;
    }

    /**
     * Thiết lập kích thước của quân cờ.
     *
     * @param size Kích thước của quân cờ
     */
    public void setSize(int size) {
        this.size = size;
    }
}
