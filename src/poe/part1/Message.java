package poe.part1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Message {

    public static class StoredMessageRecord {
        public final String messageID;
        public final String recipient;
        public final String message;
        public final String messageHash;
        public final String sender;

        public StoredMessageRecord(String messageID, String recipient, String message, String messageHash, String sender) {
            this.messageID = messageID != null ? messageID : "";
            this.recipient = recipient != null ? recipient : "";
            this.message = message != null ? message : "";
            this.messageHash = messageHash != null ? messageHash : "";
            this.sender = sender != null ? sender : "";
        }
    }

    // Dynamic Lists to store running message data
    private final ArrayList<String> sentMessages = new ArrayList<>();
    private final ArrayList<String> messageIdsList = new ArrayList<>();
    private final HashSet<String> messageIds = new HashSet<>();
    private final ArrayList<String> msgHash = new ArrayList<>();
    private final ArrayList<String> recipientNumber = new ArrayList<>();
    private final ArrayList<String> messageStatus = new ArrayList<>();
    private final ArrayList<String> senders = new ArrayList<>();

    // Part 3 collections
    private final ArrayList<String> onlySentMessages = new ArrayList<>();
    private final ArrayList<String> disregardedMessages = new ArrayList<>();
    private final ArrayList<StoredMessageRecord> storedMessagesCache = new ArrayList<>();

    private final Random random = new Random();
    private final Scanner input = new Scanner(System.in);
    private int numMessageSent = 0;

    /**
     * Main method to control the loop for capturing and processing messages.
     */
    public void sendingMessages() {
        System.out.print("How many messages do you want to send? ");
        int numberOfMessages;
        try {
            numberOfMessages = Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered.");
            return;
        }

        for (int i = 1; i <= numberOfMessages; i++) {
            System.out.println("\n==============================");
            System.out.println("MESSAGE " + i);
            System.out.println("==============================");

            String messageID = generateMessageID();

            System.out.print("Enter recipient cell number: ");
            String recipient = input.nextLine();

            String recipientResult = checkRecipientCell(recipient);
            if (!recipientResult.equals("Cell phone number successfully captured.")) {
                System.out.println(recipientResult);
                i--; // Decrement loop counter to retry this message index
                continue;
            }

            System.out.print("Enter your message: ");
            String messageText = input.nextLine();

            if (messageText.length() > 250 || messageText.trim().isEmpty()) {
                System.out.println("Please enter a message of 1 to 250 characters.");
                i--;
                continue;
            }

            int messageNumber = sentMessages.size() + 1;
            String messageHash = createMessageHash(messageID, messageNumber, messageText);

            msgHash.add(messageHash);
            sentMessages.add(messageText);
            messageIdsList.add(messageID);
            messageIds.add(messageID);
            recipientNumber.add(recipient);

            System.out.print("Enter sender name: ");
            String sender = input.nextLine();
            senders.add(sender);

            String status = SentMessage();
            messageStatus.add(status);
            recordPart3Outcome(status, messageID, recipient, messageText, messageHash, sender);

            if (status.equals("Stored")) {
                storeMessage(messageID, recipient, messageText, messageHash, sender);
            }
        }

        printMessages();
        System.out.println("\nTotal messages sent: " + returnTotalMessages());
    }

    /**
     * Validates that the Message ID does not exceed 10 characters.
     */
    public boolean checkMessageID(String messageID) {
        return messageID != null && messageID.length() <= 10;
    }

    /**
     * Generates a unique 10-digit numeric Message ID.
     */
    public String generateMessageID() {
        String id;
        do {
            long number = 1_000_000_000L + (long) (random.nextDouble() * 9_000_000_000L);
            id = String.valueOf(number);
        } while (messageIds.contains(id));
        return id;
    }

    /**
     * Validates the recipient cell number.
     */
    public String checkRecipientCell(String cellphone) {
        if (cellphone != null && (cellphone.matches("^\\+27[0-9]{9}$") || cellphone.matches("^0[0-9]{9}$"))) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code.";
    }

    /**
     * Creates a message hash using first two digits of ID, message number, and first/last words.
     */
    public String createMessageHash(String messageID, int messageNumber, String messageText) {
        String safeId = messageID != null ? messageID.trim() : "00";
        String firstTwo = safeId.length() >= 2 ? safeId.substring(0, 2) : String.format("%-2s", safeId).replace(' ', '0');

        String cleanMessage = messageText != null ? messageText.trim().replaceAll("[^a-zA-Z0-9\\s]", "") : "";
        if (cleanMessage.isEmpty()) {
            return (firstTwo + ":" + messageNumber + ":MSG").toUpperCase();
        }

        String[] words = cleanMessage.split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        return (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    /**
     * Prompts the user to Send (1), Disregard (2), or Store (3) the message.
     */
    public String SentMessage() {
        System.out.println("\nChoose what you want to do with this message:");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        System.out.print("Enter option (1-3): ");

        int action;
        try {
            action = Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            action = 2;
        }

        switch (action) {
            case 1 -> {
                return "Sent";
            }
            case 2 -> {
                System.out.print("Press 0 to delete the message: ");
                String confirm = input.nextLine().trim();
                int lastIndex = sentMessages.size() - 1;
                if ("0".equals(confirm) && lastIndex >= 0) {
                    String removedId = messageIdsList.remove(lastIndex);
                    messageIds.remove(removedId);
                    sentMessages.remove(lastIndex);
                    msgHash.remove(lastIndex);
                    recipientNumber.remove(lastIndex);
                    senders.remove(lastIndex);
                }
                return "Disregarded";
            }
            case 3 -> {
                return "Stored";
            }
            default -> {
                return "Disregarded";
            }
        }
    }

    /**
     * Appends a stored message record to messages.json.
     */
    public void storeMessage(String messageID, String recipient, String messageText, String messageHash, String sender) {
        storeMessage("messages.json", messageID, recipient, messageText, messageHash, sender);
    }

    /**
     * Appends a stored message record to the specified JSON file path.
     */
    public void storeMessage(String filePath, String messageID, String recipient, String messageText, String messageHash, String sender) {
        String jsonObject = "{\n"
                + "  \"messageID\": \"" + escapeJson(messageID) + "\",\n"
                + "  \"sender\": \"" + escapeJson(sender) + "\",\n"
                + "  \"recipient\": \"" + escapeJson(recipient) + "\",\n"
                + "  \"message\": \"" + escapeJson(messageText) + "\",\n"
                + "  \"messageHash\": \"" + escapeJson(messageHash) + "\"\n"
                + "}\n";

        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(jsonObject);
            System.out.println("Message stored in " + filePath + ".");
        } catch (IOException e) {
            System.out.println("Error saving stored message: " + e.getMessage());
        }
    }

    /**
     * Displays recently sent messages from the current session.
     */
    public void printMessages() {
        boolean anySent = false;
        System.out.println("\n===== Recently Sent Messages =====");
        for (int i = 0; i < sentMessages.size(); i++) {
            String status = i < messageStatus.size() ? messageStatus.get(i) : "";
            if ("Sent".equalsIgnoreCase(status)) {
                anySent = true;
                String id = i < messageIdsList.size() ? messageIdsList.get(i) : "";
                String hash = i < msgHash.size() ? msgHash.get(i) : "";
                String rec = i < recipientNumber.size() ? recipientNumber.get(i) : "";
                String snd = i < senders.size() ? senders.get(i) : "";
                System.out.println("Message ID   : " + id);
                System.out.println("Message Hash : " + hash);
                System.out.println("Sender       : " + snd);
                System.out.println("Recipient    : " + rec);
                System.out.println("Message      : " + sentMessages.get(i));
                System.out.println("----------------------------------");
            }
        }
        if (!anySent) {
            System.out.println("No sent messages to display.");
        }
    }

    /**
     * Returns total number of sent messages.
     */
    public int returnTotalMessages() {
        return numMessageSent;
    }

    /**
     * Records a message outcome into dedicated Part 3 lists and updates sent count.
     */
    public void recordPart3Outcome(String outcome, String messageID, String recipient, String text, String hash, String sender) {
        if ("Sent".equalsIgnoreCase(outcome)) {
            onlySentMessages.add(text);
            numMessageSent++;
        } else if ("Disregarded".equalsIgnoreCase(outcome)) {
            disregardedMessages.add(text);
        } else if ("Stored".equalsIgnoreCase(outcome)) {
            storedMessagesCache.add(new StoredMessageRecord(messageID, recipient, text, hash, sender));
        }
    }

    public String[] getSentMessagesArray() {
        return onlySentMessages.toArray(new String[0]);
    }

    public String[] getDisregardedMessagesArray() {
        return disregardedMessages.toArray(new String[0]);
    }

    public String[] getMessageHashesArray() {
        return msgHash.toArray(new String[0]);
    }

    public String[] getMessageIdsArray() {
        return messageIdsList.toArray(new String[0]);
    }

    /**
     * Reads JSON message records from the given file path into an array of StoredMessageRecord objects.
     */
    public StoredMessageRecord[] loadStoredMessagesArray(String filePath) {
        ArrayList<StoredMessageRecord> loaded = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            storedMessagesCache.clear();
            return new StoredMessageRecord[0];
        }

        try (Scanner fileScanner = new Scanner(file)) {
            StringBuilder block = new StringBuilder();
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                block.append(line);
                if (line.endsWith("}")) {
                    StoredMessageRecord parsed = parseJsonObjectBlock(block.toString());
                    if (parsed != null) {
                        loaded.add(parsed);
                    }
                    block.setLength(0);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }

        storedMessagesCache.clear();
        storedMessagesCache.addAll(loaded);
        return loaded.toArray(new StoredMessageRecord[0]);
    }

    private StoredMessageRecord parseJsonObjectBlock(String json) {
        String id = extractJsonField(json, "messageID");
        String recipient = extractJsonField(json, "recipient");
        String msg = extractJsonField(json, "message");
        String hash = extractJsonField(json, "messageHash");
        String sender = extractJsonField(json, "sender");
        if (id == null && recipient == null && msg == null && hash == null && sender == null) {
            return null;
        }
        return new StoredMessageRecord(id, recipient, msg, hash, sender);
    }

    private String extractJsonField(String json, String fieldName) {
        String pattern = "\"" + fieldName + "\"";
        int keyIndex = json.indexOf(pattern);
        if (keyIndex == -1) {
            return null;
        }
        int colonIndex = json.indexOf(":", keyIndex + pattern.length());
        if (colonIndex == -1) {
            return null;
        }
        int firstQuote = json.indexOf("\"", colonIndex + 1);
        if (firstQuote == -1) {
            return null;
        }
        StringBuilder value = new StringBuilder();
        for (int i = firstQuote + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '\\' && i + 1 < json.length()) {
                i++;
                value.append(json.charAt(i));
            } else if (c == '"') {
                break;
            } else {
                value.append(c);
            }
        }
        return value.toString();
    }

    // 2a. Sender and recipient of all stored messages
    public String displayStoredSendersAndRecipients(StoredMessageRecord[] records) {
        if (records == null || records.length == 0) {
            return "No stored messages found.";
        }
        StringBuilder sb = new StringBuilder("\n--- Stored Messages: Senders & Recipients ---\n");
        for (int i = 0; i < records.length; i++) {
            sb.append(i + 1).append(". Sender: ").append(records[i].sender)
              .append(" | Recipient: ").append(records[i].recipient).append("\n");
        }
        return sb.toString().trim();
    }

    // 2b. Longest stored message
    public String displayLongestStoredMessage(StoredMessageRecord[] records) {
        if (records == null || records.length == 0) {
            return "No stored messages found.";
        }
        StoredMessageRecord longest = records[0];
        for (int i = 1; i < records.length; i++) {
            if (records[i].message.length() > longest.message.length()) {
                longest = records[i];
            }
        }
        return "\n--- Longest Stored Message ---\n"
                + "Message ID: " + longest.messageID + "\n"
                + "Sender: " + longest.sender + "\n"
                + "Recipient: " + longest.recipient + "\n"
                + "Length: " + longest.message.length() + " characters\n"
                + "Message: " + longest.message;
    }

    // 2c. Search by Message ID
    public String searchByMessageID(StoredMessageRecord[] records, String targetId) {
        if (records == null || records.length == 0) {
            return "No stored messages found.";
        }
        String cleanId = targetId != null ? targetId.trim() : "";
        for (StoredMessageRecord r : records) {
            if (r.messageID.equalsIgnoreCase(cleanId)) {
                return "Match Found -> Recipient: " + r.recipient + " | Message: " + r.message;
            }
        }
        return "No stored message found with ID: " + cleanId;
    }

    // 2d. Search by Recipient
    public String searchByRecipient(StoredMessageRecord[] records, String targetRecipient) {
        if (records == null || records.length == 0) {
            return "No stored messages found.";
        }
        String cleanRec = targetRecipient != null ? targetRecipient.trim() : "";
        StringBuilder matches = new StringBuilder();
        int count = 0;
        for (StoredMessageRecord r : records) {
            if (r.recipient.equalsIgnoreCase(cleanRec)) {
                count++;
                matches.append(count).append(". [ID: ").append(r.messageID).append("] ")
                       .append(r.message).append("\n");
            }
        }
        if (count == 0) {
            return "No stored messages found for recipient: " + cleanRec;
        }
        return "\n--- Stored Messages for " + cleanRec + " ---\n" + matches.toString().trim();
    }

    // 2e. Delete a message using Message Hash and rewrite the JSON file
    public String deleteMessageByHash(String filePath, String targetHash) {
        StoredMessageRecord[] current = loadStoredMessagesArray(filePath);
        if (current.length == 0) {
            return "No stored messages found to delete.";
        }
        String cleanHash = targetHash != null ? targetHash.trim() : "";
        ArrayList<StoredMessageRecord> remaining = new ArrayList<>();
        boolean deleted = false;

        for (StoredMessageRecord r : current) {
            if (!deleted && r.messageHash.equalsIgnoreCase(cleanHash)) {
                deleted = true;
            } else {
                remaining.add(r);
            }
        }

        if (!deleted) {
            return "No stored message found with hash: " + cleanHash;
        }

        try (FileWriter writer = new FileWriter(filePath, false)) {
            for (StoredMessageRecord r : remaining) {
                writer.write("{\n");
                writer.write("  \"messageID\": \"" + escapeJson(r.messageID) + "\",\n");
                writer.write("  \"sender\": \"" + escapeJson(r.sender) + "\",\n");
                writer.write("  \"recipient\": \"" + escapeJson(r.recipient) + "\",\n");
                writer.write("  \"message\": \"" + escapeJson(r.message) + "\",\n");
                writer.write("  \"messageHash\": \"" + escapeJson(r.messageHash) + "\"\n");
                writer.write("}\n");
            }
        } catch (Exception e) {
            return "Error updating JSON file: " + e.getMessage();
        }

        loadStoredMessagesArray(filePath);
        return "Message with hash " + cleanHash + " deleted successfully.";
    }

    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // 2f. Full report of all stored messages
    public String displayStoredMessagesReport(StoredMessageRecord[] records) {
        if (records == null || records.length == 0) {
            return "No stored messages available for report.";
        }
        StringBuilder report = new StringBuilder("\n================ STORED MESSAGES REPORT ================\n");
        for (int i = 0; i < records.length; i++) {
            StoredMessageRecord r = records[i];
            report.append("Message ").append(i + 1).append(":\n")
                  .append("  Message ID   : ").append(r.messageID).append("\n")
                  .append("  Message Hash : ").append(r.messageHash).append("\n")
                  .append("  Sender       : ").append(r.sender).append("\n")
                  .append("  Recipient    : ").append(r.recipient).append("\n")
                  .append("  Message      : ").append(r.message).append("\n")
                  .append("--------------------------------------------------------\n");
        }
        return report.toString().trim();
    }

    public void storedMessagesMenu() {
        boolean inSubMenu = true;
        while (inSubMenu) {
            StoredMessageRecord[] storedArray = loadStoredMessagesArray("messages.json");
            System.out.println("\n==== Stored Messages Menu ====");
            System.out.println("1. Display sender and recipient of all stored messages");
            System.out.println("2. Display the longest stored message");
            System.out.println("3. Search by Message ID");
            System.out.println("4. Search by Recipient");
            System.out.println("5. Delete a message using Message Hash");
            System.out.println("6. Display full report of all stored messages");
            System.out.println("7. Back to QuickChat Menu");
            System.out.print("Enter option (1-7): ");

            String raw = input.nextLine().trim();
            int subChoice;
            try {
                subChoice = Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please enter a number from 1 to 7.");
                continue;
            }

            switch (subChoice) {
                case 1 -> System.out.println(displayStoredSendersAndRecipients(storedArray));
                case 2 -> System.out.println(displayLongestStoredMessage(storedArray));
                case 3 -> {
                    System.out.print("Enter Message ID to search: ");
                    String id = input.nextLine();
                    System.out.println(searchByMessageID(storedArray, id));
                }
                case 4 -> {
                    System.out.print("Enter Recipient number to search: ");
                    String rec = input.nextLine();
                    System.out.println(searchByRecipient(storedArray, rec));
                }
                case 5 -> {
                    System.out.print("Enter Message Hash to delete: ");
                    String hash = input.nextLine();
                    System.out.println(deleteMessageByHash("messages.json", hash));
                }
                case 6 -> System.out.println(displayStoredMessagesReport(storedArray));
                case 7 -> inSubMenu = false;
                default -> System.out.println("Invalid option. Please enter 1-7.");
            }
        }
    }
}