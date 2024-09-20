package com.example.xoserver.messageprocessor.match;

import com.example.shared.Message;
import com.example.xoserver.messageprocessor.MessageProcessor;

public class Wait extends MessageProcessor {
    @Override
    protected void performTask(String decryptedContent, String status) {
    
    }
    
    @Override
    protected Message generateResponse(String decryptedContent) {
        return null;
    }
}
