package com.example.xoserver.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface này định nghĩa các thao tác cơ bản để truy cập dữ liệu.
 *
 * @param <K> Kiểu dữ liệu cho khóa (key).
 * @param <T> Kiểu dữ liệu cho đối tượng (entity).
 */
public interface DataAccessObject<K, T> {

    /**
     * Lấy tất cả các đối tượng từ cơ sở dữ liệu.
     *
     * @return Danh sách tất cả các đối tượng.
     * @throws SQLException Nếu có lỗi xảy ra khi truy vấn cơ sở dữ liệu.
     */
    List<T> getAll() throws SQLException;

    /**
     * Lấy đối tượng theo khóa.
     *
     * @param key Khóa để tìm đối tượng.
     * @return Đối tượng tương ứng hoặc null nếu không tìm thấy.
     * @throws SQLException Nếu có lỗi xảy ra khi truy vấn cơ sở dữ liệu.
     */
    T get(K key) throws SQLException;

    /**
     * Thêm một đối tượng mới vào cơ sở dữ liệu.
     *
     * @param t Đối tượng cần thêm.
     * @throws SQLException Nếu có lỗi xảy ra khi thực hiện thêm đối tượng.
     */
    void add(T t) throws SQLException;

    /**
     * Cập nhật thông tin của đối tượng trong cơ sở dữ liệu.
     *
     * @param t Đối tượng cần cập nhật.
     * @throws SQLException Nếu có lỗi xảy ra khi thực hiện cập nhật đối tượng.
     */
    void update(T t) throws SQLException;

    /**
     * Xóa đối tượng khỏi cơ sở dữ liệu.
     *
     * @param key Khóa của đối tượng cần xóa.
     * @throws SQLException Nếu có lỗi xảy ra khi thực hiện xóa đối tượng.
     */
    void delete(K key) throws SQLException;
}
