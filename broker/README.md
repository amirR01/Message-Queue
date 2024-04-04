# Broker 
The broker is responsible for managing the partitions and the clients. The broker is responsible for sending the messages to the consumers and receiving the messages from the producers.

## Implementation Details
The broker is implemented in `java` using `spring-boot` framework. It has 3 controllers for Broker-Broker, Broker-Client, and Broker-Server communication. And also it has 4 services: `BrokerBrokerService`, `BrokerClientService`, `BrokerServerService`, and `DataManager`. Also the artitecture of a interface and a implementation for each service is used to make the development more flexible.

## Data Management
The `DataManager` service is responsible for managing the partitions and the messages. Each partition is stored in a file in the `data` directory. the service is responsible to save the index of last read massage for each partition. The information of the partitions is stored in the `partitions.json` file. The service is responsible for reading and writing the messages to the partitions.

## Broker-Broker Communication
Brokers need to communicate with each other to replicate the partitions and the messages. and also infrom each other about each partition's index of last read message. Also as the consequence of rebalancing the partitions, the brokers should be able to talk to each other and send whole data of the partition to the new broker.

## Broker-Client Communication
Clients need to communicate with the broker to send and receive the messages. The broker should be able to send the messages to the consumers and receive the messages from the producers.

## Broker-Server Communication
The broker should register itself to the server and send its information to the server. The server will assign the partitions to the broker based on the load of the broker. The broker has some APIs to receive the commands from the server to update the partitions.