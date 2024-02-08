package MQproject.client.Implementation;

import MQproject.client.Interface.CommandLineInterface;
import MQproject.client.model.message.BrokerClientMessage;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;


public class CommandLineInterfaceImpl implements CommandLineInterface {
    private final ProducerImpl producer;
    private final ConsumerImpl consumer;

    public static NetworkHandlerImpl networkHandler;

    public CommandLineInterfaceImpl(ProducerImpl producer, ConsumerImpl consumer) {
        this.producer = producer;
        this.consumer = consumer;
        this.networkHandler = new NetworkHandlerImpl();
    }

    @Override
    public void runCommandLineInterface() {
        Scanner scanner = new Scanner(System.in);
        String command = "";
        while (!command.equals("exit")) {
            command = scanner.nextLine();
            LinkedList<String> command_args;
            command_args = new LinkedList<>(Arrays.asList(command.split(" ")));
            handleCommands(command_args);
        }
    }

    @Override
    public void stopCommandLineInterface() {
        System.out.println("Stopping command line interface...");
    }

    @Override
    public void printMessage(String message) {
        System.out.println("Message: " + message);
    }

    @Override
    public void printError(String message) {
        System.err.println("Error: " + message);
    }

    @Override
    public void printInfo(String message) {
        System.out.println("Info: " + message);
    }

    @Override
    public void printWarning(String message) {
        System.out.println("Warning: " + message);
    }

    @Override
    public String getCommand() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @Override
    public void handleCommands(LinkedList<String> command) {
        switch (command.getFirst()) {
            case "produce":
                producer.produceMessage(new BrokerClientMessage());
                break;
            case "consume":
                consumer.consumeMessage();
                break;
            default:
                printError("Invalid command");
        }
    }
}