package MQproject.client.Implementation;

import MQproject.client.Interface.CommandLineInterface;

import java.util.Scanner;

public class CommandLineInterfaceImpl implements CommandLineInterface {

    @Override
    public void runCommandLineInterface() {
        boolean isRunning = true;
        while (isRunning) {
            // Call getCommand() to get the user's input
            String command = getCommand();

            // Call handleCommands() to handle the entered command
            handleCommands(command);

            // Check if the user wants to stop the command-line interface
            if (command.equalsIgnoreCase("stop")) {
                isRunning = false;
            }
        }

    }

    @Override
    public void stopCommandLineInterface() {
        // Implementation for stopping the command-line interface
        // Your code here...
        // For example:
        // Stop the loop that accepts user commands
        // Release any acquired resources
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void printError(String message) {
        System.err.println("[ERROR] " + message);
    }

    @Override
    public void printInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    @Override
    public void printWarning(String message) {
        System.out.println("[WARNING] " + message);
    }

    @Override
    public String getCommand() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a command: ");
        return scanner.nextLine();
    }

    @Override
    public void handleCommands(String command) {
        // Implementation for handling commands entered by the user
        // Your code here...
        // For example:
        // Use a switch statement to handle different commands
        // Perform the appropriate actions based on the entered command
        switch (command) {
            case "start":
                // Start the message broker client
                break;
            case "stop":
                // Stop the message broker client
                break;
            case "subscribe":
                // Subscribe to a server
                break;
            case "unsubscribe":
                // Unsubscribe from a server
                break;
            case "pull":
                // Pull messages from a server
                break;
            case "push":
                // Push messages to a server
                break;
            default:
                // Handle unrecognized commands
                break;
        }
    }
}