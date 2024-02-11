from PythonClient import PythonClient
import sys
def main(ip,port):
    client = PythonClient(f'http://{ip}:{port}/api/python')
    while True:
        command = input('Enter command: ')
        if command == 'exit':
            break

        elif command == 'pull' and not is_producer:
            print(client.pull())

        elif command == 'push' and is_producer:
            key = input('Enter key: ')
            message = input('Enter message: ')
            print(client.push(key, message))

        elif command == 'subscribe' and not is_producer:
            def print_message(message):
                if (len(message) > 2):
                    print('Received message:', message)
            client.subscribe(print_message)

        else:
            print('Unknown command')

if __name__ == '__main__':
    ip = sys.argv[1]
    port = sys.argv[2]
    is_producer = eval(sys.argv[3])
    main(ip,port)