package MQproject.client.Implementation;

import MQproject.client.Interface.CommandLineInterfaceOut;
import MQproject.client.model.message.BrokerClientMessage;
import java.util.List;


public class CommandLineInterfaceOutImpl implements CommandLineInterfaceOut {


    @Override
    public void runCommandLineInterface() {

    }

    @Override
    public void stopCommandLineInterface() {
        System.out.println("Stopping command line interface...");
    }

    @Override
    public void printMessage(List<BrokerClientMessage.BrokerClientSmallerMessage> messages) {
        for (BrokerClientMessage.BrokerClientSmallerMessage message : messages) {
            System.out.println(message.data);
        }
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


}