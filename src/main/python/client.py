from python_client import Consumer, Producer


CONSUMER_ADDRESS = "localhost"
CONSUMER_PORT = 8092
PRODUCER_ADDRESS = "localhost"
PRODUCER_PORT = 8090

consumer = Consumer(CONSUMER_ADDRESS, CONSUMER_PORT)
producer = Producer(PRODUCER_ADDRESS, PRODUCER_PORT)

def pull():
    return consumer.pull()

def push(key, val):
    return producer.push(key, val)

def subscribe(action):
    consumer.subscribe(action)
