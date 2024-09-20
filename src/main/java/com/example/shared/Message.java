package com.example.shared;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.Serializable;

/**
 * Lớp Message đại diện cho một thông điệp chứa mã hành động, nội dung và trạng thái.
 */
public class Message implements Serializable {
    private final String actionCode;
    private final String content;
    private final String status;

    /**
     * Khởi tạo một đối tượng Message với mã hành động, nội dung và trạng thái được cung cấp.
     *
     * @param actionCode Mã hành động (action code) của thông điệp
     * @param content Nội dung của thông điệp, có thể là một chuỗi JSON
     * @param status Trạng thái của thông điệp
     */
    public Message(String actionCode, String content, String status) {
        this.actionCode = actionCode;
        this.content = content;
        this.status = status;
    }

    /**
     * Trả về mã hành động của thông điệp.
     *
     * @return Mã hành động của thông điệp
     */
    public String getActionCode() {
        return actionCode;
    }

    /**
     * Trả về nội dung của thông điệp.
     *
     * @return Nội dung của thông điệp
     */
    public String getContent() {
        return content;
    }

    /**
     * Trả về trạng thái của thông điệp.
     *
     * @return Trạng thái của thông điệp
     */
    public String getStatus() {
        return status;
    }

    /**
     * Chuyển đổi đối tượng Message thành chuỗi có định dạng.
     * Nếu nội dung là một chuỗi JSON, nó sẽ được định dạng đẹp (pretty-printing).
     * Trạng thái sẽ được định dạng màu tùy thuộc vào giá trị của nó (sử dụng phương thức hỗ trợ từ lớp Text).
     *
     * @return Chuỗi mô tả đối tượng Message với các trường code, content, và status
     */
    @Override
    public String toString() {
        String res = "\t" + "code: " + actionCode;
        res += "\n\t" + "content: ";

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement jsonElement = JsonParser.parseString(content);
            String jsonString = gson.toJson(jsonElement);

            res += "\n";
            res += jsonString.replaceAll("(?m)^", "\t\t");
        } catch (Exception e) {
            res += content;
        }
        res += "\n\t" + "status: " + (status.equals("OK") ? Text.green(status) : (status.equals("WRONG") ? Text.red(status) : status));

        return res;
    }
}
