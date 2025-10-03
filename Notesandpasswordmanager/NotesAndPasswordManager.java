import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * =================================================================
 * Main Application Class: NotesAndPasswordManager
 * =================================================================
 * This is the new entry point for the application. Its only job is
 * to create and launch the graphical user interface (GUI).
 */
public class NotesAndPasswordManager {
    public static void main(String[] args) {
        // Swing applications should be run on the Event Dispatch Thread (EDT)
        // for thread safety. SwingUtilities.invokeLater handles this.
        SwingUtilities.invokeLater(() -> {
            // Create an instance of our GUI and make it visible.
            ManagerGUI gui = new ManagerGUI();
            gui.setVisible(true);
        });
    }
}

/**
 * =================================================================
 * ManagerGUI Class (The "Front-End")
 * =================================================================
 * This class creates and manages the entire graphical user interface.
 * It extends JFrame, which is the main window of the application.
 */
class ManagerGUI extends JFrame {
    // Back-end logic handlers
    private final NoteManager noteManager;
    private final PasswordManager passwordManager;

    // GUI Components
    private JTextArea displayArea;
    private JButton addNoteButton, viewNotesButton, deleteNoteButton;
    private JButton addPasswordButton, viewPasswordButton, deletePasswordButton;

    public ManagerGUI() {
        // Initialize the back-end managers first
        this.noteManager = new NoteManager();
        this.passwordManager = new PasswordManager();

        // --- Window Setup ---
        setTitle("Secure Notes & Password Manager");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout(10, 10)); // Use BorderLayout for overall structure

        // --- Main Display Area ---
        displayArea = new JTextArea("Welcome! Click a button to get started.");
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        displayArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(displayArea); // Add scrollbars
        add(scrollPane, BorderLayout.CENTER);

        // --- Control Panel for Buttons ---
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 3, 10, 10)); // Grid for buttons
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(controlPanel, BorderLayout.SOUTH);

        // --- Initialize and Add Buttons to the Panel ---
        // Note Buttons
        addNoteButton = new JButton("Add Note");
        viewNotesButton = new JButton("View All Notes");
        deleteNoteButton = new JButton("Delete Note");
        controlPanel.add(addNoteButton);
        controlPanel.add(viewNotesButton);
        controlPanel.add(deleteNoteButton);

        // Password Buttons
        addPasswordButton = new JButton("Add Password");
        viewPasswordButton = new JButton("View Password");
        deletePasswordButton = new JButton("Delete Password");
        controlPanel.add(addPasswordButton);
        controlPanel.add(viewPasswordButton);
        controlPanel.add(deletePasswordButton);

        // --- Add Action Listeners (Event Handlers) for each button ---
        setupActionListeners();
    }

    private void setupActionListeners() {
        // --- Note Actions ---
        addNoteButton.addActionListener(e -> {
            String note = JOptionPane.showInputDialog(this, "Enter your note:");
            if (note != null && !note.trim().isEmpty()) {
                noteManager.addNote(note);
                displayArea.setText("Note added successfully!\n\n" + noteManager.getFormattedNotes());
            }
        });

        viewNotesButton.addActionListener(e -> {
            displayArea.setText(noteManager.getFormattedNotes());
        });

        deleteNoteButton.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Enter the ID of the note to delete:");
            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    if (noteManager.deleteNote(id)) {
                        displayArea.setText("Note deleted successfully.\n\n" + noteManager.getFormattedNotes());
                    } else {
                        JOptionPane.showMessageDialog(this, "Note with ID " + id + " not found.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid ID. Please enter a number.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- Password Actions ---
        addPasswordButton.addActionListener(e -> {
            JTextField serviceField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            Object[] message = {
                    "Service:", serviceField,
                    "Password:", passwordField
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Add New Password", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String service = serviceField.getText();
                String password = new String(passwordField.getPassword());
                if (!service.trim().isEmpty() && !password.trim().isEmpty()) {
                    passwordManager.addPassword(service, password);
                    displayArea.setText("Password for '" + service + "' added successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Service and Password cannot be empty.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        viewPasswordButton.addActionListener(e -> {
            String service = JOptionPane.showInputDialog(this, "Enter service name to view password:", "View Password",
                    JOptionPane.PLAIN_MESSAGE);
            if (service != null && !service.trim().isEmpty()) {
                String password = passwordManager.getPassword(service);
                if (password != null) {
                    displayArea.setText("--- Password for '" + service + "' ---\n" + password);
                } else {
                    JOptionPane.showMessageDialog(this, "Password for '" + service + "' not found.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deletePasswordButton.addActionListener(e -> {
            String service = JOptionPane.showInputDialog(this, "Enter service name to delete:", "Delete Password",
                    JOptionPane.PLAIN_MESSAGE);
            if (service != null && !service.trim().isEmpty()) {
                if (passwordManager.deletePassword(service)) {
                    displayArea.setText("Password for '" + service + "' deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Password for '" + service + "' not found.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

/**
 * =================================================================
 * Crypto Utility Class (Unchanged)
 * =================================================================
 */
class Crypto {
    private static final int SHIFT_KEY = 5;

    public static String encrypt(String plainText) {
        StringBuilder encryptedText = new StringBuilder();
        for (char character : plainText.toCharArray()) {
            encryptedText.append((char) (character + SHIFT_KEY));
        }
        return encryptedText.toString();
    }

    public static String decrypt(String encryptedText) {
        StringBuilder plainText = new StringBuilder();
        for (char character : encryptedText.toCharArray()) {
            plainText.append((char) (character - SHIFT_KEY));
        }
        return plainText.toString();
    }
}

/**
 * =================================================================
 * NoteManager Class (Refactored "Back-End" Logic)
 * =================================================================
 * Now returns data instead of printing to the console.
 */
class NoteManager {
    private final Map<Integer, String> notes = new HashMap<>();
    private final AtomicInteger noteIdCounter = new AtomicInteger(1);

    public void addNote(String noteContent) {
        int newNoteId = noteIdCounter.getAndIncrement();
        notes.put(newNoteId, noteContent);
    }

    public String getFormattedNotes() {
        if (notes.isEmpty()) {
            return "No notes found.";
        }
        // Use Java Streams to format the output nicely.
        return notes.entrySet().stream()
                .map(entry -> "ID: " + entry.getKey() + " | Note: " + entry.getValue())
                .collect(Collectors.joining("\n"));
    }

    public boolean deleteNote(int idToDelete) {
        if (notes.containsKey(idToDelete)) {
            notes.remove(idToDelete);
            return true; // Indicate success
        }
        return false; // Indicate failure
    }
}

/**
 * =================================================================
 * PasswordManager Class (Refactored "Back-End" Logic)
 * =================================================================
 * Now returns data instead of printing to the console.
 */
class PasswordManager {
    private final Map<String, String> passwords = new HashMap<>();

    public void addPassword(String service, String password) {
        String encryptedPassword = Crypto.encrypt(password);
        passwords.put(service.toLowerCase().trim(), encryptedPassword);
    }

    public String getPassword(String service) {
        String encryptedPassword = passwords.get(service.toLowerCase().trim());
        if (encryptedPassword != null) {
            return Crypto.decrypt(encryptedPassword);
        }
        return null; // Return null if not found
    }

    public boolean deletePassword(String service) {
        if (passwords.containsKey(service.toLowerCase().trim())) {
            passwords.remove(service.toLowerCase().trim());
            return true; // Indicate success
        }
        return false; // Indicate failure
    }
}
