package poe.part1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Message {

    // Dynamic Lists to store running message data
    private final ArrayList<String> sentMessages = new ArrayList<>();
    private final ArrayList<String> messageIdsList = new ArrayList<>();
    private final HashSet<String> messageIds = new HashSet<>();
    private final ArrayList<String> msgHash = new ArrayList<>();
    private final ArrayList<String> recipientNumber = new ArrayList<>();
    private final ArrayList<String> messageStatus = new ArrayList<>();
    private final ArrayList<String> senders = new ArrayList<>();

    private final Random random = new Random();
    private final Scanner input = new Scanner(System.in);

    private int numMessageSent = 0;

    /**
     * Main method to control the loop for capturing and processing messages.
     */
    public void sendingMessages() {
        System.out.print("How many messages do you want to send? ");
        int numberOfMessages = input.nextInt();
        input.nextLine(); // Clear scanner buffer

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
                i--; // Decrement loop counter to re-try this message index
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

            if (status.equals("Sent")) {
                numMessageSent++;
            } else if (status.equals("Stored")) {
                storeMessage(messageID, recipient, messageText, messageHash, sender);
            }
        }

        printMessages();
        System.out.println("\nTotal messages sent: " + returnTotalMessages());
    }

    /**
     * Rubric Method: Validates that the Message ID does not exceed 10 digits.
     */
    public boolean checkMessageID(String messageID) {
        return messageID != null && messageID.length() <= 10;
    }

    /**
     * Generates a unique 10-digit random Message ID.
     */
    public String generateMessageID() {
        String messageID;
        do {
            long number = 1000000000L + (long) (random.nextDouble() * 9000000000L);
            messageID = String.valueOf(number);
        } while (messageIds.contains(messageID));

        return messageID;
    }

    /**
     * Rubric Method: Validates recipient cellphone numbers.
     */
    public String checkRecipientCell(String cellphone) {
        if (cellphone != null && (cellphone.matches("^\\+27[0-9]{9}$") || cellphone.matches("^0[0-9]{9}$"))) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    /**
     * Rubric Method: Generates the custom Message Hash string in UPPERCASE.
     */
    public String createMessageHash(String messageID, int messageNumber, String messageText) {
        String firstTwoNumbers = messageID.substring(0, 2);
        String cleanMessage = messageText.trim();
        String[] words = cleanMessage.split("\\s+");

        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        String hash = firstTwoNumbers + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    /**
     * Handles user choice to Send, Disregard, or Store the message.
     */
    public String SentMessage() {
        System.out.println("\nChoose what you want to do with this message!");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message");
        System.out.print("Enter your choice: ");

        int choice = input.nextInt();
        input.nextLine(); // Clear buffer

        switch (choice) {
            case 1 -> {
                System.out.println("Message successfully sent");
                return "Sent";
            }
            case 2 -> {
                System.out.print("Press 0 to delete the message: ");
                int deleteChoice = input.nextInt();
                input.nextLine();

                if (deleteChoice == 0) {
                    System.out.println("Message deleted");
                    int lastIndex = sentMessages.size() - 1;

                    String messageID = messageIdsList.get(lastIndex);
                    messageIds.remove(messageID);
                    recipientNumber.remove(lastIndex);
                    sentMessages.remove(lastIndex);
                    msgHash.remove(lastIndex);
                    messageIdsList.remove(lastIndex);
                    senders.remove(lastIndex);

                    return "Disregarded";
                } else {
                    System.out.println("Invalid option!");
                    return "Disregarded";
                }
            }
            case 3 -> {
                System.out.println("Message successfully stored");
                return "Stored";
            }
            default -> {
                System.out.println("Invalid option");
                return "Disregarded";
            }
        }
    }

    /**
     * Stores message details in a JSON formatted file.
     * Reference: Standard Java File I/O for JSON formatting.
     */
    public void storeMessage(String id, String recipient, String text, String hash, String sender) {
        String jsonPayload = String.format(
            "{\n  \"messageID\": \"%s\",\n  \"recipient\": \"%s\",\n  \"message\": \"%s\",\n  \"messageHash\": \"%s\",\n  \"sender\": \"%s\"\n}\n",
            id, recipient, text, hash, sender
        );

        try (FileWriter writer = new FileWriter("messages.json", true)) {
            writer.write(jsonPayload);
        } catch (IOException e) {
            System.out.println("Error storing message to JSON: " + e.getMessage());
        }
    }

    /**
     * Displays all processed messages on the screen.
     */
    public String printMessages() {
        StringBuilder allMessages = new StringBuilder();
        System.out.println("\n===== All Messages =====");

        for (int i = 0; i < sentMessages.size(); i++) {
            int messageNumber = i + 1;

            allMessages.append("\nMessage Number: ").append(messageNumber)
                       .append("\nMessage ID: ").append(messageIdsList.get(i))
                       .append("\nMessage Hash: ").append(msgHash.get(i))
                       .append("\nRecipient: ").append(recipientNumber.get(i))
                       .append("\nMessage: ").append(sentMessages.get(i))
                       .append("\nStatus: ").append(messageStatus.get(i))
                       .append("\n------------------------------");

            System.out.println("\nMessage Number: " + messageNumber);
            System.out.println("Message ID: " + messageIdsList.get(i));
            System.out.println("Message Hash: " + msgHash.get(i));
            System.out.println("Recipient: " + recipientNumber.get(i));
            System.out.println("Message: " + sentMessages.get(i));
            System.out.println("Status: " + messageStatus.get(i));
            System.out.println("------------------------------");
        }

        return allMessages.toString();
    }

    /**
     * Returns total count of sent messages.
     */
    public int returnTotalMessages() {
        return numMessageSent;
    }
}