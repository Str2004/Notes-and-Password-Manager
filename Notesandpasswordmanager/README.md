## Secure Notes & Password Manager

This project is a simple, secure, and user-friendly Notes & Password Manager built with Java Swing. It allows users to securely manage notes and passwords with an intuitive graphical interface.

---

## Features

- **Add, View, and Delete Notes:**  
  Easily manage textual notes. Each note is assigned an auto-incremented unique ID for reference.

- **Add, View, and Delete Passwords:**  
  Store, retrieve, and remove passwords for different services. Passwords are encrypted using a simple character shifting technique for demonstration purposes.

- **User-Friendly GUI:**  
  The application uses Java Swing for its graphical user interface, providing easy-to-use buttons and a central display area.

---

## How to Run

1. **Requirements:**
   - Java JDK 8 or above installed
   - No external dependencies required

2. **Compile and Run:**
   ```sh
   javac NotesAndPasswordManager.java
   java NotesAndPasswordManager
   ```

---

## Usage

- **Notes Section:**
  - **Add Note:** Click `Add Note`, enter your note, and press OK.
  - **View All Notes:** Click `View All Notes` to display all notes with their IDs.
  - **Delete Note:** Click `Delete Note`, enter the note ID, and the note will be deleted if found.

- **Password Section:**
  - **Add Password:** Click `Add Password`, enter service name and password, and press OK.
  - **View Password:** Click `View Password`, enter the service name, and the password (decrypted) will be shown if present.
  - **Delete Password:** Click `Delete Password`, enter the service name, and it will be removed if present.

- The center text area displays the current status, notes, and passwords based on your actions.

---

## Screenshots

- Main GUI (Notes Section):  
  ![Screenshot-2025-10-04-012339](attached_image:1)

- Viewing a Password:  
  ![Screenshot-2025-10-04-012501](attached_image:2)

---

## Implementation Details

- **GUI:** Implemented using `JFrame`, `JTextArea`, and `JButton` in the `ManagerGUI` class.
- **Note Management:** Handled by the `NoteManager` class. Notes are stored in a `HashMap<Integer, String>` and uniquely ID'd.
- **Password Management:** Uses the `PasswordManager` class, storing encrypted passwords in a `HashMap<String, String>`.
- **Encryption:** Passwords are encrypted via a basic Caesar cipher (character shift) implemented in the `Crypto` class.
- **No data persistence:** Notes and passwords exist only during program execution.

---

## Developer

- Created by Abhilash Dalai as a simple demonstration for secure note and password management systems using Java.

---

## Disclaimer

This application uses a basic encryption technique (Caesar cipher) and is meant for educational purposes only; it is **not suitable for real-world secret management**.
'''