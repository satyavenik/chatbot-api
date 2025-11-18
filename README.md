# chatbot-api

A Spring Boot REST API for a chatbot with conversation memory using simple session-based storage.

## Features

- **Session-based Conversations**: Create unique sessions for different users
- **Conversation Memory**: Stores and retrieves full conversation history per session
- **REST API**: Easy-to-use RESTful endpoints
- **Context-aware Responses**: Bot responses take conversation history into account
- **Session Management**: Create, retrieve, and clear sessions

## Technology Stack

- Java 17
- Spring Boot 3.1.5
- Maven
- In-memory session storage (ConcurrentHashMap)

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Build the Application

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Run Tests

```bash
mvn test
```

## API Endpoints

### 1. Health Check

Check if the API is running and view active sessions count.

```bash
GET /api/chat/health
```

**Response:**
```json
{
  "status": "UP",
  "activeSessions": 0
}
```

### 2. Create Session

Create a new conversation session.

```bash
POST /api/chat/session
```

**Response:**
```json
{
  "sessionId": "f867e1d8-008a-4a9d-a4ea-9b07cc6c695e",
  "message": "Session created successfully"
}
```

### 3. Send Message

Send a message to the chatbot and get a response.

```bash
POST /api/chat/message
Content-Type: application/json

{
  "sessionId": "f867e1d8-008a-4a9d-a4ea-9b07cc6c695e",
  "message": "Hello!"
}
```

**Response:**
```json
{
  "sessionId": "f867e1d8-008a-4a9d-a4ea-9b07cc6c695e",
  "response": "Hello! How can I assist you?",
  "conversationHistory": [
    {
      "sender": "user",
      "content": "Hello!",
      "timestamp": "2025-11-18T00:38:05.059635831"
    },
    {
      "sender": "bot",
      "content": "Hello! How can I assist you?",
      "timestamp": "2025-11-18T00:38:05.05967129"
    }
  ]
}
```

### 4. Get Session History

Retrieve the full conversation history for a session.

```bash
GET /api/chat/session/{sessionId}
```

**Response:**
```json
{
  "sessionId": "f867e1d8-008a-4a9d-a4ea-9b07cc6c695e",
  "messages": [
    {
      "sender": "user",
      "content": "Hello!",
      "timestamp": "2025-11-18T00:38:05.059635831"
    },
    {
      "sender": "bot",
      "content": "Hello! How can I assist you?",
      "timestamp": "2025-11-18T00:38:05.05967129"
    }
  ]
}
```

### 5. Clear Session

Delete a conversation session and its history.

```bash
DELETE /api/chat/session/{sessionId}
```

**Response:**
```json
{
  "message": "Session cleared successfully"
}
```

## Example Usage

```bash
# 1. Create a new session
curl -X POST http://localhost:8080/api/chat/session

# 2. Send messages (replace SESSION_ID with actual session ID from step 1)
curl -X POST http://localhost:8080/api/chat/message \
  -H "Content-Type: application/json" \
  -d '{"sessionId":"SESSION_ID","message":"Hello!"}'

curl -X POST http://localhost:8080/api/chat/message \
  -H "Content-Type: application/json" \
  -d '{"sessionId":"SESSION_ID","message":"What is your name?"}'

curl -X POST http://localhost:8080/api/chat/message \
  -H "Content-Type: application/json" \
  -d '{"sessionId":"SESSION_ID","message":"Can you remember our conversation?"}'

# 3. Get conversation history
curl -X GET http://localhost:8080/api/chat/session/SESSION_ID

# 4. Clear the session
curl -X DELETE http://localhost:8080/api/chat/session/SESSION_ID
```

## Architecture

### Components

1. **Model Classes**
   - `Message`: Represents a single message with sender, content, and timestamp
   - `ConversationSession`: Stores session ID and list of messages
   - `ChatRequest`: Request body for sending messages
   - `ChatResponse`: Response containing bot reply and conversation history

2. **Service Layer**
   - `ChatbotService`: Core business logic for session management and response generation

3. **Controller Layer**
   - `ChatController`: REST endpoints for chatbot interaction

### Conversation Memory

The chatbot maintains conversation memory using an in-memory `ConcurrentHashMap` that maps session IDs to `ConversationSession` objects. Each session stores:
- Unique session ID
- Complete list of messages (both user and bot)
- Timestamps for each message

The bot generates context-aware responses based on:
- Current message content
- Number of messages in the conversation
- Previous conversation patterns

## Limitations

- **In-Memory Storage**: Sessions are stored in memory and will be lost when the application restarts
- **No Persistence**: For production use, consider implementing database persistence
- **Simple Bot Logic**: Uses rule-based responses; can be enhanced with AI/ML models
- **No Authentication**: No user authentication or authorization implemented

## Future Enhancements

- Add database persistence (PostgreSQL, MongoDB)
- Implement user authentication and authorization
- Integrate with AI/ML models for smarter responses
- Add WebSocket support for real-time messaging
- Implement session expiration and cleanup
- Add rate limiting and security features
- Support for multiple languages

## License

MIT
