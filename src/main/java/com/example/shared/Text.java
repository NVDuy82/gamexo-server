package com.example.shared;

/**
 * Lớp Text cung cấp các phương thức để định dạng màu sắc cho chuỗi văn bản
 * bằng cách sử dụng mã màu ANSI.
 */
public class Text {

    /**
     * Định dạng văn bản thành màu đỏ.
     *
     * @param text Chuỗi văn bản cần định dạng
     * @return Chuỗi văn bản được bọc bởi mã màu đỏ ANSI
     */
    public static String red(String text) {
        return colorText(text, "\u001B[31m");
    }

    /**
     * Định dạng văn bản thành màu xanh lá cây.
     *
     * @param text Chuỗi văn bản cần định dạng
     * @return Chuỗi văn bản được bọc bởi mã màu xanh lá cây ANSI
     */
    public static String green(String text) {
        return colorText(text, "\u001B[32m");
    }

    /**
     * Định dạng văn bản thành màu xanh dương.
     *
     * @param text Chuỗi văn bản cần định dạng
     * @return Chuỗi văn bản được bọc bởi mã màu xanh dương ANSI
     */
    public static String blue(String text) {
        return colorText(text, "\u001B[34m");
    }

    /**
     * Định dạng văn bản với màu sắc tùy chỉnh.
     *
     * @param text Chuỗi văn bản cần định dạng
     * @param colorCode Mã màu ANSI để định dạng văn bản
     * @return Chuỗi văn bản được bọc bởi mã màu ANSI tùy chỉnh
     */
    public static String colorText(String text, String colorCode) {
        String resetColorCode = "\u001B[0m"; // Mã ANSI để reset lại màu mặc định
        return colorCode + text + resetColorCode;
    }
}
