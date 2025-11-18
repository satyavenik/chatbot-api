package com.chatbot.api.model;

import java.util.List;

public class ChatResponse {
    private String sessionId;
    private String response;
    private List<Message> conversationHistory;
    
    public ChatResponse() {
    }
    
    public ChatResponse(String sessionId, String response, List<Message> conversationHistory) {
        this.sessionId = sessionId;
        this.response = response;
        this.conversationHistory = conversationHistory;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    public List<Message> getConversationHistory() {
        return conversationHistory;
    }
    
    public void setConversationHistory(List<Message> conversationHistory) {
        this.conversationHistory = conversationHistory;
    }
}
