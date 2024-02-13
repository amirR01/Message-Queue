from python_client import Consumer, Producer
import argparse

parser = argparse.ArgumentParser(description="Argument Parser for Consumer and Producer addresses and ports")
parser.add_argument("--consumer_address", type=str, default="localhost", help="Address for the Consumer (default: localhost)")
parser.add_argument("--consumer_port", type=int, default=8092, help="Port for the Consumer (default: 8092)")
parser.add_argument("--producer_address", type=str, default="localhost", help="Address for the Producer (default: localhost)")
parser.add_argument("--producer_port", type=int, default=8090, help="Port for the Producer (default: 8090)")
args = parser.parse_args()

CONSUMER_ADDRESS = args.consumer_address
CONSUMER_PORT = args.consumer_port
PRODUCER_ADDRESS = args.producer_address
PRODUCER_PORT = args.producer_port

consumer = Consumer(CONSUMER_ADDRESS, CONSUMER_PORT)
producer = Producer(PRODUCER_ADDRESS, PRODUCER_PORT)

while True:
    command = input('>').strip().lower()
    
    if command == 'exit':
        break

    elif command == 'pull':
        print(consumer.pull())
    
    elif command == 'subscribe':
        def print_message(messages):
            messages = eval(messages)
            print('\n'.join(messages))
        consumer.subscribe(print_message)
    
    elif command[:4] == 'push':
        if len(command.split()) < 3:
            print('Unknown command. specifications for push:\npush [KEY] [VALUE]')
            continue
        key, value = command.split()[1], ' '.join(command.split()[2: ])
        producer.push(key, value)
    else:
        print('Unknown command.')