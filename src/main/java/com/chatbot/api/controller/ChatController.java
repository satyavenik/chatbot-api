package com.chatbot.api.controller;

import com.chatbot.api.model.ChatRequest;
import com.chatbot.api.model.ChatResponse;
import com.chatbot.api.model.ConversationSession;
import com.chatbot.api.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    @Autowired
    private ChatbotService chatbotService;
    
    @PostMapping("/session")
    public ResponseEntity<Map<String, String>> createSession() {
        String sessionId = chatbotService.createSession();
        Map<String, String> response = new HashMap<>();
        response.put("sessionId", sessionId);
        response.put("message", "Session created successfully");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/message")
    public ResponseEntity<ChatResponse> sendMessage(@RequestBody ChatRequest request) {
        if (request.getSessionId() == null || request.getSessionId().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        if (request.getMessage() == null || request.getMessage().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        String botResponse = chatbotService.processMessage(
            request.getSessionId(), 
            request.getMessage()
        );
        
        ConversationSession session = chatbotService.getSession(request.getSessionId());
        
        if (session == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        ChatResponse response = new ChatResponse(
            request.getSessionId(),
            botResponse,
            session.getMessages()
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ConversationSession> getSession(@PathVariable String sessionId) {
        ConversationSession session = chatbotService.getSession(sessionId);
        
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(session);
    }
    
    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Map<String, String>> clearSession(@PathVariable String sessionId) {
        chatbotService.clearSession(sessionId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Session cleared successfully");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("activeSessions", chatbotService.getSessionCount());
        return ResponseEntity.ok(response);
    }
}
