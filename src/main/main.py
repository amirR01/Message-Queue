from PythonClient import PythonClient

producer = True
consumer = False
def main():
    client = PythonClient('http://localhost:8080')  # replace with your base_url
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
    main()