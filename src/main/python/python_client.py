# import requests
import threading
import json
import requests

class Client:
    def __init__(self, server_address, server_port):
        self.base_url = f'http://{server_address}:{server_port}/api/python'


class Producer(Client):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
    
    def push(self, key, message):
        headers = {'Content-Type': 'application/json'}
        response = requests.post(f'{self.base_url}/push', data=json.dumps({'key': key, 'message': message}), headers = headers)
        return response.text


class Consumer(Client):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
    
    def subscribe(self, f): # consumer
        # create a thread that sends a subscribe request
        response = requests.post(f'{self.base_url}/subscribe')
        if response.text == "OK":
            i = 0
            while True:
                i += 1
                response = requests.post(f'{self.base_url}/pull-as-subscriber')
                thread = threading.Thread(target=f, args=(response.text,))
                thread.daemon = True
                thread.start()
                if i == 5:
                    response = requests.post(f'{self.base_url}/subscribe')
                    i = 0
    
    def pull(self):
        response = requests.post(f'{self.base_url}/pull')
        return response.text