package com.example.xoserver.messageprocessor.log;

import com.example.shared.Message;
import com.example.xoserver.messageprocessor.MessageProcessor;

public class ForgotPass extends MessageProcessor {
    @Override
    protected void performTask(String decryptedContent, String status) {
    
    }
    
    @Override
    protected Message generateResponse(String decryptedContent) {
        return null;
    }
}
