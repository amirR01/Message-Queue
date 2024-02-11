from PythonClient import PythonClient
import sys
producer = True
consumer = False
def main(ip,port):
    client = PythonClient(f'http://{ip}:{port}/api/python')
    while True:
        command = input('Enter command: ')
        if command == 'exit':
            break
        elif command == 'pull' and consumer:
            print(client.pull())
        elif command == 'push' and producer:
            key = input('Enter key: ')
            message = input('Enter message: ')
            print(client.push(key, message))
        elif command == 'subscribe' and consumer:
            def print_message(message):
                print('Received message:', message)
            client.subscribe(print_message)
        else:
            print('Unknown command')

if __name__ == '__main__':
    ip = sys.argv[1]
    port = sys.argv[2]
    main(ip,port)