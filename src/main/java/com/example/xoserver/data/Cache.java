package com.example.xoserver.data;

import com.example.xoserver.ClientHandler;
import com.example.xoserver.cache.DataCache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Lớp này quản lý cache cho các token và profile, cùng với việc lưu trữ các client handler.
 */
public class Cache {
    /**
     * Cache lưu trữ token và profile, với thời gian hết hạn là 2 ngày.
     */
    public static DataCache token_profile = new DataCache(2, TimeUnit.DAYS);

    /**
     * Bản đồ lưu trữ các client handler, ánh xạ từ tên người dùng tới client handler tương ứng.
     */
    public static Map<String, ClientHandler> clientHandlerMap = new HashMap<>();
}
