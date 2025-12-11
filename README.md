# Java Chat Application

A simple multi-client chat application built using Java sockets.  
It includes a server that handles multiple clients and broadcasts messages to everyone connected.

 Project Files

- **ChatServer.java** — Starts the server, listens for clients, and manages message broadcasting.
- **ClientHandler.java** — Handles each connected client in a separate thread.
- **ChatClient.java** — Connects to the server, sends messages, and listens for server responses.

---

How It Works

 Server  
1. Run `ChatServer.java`  
2. It opens a socket on a port  
3. Every new client is assigned a `ClientHandler`  
4. Messages are broadcasted to all connected clients

🔹 Client  
1. Run `ChatClient.java`  
2. Connects to the server’s IP + port  
3. Sends and receives messages in real time

---

 How to Run

1. Compile all files:
```bash
javac *.java
