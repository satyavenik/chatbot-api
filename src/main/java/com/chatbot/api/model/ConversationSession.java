package com.chatbot.api.model;

import java.util.ArrayList;
import java.util.List;

public class ConversationSession {
    private String sessionId;
    private List<Message> messages;
    
    public ConversationSession() {
        this.messages = new ArrayList<>();
    }
    
    public ConversationSession(String sessionId) {
        this.sessionId = sessionId;
        this.messages = new ArrayList<>();
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public List<Message> getMessages() {
        return messages;
    }
    
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    
    public void addMessage(Message message) {
        this.messages.add(message);
    }
}
