package MQproject.client.Interface;

import java.util.LinkedList;

public interface CommandLineInterface {

    public void runCommandLineInterface();

    public void stopCommandLineInterface();

    public void printMessage(String message);

    public void printError(String message);

    public void printInfo(String message);

    public void printWarning(String message);

    public String getCommand();

    public void handleCommands(LinkedList<String> command);
}
