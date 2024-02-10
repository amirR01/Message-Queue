package MQproject.client.Implementation;

import MQproject.client.Interface.CommandLineInterfaceIn;
import MQproject.client.Interface.Consumer;
import MQproject.client.Interface.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
@Service
public class CommandLineInterfaceInImpl implements CommandLineInterfaceIn {

    @Autowired
    public Producer producer;

    @Autowired
    public Consumer consumer;

    @Override
    public String getCommand() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @Override
    public void handleCommands(LinkedList<String> command) {
        switch (command.getFirst()) {
            case "produce":
                producer.produceMessage(command.getFirst(), command.get(1));
                break;
            case "pull":
                consumer.pull();
                break;
            case "subscribe":
                consumer.subscribe();
                break;
            default:
                System.out.println("Invalid command");
        }
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

    }
}
