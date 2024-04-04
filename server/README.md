# Sever
The server is responsible for managing the brokers and the clients. The server is responsible for rebalancing the partitions between the brokers and the clients.

## Implementation Details
The server is implemented in `java` using `spring-boot` framework. It has 3 controllers for Server-Broker, Server-Consumer, and Server-Producer communication. And also it has 3 services for the Main Service, `BrokerLoadBalancer`, and `ConsumerLoadBalancer`. Also the artitecture of a interface and a implementation for each service is used to make the development more flexible.

## Server-Broker Communication
Eech broker should register itself to the server and send its information to the server. After that each broker should send a heartbeat to the server to show that it is alive. The server checks the heartbeats and if a broker does not send a heartbeat for a specific time, the server will consider the broker as failed and will call `balanceOnBrokerDeath` method of the `BrokerLoadBalancer` service to rebalance the partitions between the available brokers.
by the changes in the partitions, the server will send the needed actions to the brokers to update their partitions.

## Server-Consumer Communication
Each consumer should register itself to the server and send its information to the server. The server will assign the partitions to the consumers based on the load of the consumers.

## Server-Producer Communication
Each producer should register itself to the server and send its information to the server. By sending the key of the message, the server will assign the partition to the producer based on the key.

## Load Balancing
The server uses `BrokerLoadBalancer` and `ConsumerLoadBalancer` services to balance the load between the brokers and the consumers. The `BrokerLoadBalancer` service is responsible for balancing the partitions between the brokers. The `ConsumerLoadBalancer` service is responsible for balancing the partitions between the consumers. The balancer awlays tries to balance the load uniformly between the brokers and the consumers. And also tries not to request the brokers and the consumers to do unnecessary actions. the events that activate the balancers are:
- A broker failure
- A consumer failure
- A new broker registration
- A new consumer registration
- A new partition assignment




