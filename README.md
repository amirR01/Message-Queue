# Message-Queue
The implemented project is a message queue and broker system similar to `Kafka` or `RabbitMQ`. This project serves as a minimum viable product for a real industrial message queue system that utilizes advanced system design and analysis, despite its simple implementation.

The project consists of three microservices: [**broker**](https://github.com/amirR01/Message-Queue/tree/main/broker), [**server**](https://github.com/amirR01/Message-Queue/tree/main/server), and [**client**](https://github.com/amirR01/Message-Queue/tree/main/client) that communicate with each other using https restful APIs. The broker is responsible for receiving, storing, and delivering messages to the clients. The server acts as a cordinator between the brokers themselves and the clients. The client can be a producer or a consumer. The producer sends messages to the broker, and the consumer receives messages from the broker. each microservice has its own readmes that explains more about the service.

picture below shows simplified version of the system design. you can find more details in each service's readme.

![imageedit_6_9815287502](https://github.com/amirR01/Message-Queue/assets/78862582/0cdef36a-1588-459c-8836-f4c26ee64eab)


## Functional Requirements
- The ablitity to send and receive messages by producers and consumers.
- The ability to **subscribe** to queue or pull messages from the queue.
- Each message should be received by only one consumer.
- The messages should be received in the **order** they were sent per key.

## Non-Functional Requirements
- **Scalability** 
- **Fault Tolerance**
- **Monitoring**
- **Portability**
- **CI/CD**
- **Orchestration**

### Scalability
In case of having high load of messages with different keys, by adding more brokers, or more consumers, it is expected to have a better performance. This means that the system should balance the load between the brokers and the consumers. For this purpose, we used **partitioning** and **replication** techniques.

Each key is hashed and assigned to a specific partition. Each partition is assigned to a specific broker for storing the messages and specific consumers for consuming the messages. by dining or adding more brokers or consumers, the [**server**](https://github.com/amirR01/Message-Queue/tree/main/server) service will be responsible for reassigning the partitions to the brokers and consumers.

you can read more about rebalancing and health check in the [**server**](https://github.com/amirR01/Message-Queue/tree/main/server) service's readme.

### Fault Tolerance
In case of having a broker failure, the system should be able to recover the messages and the partitions that were assigned to the failed broker. For this purpose, we used **replication** technique. Each partition is replicated to another broker. In case of having a broker failure, the [**server**](https://github.com/amirR01/Message-Queue/tree/main/server) service will be responsible for reassigning the partitions to the available brokers. The [**server**](https://github.com/amirR01/Message-Queue/tree/main/server) will command the secondary broker to become the primary broker and replicate its messages to the new secondary broker.

you can read more about replication and fault tolerance and the API's between the brokers themselves and the server in the [**broker**](https://github.com/amirR01/Message-Queue/tree/main/broker) service's readme.

### Monitoring
For monitoring the system, we used **Prometheus** and **Grafana**. The health of the each service and also other metrics are collected by Prometheus and visualized by Grafana. some of the metrics are:
- The number of messages in the queue.
- The number of messages sent and received.
- The number of consumers and producers.
- The number of partitions and brokers.
- and so on.

### Portability
The system is dockerized. Each service has its own Dockerfile. The system can be deployed on any platform that supports docker. 

### CI/CD
We used `Gitlab CI/CD` for testing, building, and deploying the system. The CI/CD pipeline is defined in the `.gitlab-ci.yml` file. The pipeline consists of the following stages:
- **Test**: run the unit tests.
- **Build**: build the docker images.
- **Deploy**: pull the images and compose the services on the server.

The picture below shows the CI/CD pipeline. Project piplines from the beginning are available in the gitlab repository of each service.

<img width="459" alt="Screenshot 2024-04-05 at 2 07 58 AM" src="https://github.com/amirR01/Message-Queue/assets/78862582/35ed9903-f60c-4093-b188-8fae5611429c">


gitlab repositories:
[server](https://gitlab.com/amirR01/MQ-SAD-1402-1-SERVER), [broker](https://gitlab.com/amirR01/mq-sad-1402-1-broker), [client](https://gitlab.com/amirR01/mq-sad-1402-1-java-client)

### Orchestration
For orchestrating the services, we used `docker-compose`. The `docker-compose.yml` file defines the services and their configurations. Volumes of the services are kept in the `docker_volumes` directory. To run the system, we need at least one broker, one server, and two clients one for producer and one for consumer. The system can be scaled by adding more brokers or clients by changing the `docker-compose.yml` file.

## How to run
As it was mentioned before, the system is dockerized. To run the system, you need to have `docker` and `docker-compose` installed on your machine. Then, you can run the following command:
```bash
docker-compose up
```
This command will pull the images from the gitlab registry and run the services. 

## Testing
The unit tests are available in each service and also some requierments are tested by the integration tests available in `test` directory. 

## Software Development Methodology
We used `Agile` methodology and `Scrum` framework for developing the project. The project was divided into sprints, and each sprint was divided into tasks. The tasks were assigned to the team members. The tasks were tracked on the `Jira` board. 
In this project beside the development of most parts of the system, I played the role of the `Tech Lead`. I was responsible for the system design, code review, and also the CI/CD pipeline. I was also responsible for the communication between the team members and defining and dividing the tasks. I was also responsible for the system documentation. I thank my team members for their hard work and dedication.

## Open To Contribution
The project is open to contribution. You can fork the project and add more features to it, or contact me for ideas and suggestions. I am open to collaboration and contribution.

## License
The project is licensed under the MIT License. You can use the project for any purpose, commercial or non-commercial. You can also modify the project and distribute it. 
