import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient {
    private String serverAddress = "127.0.0.1";
    private int serverPort = 12345;

    private JFrame frame;
    private JTextArea messageArea;
    private JTextField textField;
    private JButton sendButton;
    private PrintWriter out;

    public ChatClient() {
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Java Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.BLACK);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageArea.setBackground(new Color(30, 30, 30));
        messageArea.setForeground(Color.WHITE);
        messageArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        getVerticalScrollBar(scrollPane).setBackground(Color.BLACK);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(Color.BLACK);

        textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField.setBackground(new Color(40, 40, 40));
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(new Color(0, 120, 215));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setPreferredSize(new Dimension(80, 40));
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ActionListener sendAction = e -> sendMessage();
        textField.addActionListener(sendAction);
        sendButton.addActionListener(sendAction);

        inputPanel.add(textField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
    }

    private JScrollBar getVerticalScrollBar(JScrollPane scrollPane) {
        return scrollPane.getVerticalScrollBar();
    }

    private void show() {
        frame.setVisible(true);
        connectToServer();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        String finalMessage = message;
                        SwingUtilities.invokeLater(() -> {
                            messageArea.append(finalMessage + "\n");
                            messageArea.setCaretPosition(messageArea.getDocument().getLength());
                        });
                    }
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> messageArea.append("Connection to server lost.\n"));
                }
            }).start();

        } catch (IOException e) {
            messageArea.append("Could not connect to server at " + serverAddress + ":" + serverPort + "\n");
        }
    }

    private void sendMessage() {
        String message = textField.getText();
        if (!message.trim().isEmpty() && out != null) {
            out.println(message);
            textField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatClient().show();
        });
    }
}
