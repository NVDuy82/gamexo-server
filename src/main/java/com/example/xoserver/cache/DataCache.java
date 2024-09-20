package com.example.xoserver.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Lớp này cung cấp một cache dữ liệu sử dụng Guava Cache.
 * Cache tự động hết hạn sau một khoảng thời gian nhất định.
 */
public class DataCache {
    private final Cache<String, Object> cache;

    /**
     * Khởi tạo một cache với thời gian hết hạn mặc định.
     *
     * @param time Thời gian hết hạn của cache (đơn vị: giây).
     */
    public DataCache(long time) {
        cache = CacheBuilder.newBuilder().expireAfterWrite(time, TimeUnit.SECONDS).build();
    }

    /**
     * Khởi tạo một cache với thời gian hết hạn tùy chỉnh.
     *
     * @param time      Thời gian hết hạn của cache.
     * @param timeUnit  Đơn vị thời gian cho thời gian hết hạn.
     */
    public DataCache(long time, TimeUnit timeUnit) {
        cache = CacheBuilder.newBuilder().expireAfterWrite(time, timeUnit).build();
    }

    /**
     * Lưu dữ liệu vào cache với một key nhất định.
     *
     * @param key   Khóa để lưu dữ liệu.
     * @param data  Dữ liệu cần lưu.
     */
    public void storeData(String key, Object data) {
        cache.put(key, data);
    }

    /**
     * Lấy dữ liệu từ cache dựa trên key.
     *
     * @param key Khóa để truy xuất dữ liệu.
     * @return Dữ liệu nếu tồn tại, null nếu không có.
     */
    public Object retrieveData(String key) {
        return cache.getIfPresent(key);
    }

    /**
     * Xóa dữ liệu khỏi cache dựa trên key.
     *
     * @param key Khóa của dữ liệu cần xóa.
     */
    public void removeData(String key) {
        cache.invalidate(key);
    }

    /**
     * Kiểm tra xem cache có chứa key hay không.
     *
     * @param key Khóa cần kiểm tra.
     * @return true nếu cache chứa key, false nếu không.
     */
    public boolean containsKey(String key) {
        return cache.asMap().containsKey(key);
    }
}
