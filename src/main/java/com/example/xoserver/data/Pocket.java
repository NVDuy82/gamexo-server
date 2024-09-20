package com.example.xoserver.data;

/**
 * Lớp Pocket đại diện cho bộ chứa các quân cờ của người chơi.
 * Lớp này quản lý số lượng các kích thước quân cờ: nhỏ, trung bình và lớn.
 */
public class Pocket {
    private int small; // Số lượng quân cờ nhỏ
    private int medium; // Số lượng quân cờ trung bình
    private int large; // Số lượng quân cờ lớn
    private final int initialSize; // Tổng số quân cờ ban đầu

    /**
     * Khởi tạo một Pocket với số lượng quân cờ mặc định.
     * Ban đầu có 4 quân cờ nhỏ, 2 quân cờ trung bình, và 1 quân cờ lớn.
     */
    public Pocket() {
        this.small = 4;
        this.medium = 2;
        this.large = 1;
        this.initialSize = this.small + this.medium + this.large;
    }

    /**
     * Khởi tạo một Pocket với số lượng quân cờ được chỉ định.
     *
     * @param small Số lượng quân cờ nhỏ
     * @param medium Số lượng quân cờ trung bình
     * @param large Số lượng quân cờ lớn
     */
    public Pocket(int small, int medium, int large) {
        this.small = small;
        this.medium = medium;
        this.large = large;
        this.initialSize = this.small + this.medium + this.large;
    }

    /**
     * Lấy số lượng quân cờ theo kích thước.
     *
     * @param size Kích thước của quân cờ (0: nhỏ, 1: trung bình, 2: lớn)
     * @return Số lượng quân cờ của kích thước tương ứng
     */
    public int getNumberOfSize(int size) {
        if (size == 0) return this.small;
        if (size == 1) return this.medium;
        return large;
    }

    /**
     * Sử dụng một quân cờ theo kích thước.
     *
     * @param size Kích thước của quân cờ (0: nhỏ, 1: trung bình, 2: lớn)
     * @return true nếu việc sử dụng quân cờ thành công, false nếu không đủ quân cờ
     */
    public boolean useSize(int size) {
        if (getNumberOfSize(size) == 0) return false;

        if (size == 0) small--;
        if (size == 1) medium--;
        if (size == 2) large--;
        return true;
    }

    /**
     * Lấy tổng số quân cờ ban đầu.
     *
     * @return Tổng số quân cờ ban đầu
     */
    public int initSize() {
        return this.initialSize;
    }

    /**
     * Lấy tổng số quân cờ hiện có.
     *
     * @return Tổng số quân cờ hiện có
     */
    public int size() {
        return this.small + this.medium + this.large;
    }

    /**
     * Kiểm tra xem bộ chứa có rỗng hay không.
     *
     * @return true nếu không còn quân cờ nào, false nếu còn quân cờ
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }
}
