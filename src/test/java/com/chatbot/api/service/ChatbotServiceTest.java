package com.chatbot.api.service;

import com.chatbot.api.model.ConversationSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatbotServiceTest {

    private ChatbotService chatbotService;

    @BeforeEach
    void setUp() {
        chatbotService = new ChatbotService();
    }

    @Test
    void testCreateSession() {
        String sessionId = chatbotService.createSession();
        assertNotNull(sessionId);
        assertNotNull(chatbotService.getSession(sessionId));
    }

    @Test
    void testProcessMessage() {
        String sessionId = chatbotService.createSession();
        String response = chatbotService.processMessage(sessionId, "Hello");
        
        assertNotNull(response);
        assertFalse(response.isEmpty());
        
        ConversationSession session = chatbotService.getSession(sessionId);
        assertEquals(2, session.getMessages().size()); // User message + bot response
    }

    @Test
    void testConversationMemory() {
        String sessionId = chatbotService.createSession();
        
        chatbotService.processMessage(sessionId, "Hello");
        chatbotService.processMessage(sessionId, "How are you?");
        
        ConversationSession session = chatbotService.getSession(sessionId);
        assertEquals(4, session.getMessages().size()); // 2 user messages + 2 bot responses
    }

    @Test
    void testClearSession() {
        String sessionId = chatbotService.createSession();
        chatbotService.processMessage(sessionId, "Hello");
        
        chatbotService.clearSession(sessionId);
        assertNull(chatbotService.getSession(sessionId));
    }

    @Test
    void testMultipleSessions() {
        String sessionId1 = chatbotService.createSession();
        String sessionId2 = chatbotService.createSession();
        
        chatbotService.processMessage(sessionId1, "Hello from session 1");
        chatbotService.processMessage(sessionId2, "Hello from session 2");
        
        ConversationSession session1 = chatbotService.getSession(sessionId1);
        ConversationSession session2 = chatbotService.getSession(sessionId2);
        
        assertNotEquals(session1.getSessionId(), session2.getSessionId());
        assertEquals(2, session1.getMessages().size());
        assertEquals(2, session2.getMessages().size());
    }
}
