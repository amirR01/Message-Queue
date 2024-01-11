package MQproject.client.Interface;

public interface CommandLineInterface {

    public void runCommandLineInterface();

    public void stopCommandLineInterface();

    public void printMessage(String message);

    public void printError(String message);

    public void printInfo(String message);

    public void printWarning(String message);

    public String getCommand();

    public void handleCommands(String command);
}
