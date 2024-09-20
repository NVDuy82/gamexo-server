package com.example.xoserver.data;

import com.example.xoserver.cache.DataCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Lớp Match quản lý các trận đấu trong hệ thống.
 * Lớp này chứa các thông tin về người chơi đang chờ, các phòng chơi và thông tin liên quan đến token của người chơi.
 */
public class Match {
    /**
     * Bảng tìm kiếm người chơi đang chờ.
     * Khóa là ID của trận đấu và giá trị là thông tin của người chơi chờ.
     */
    public static Map<Integer, WaitingPlayer> finder = new HashMap<>();

    /**
     * Bảng chứa các phòng chơi.
     * Khóa là mã phòng và giá trị là đối tượng Room đại diện cho phòng đó.
     */
    public static Map<String, Room> room = new HashMap<>();

    /**
     * Bảng ánh xạ giữa mã token của người chơi và ID của phòng chơi.
     * Khóa là mã token, giá trị là ID của phòng tương ứng.
     */
    public static Map<String, String> token_idRoom = new HashMap<>();

    /**
     * Bộ nhớ cache cho token của đối thủ.
     * Dữ liệu sẽ hết hạn sau 30 giây.
     */
    public static DataCache opponentToken = new DataCache(30);  // 30 seconds

    /**
     * Bộ nhớ cache cho thông tin trò chơi.
     * Dữ liệu sẽ hết hạn sau 600 giây (10 phút).
     */
    public static DataCache game = new DataCache(600);  // 600 seconds = 10 minutes
}
