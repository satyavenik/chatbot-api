package com.chatbot.api.service;

import com.chatbot.api.model.ConversationSession;
import com.chatbot.api.model.Message;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatbotService {
    
    private final Map<String, ConversationSession> sessions = new ConcurrentHashMap<>();
    
    public String createSession() {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, new ConversationSession(sessionId));
        return sessionId;
    }
    
    public ConversationSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }
    
    public String processMessage(String sessionId, String userMessage) {
        ConversationSession session = sessions.computeIfAbsent(
            sessionId, 
            id -> new ConversationSession(id)
        );
        
        // Store user message
        Message userMsg = new Message("user", userMessage);
        session.addMessage(userMsg);
        
        // Generate bot response based on conversation context
        String botResponse = generateResponse(session, userMessage);
        
        // Store bot message
        Message botMsg = new Message("bot", botResponse);
        session.addMessage(botMsg);
        
        return botResponse;
    }
    
    private String generateResponse(ConversationSession session, String userMessage) {
        String lowerMessage = userMessage.toLowerCase();
        int messageCount = session.getMessages().size();
        
        // Simple rule-based responses with context awareness
        if (messageCount == 0) {
            return "Hello! I'm your chatbot assistant. How can I help you today?";
        }
        
        if (lowerMessage.contains("hello") || lowerMessage.contains("hi")) {
            if (messageCount > 2) {
                return "Hello again! What else can I help you with?";
            }
            return "Hello! How can I assist you?";
        }
        
        if (lowerMessage.contains("bye") || lowerMessage.contains("goodbye")) {
            return "Goodbye! It was nice talking to you. Feel free to come back anytime!";
        }
        
        if (lowerMessage.contains("help")) {
            return "I can chat with you and remember our conversation. Try asking me questions or just chat!";
        }
        
        if (lowerMessage.contains("weather")) {
            return "I don't have access to real-time weather data, but I hope it's nice where you are!";
        }
        
        if (lowerMessage.contains("name")) {
            return "I'm a simple chatbot created to demonstrate conversation memory. You can call me ChatBot!";
        }
        
        if (lowerMessage.contains("remember") || lowerMessage.contains("history")) {
            return String.format("Yes, I remember! We've exchanged %d messages in this conversation so far.", messageCount);
        }
        
        // Default response with context
        String[] responses = {
            "That's interesting! Tell me more.",
            "I understand. What else would you like to discuss?",
            "Thanks for sharing that with me!",
            "I'm listening. Please continue.",
            "That's a good point. What do you think about it?"
        };
        
        return responses[messageCount % responses.length];
    }
    
    public void clearSession(String sessionId) {
        sessions.remove(sessionId);
    }
    
    public int getSessionCount() {
        return sessions.size();
    }
}
