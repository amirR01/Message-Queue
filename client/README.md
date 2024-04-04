# Client
A client can be a consumer or a producer. A consumer is a client that subscribes to the queue and a producer is a client that pushes messages to the queue.

## Implementation Details
The client is implemented in `java` using `spring-boot` framework. And also we implemented a `python` proggram that talks to the `java` client using restful APIs and can be used as a producer or a consumer. The `python` and `java` are placed in different directories with corresponding names. The 

## Producer 
The producer is responsible for sending the messages to the queue. The producer should register itself to the server and send its information to the server. On each push request, the producer will check if it knows the partition of the key of the message. If it knows the partition, it will send the message to the broker of the partition. If it does not know the partition, it will ask the server to assign a partition to the key of the message. And then it will send the message to the broker of the partition.

## Consumer
The consumer is responsible for receiving the messages from the queue. The consumer should register itself to the server and send its information. when user commands to subscribe, the consumer will ask the server to assign free partitions to itself. And then it will start to receive the messages from the assigned partitions. The consumer will send a heartbeat to the server to show that it is alive.

## How to Use

### Java Client
To push a message to the queue with producer type the following command on the terminal:
```bash
produce --key x --value y
```
To subscribe to the queue with consumer type the following command on the terminal:
```bash
subscribe
```

### Python Client
To use the python client, you should first run `main.py` file. 
```bash
python main.py --consumer_address x --consumer_port y --producer_address z --producer_port w
```
Then you can use the following commands to push a message to the queue with producer type the following command on the terminal:
```bash
push x y
```
To subscribe to the queue with consumer type the following command on the terminal:
```bash
subscribe
```

