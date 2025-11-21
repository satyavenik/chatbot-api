package com.chatbot.api.service;

import com.chatbot.api.model.ConversationSession;
import com.chatbot.api.model.Message;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ChatbotService {
    
    private final Map<String, ConversationSession> sessions = new ConcurrentHashMap<>();
    private final ChatClient chatClient;

    public ChatbotService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("You are a helpful AI assistant. Keep responses concise and friendly.")
                .build();
    }

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
        
        // Generate bot response using LLM
        String botResponse = generateLLMResponse(session, userMessage);

        // Store bot message
        Message botMsg = new Message("bot", botResponse);
        session.addMessage(botMsg);
        
        return botResponse;
    }
    
    private String generateLLMResponse(ConversationSession session, String userMessage) {
        try {
            // Build conversation context
            String conversationHistory = session.getMessages().stream()
                    .limit(session.getMessages().size() - 1) // Exclude the current user message we just added
                    .map(msg -> msg.getSender() + ": " + msg.getContent())
                    .collect(Collectors.joining("\n"));

            String prompt = conversationHistory.isEmpty()
                ? userMessage
                : "Previous conversation:\n" + conversationHistory + "\n\nUser: " + userMessage;

            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();
        } catch (Exception e) {
            // Fallback to rule-based response if LLM fails
            return generateFallbackResponse(session, userMessage);
        }
    }

    private String generateFallbackResponse(ConversationSession session, String userMessage) {
        String lowerMessage = userMessage.toLowerCase();
        int messageCount = session.getMessages().size();

        if (messageCount <= 1) {
            return "Hello! I'm your chatbot assistant. How can I help you today?";
        }

        if (lowerMessage.contains("hello") || lowerMessage.contains("hi")) {
            return messageCount > 2 ? "Hello again! What else can I help you with?" : "Hello! How can I assist you?";
        }

        if (lowerMessage.contains("bye") || lowerMessage.contains("goodbye")) {
            return "Goodbye! It was nice talking to you. Feel free to come back anytime!";
        }

        if (lowerMessage.contains("help")) {
            return "I can chat with you and remember our conversation. Try asking me questions or just chat!";
        }

        return "I'm having trouble connecting to my AI service right now. Please try again later or check your configuration.";
    }
    
    public void clearSession(String sessionId) {
        sessions.remove(sessionId);
    }
    
    public int getSessionCount() {
        return sessions.size();
    }
}
