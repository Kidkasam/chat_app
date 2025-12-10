import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler extends Thread {
    private Socket socket;
    private Set<PrintWriter> writers;
    private PrintWriter out;

    public ClientHandler(Socket socket, Set<PrintWriter> writers) {
        this.socket = socket;
        this.writers = writers;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            out = new PrintWriter(socket.getOutputStream(), true);

            writers.add(out);
            System.out.println("Client connected: " + socket.getInetAddress());

            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Received: " + message);
                broadcast(message);
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            if (out != null) {
                writers.remove(out);
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client disconnected");
        }
    }

    private void broadcast(String message) {
        synchronized (writers) {
            for (PrintWriter writer : writers) {
                writer.println(message);
            }
        }
    }
}
