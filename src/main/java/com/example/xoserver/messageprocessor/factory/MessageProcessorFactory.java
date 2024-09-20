package com.example.xoserver.messageprocessor.factory;

import com.example.xoserver.messageprocessor.Default;
import com.example.xoserver.messageprocessor.MessageProcessor;
import com.example.xoserver.messageprocessor.log.ForgotPass;
import com.example.xoserver.messageprocessor.log.Login;
import com.example.xoserver.messageprocessor.log.Signup;
import com.example.xoserver.messageprocessor.match.CancelFinding;
import com.example.xoserver.messageprocessor.match.FindMatch;
import com.example.xoserver.messageprocessor.match.Resign;
import com.example.xoserver.messageprocessor.match.MoveProcessor;
import com.example.xoserver.messageprocessor.room.CreateRoom;
import com.example.xoserver.messageprocessor.room.ExitRoom;
import com.example.xoserver.messageprocessor.room.JoinRoom;
import com.example.xoserver.messageprocessor.room.StartGame;

/**
 * Lớp MessageProcessorFactory chịu trách nhiệm tạo ra các đối tượng
 * MessageProcessor tương ứng với các mã hành động (action code) khác nhau.
 * Lớp này sử dụng phương thức static để trả về một thể hiện của
 * MessageProcessor dựa trên mã hành động đã cung cấp.
 */
public class MessageProcessorFactory {

    /**
     * Tạo ra một đối tượng MessageProcessor dựa trên mã hành động.
     *
     * @param actionCode Mã hành động xác định loại thông điệp cần xử lý.
     * @return Một đối tượng MessageProcessor tương ứng với mã hành động.
     */
    public static MessageProcessor createDecryption(String actionCode) {
        return switch (actionCode) {
            case "login" -> new Login();
            case "signup" -> new Signup();
            case "forgot_pass" -> new ForgotPass();
            case "create_room" -> new CreateRoom();
            case "join_room" -> new JoinRoom();
            case "start_game" -> new StartGame();
            case "exit_room" -> new ExitRoom();
            case "find_match" -> new FindMatch();
            case "cancel_finding" -> new CancelFinding();
            case "move" -> new MoveProcessor();
            case "resign" -> new Resign();
            default -> new Default();
        };
    }
}
