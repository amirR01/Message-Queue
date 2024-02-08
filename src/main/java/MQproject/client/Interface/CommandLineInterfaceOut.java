package MQproject.client.Interface;

import MQproject.client.model.message.BrokerClientMessage;

import java.util.LinkedList;
import java.util.List;

public interface CommandLineInterfaceOut {

    public void runCommandLineInterface();

    public void stopCommandLineInterface();

    public void printMessage(List<BrokerClientMessage.BrokerClientSmallerMessage> messages);

    public void printError(String message);

    public void printInfo(String message);

    public void printWarning(String message);
}
