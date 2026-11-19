package poe.part1;

import java.util.Scanner;

public class PoEPart1 {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Login register = new Login();

        System.out.println("==== Register ====");
        System.out.print("Please Enter Username: ");
        String username = scan.nextLine();

        System.out.print("Enter Password: ");
        String password = scan.nextLine();

        System.out.print("Enter cellphone number: ");
        String cellphone = scan.nextLine();

        // Check registration requirements
        if (!register.checkUserName(username)) {
            System.out.println("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than 5 characters in length.");
        } else {
            System.out.println("Username successfully captured.");
        }

        if (!register.checkPasswordComplexity(password)) {
            System.out.println("Password is not correctly formatted, please ensure that the password contains at least 8 characters, a capital letter, a number and a special character.");
        } else {
            System.out.println("Password successfully captured.");
        }

        if (!register.checkCellPhoneNumber(cellphone)) {
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        } else {
            System.out.println("Cell phone number successfully added.");
        }

        String registrationMessage = register.registerUser(username, password, cellphone);
        System.out.println(registrationMessage);

        // If registration was successful, proceed to Login
        if (registrationMessage.equals("User registered successfully.")) {
            System.out.println("\n==== Login ====");
            System.out.print("Enter Username: ");
            String loginUsername = scan.nextLine();

            System.out.print("Enter Password: ");
            String loginPassword = scan.nextLine();

            boolean loginSuccess = register.loginUser(loginUsername, loginPassword);
            System.out.println(register.returnLoginStatus(loginSuccess));

            // Part 2 & Part 3 Integration: Launch QuickChat menu if login succeeds
            if (loginSuccess) {
                System.out.println("\n==============================");
                System.out.println("Welcome to QuickChat.");
                System.out.println("==============================");

                Message messageManager = new Message();
                boolean running = true;

                while (running) {
                    System.out.println("\nPlease select an option:");
                    System.out.println("1) Send Messages");
                    System.out.println("2) Show recently sent messages");
                    System.out.println("3) Quit");
                    System.out.println("4) Stored Messages");
                    System.out.print("Enter option (1-4): ");

                    String rawChoice = scan.nextLine().trim();
                    int choice;
                    try {
                        choice = Integer.parseInt(rawChoice);
                    } catch (NumberFormatException e) {
                        System.out.println("\nInvalid option. Please enter 1, 2, 3, or 4.");
                        continue;
                    }

                    switch (choice) {
                        case 1 -> messageManager.sendingMessages();
                        case 2 -> messageManager.printMessages();
                        case 3 -> {
                            System.out.println("\nThank you for using QuickChat. Goodbye!");
                            running = false;
                        }
                        case 4 -> messageManager.storedMessagesMenu();
                        default -> System.out.println("\nInvalid option. Please enter 1, 2, 3, or 4.");
                    }
                }
            }
        }
    }
}
