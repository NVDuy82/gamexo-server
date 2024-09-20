package com.example.xoserver.messageprocessor;

import com.example.shared.Message;

/**
 * Lớp Default kế thừa từ MessageProcessor, dùng để xử lý các thông điệp mặc định.
 * Lớp này thực hiện các tác vụ cơ bản mà không cần xử lý cụ thể.
 */
public class Default extends MessageProcessor {

    /**
     * Thực hiện các công việc cần thiết dựa trên nội dung đã giải mã.
     * Trong lớp Default, không thực hiện công việc nào.
     *
     * @param decryptedContent Nội dung đã giải mã
     * @param status Trạng thái hiện tại của người dùng
     */
    @Override
    protected void performTask(String decryptedContent, String status) {
        // Không thực hiện công việc nào trong lớp Default
    }

    /**
     * Tạo và trả về thông điệp phản hồi dựa trên nội dung đã giải mã.
     *
     * @param decryptedContent Nội dung đã giải mã
     * @return Thông điệp phản hồi với mã hành động "action_code"
     */
    @Override
    protected Message generateResponse(String decryptedContent) {
        return new Message("action_code", decryptedContent, "OK");
    }
}
