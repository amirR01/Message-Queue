package MQproject.client.Interface;

import java.util.LinkedList;

public interface CommandLineInterfaceIn {
    public String getCommand();

    public void handleCommands(LinkedList<String> command);

    public void runCommandLineInterface();


    public void stopCommandLineInterface();
}
