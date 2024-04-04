package MQproject.client.Interface;

import org.springframework.stereotype.Service;

import java.util.LinkedList;


public interface CommandLineInterfaceIn {
    public String getCommand();

    public void handleCommands(LinkedList<String> command);

    public void runCommandLineInterface();


    public void stopCommandLineInterface();
}
