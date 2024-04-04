# import requests
import threading
import json
import requests
import re
import time

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

def extract_key_value(input_string):
    pattern = r'key:(?P<key>[^:]+)-value:(?P<value>[^:]+)'
    match = re.search(pattern, input_string)
    if match:
        return match.group('key'), match.group('value')
    else:
        return None, None

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
                if len(response.text) > 2 :
                    response = response.text[1:-1]
                    response = response.split(",")
                    key_value_tuples = [extract_key_value(message) for message in response]
                    for key,value in key_value_tuples:
                        thread = threading.Thread(target=f, args=(key,value))
                        thread.daemon = True
                        thread.start()
                if i == 5:
                    response = requests.post(f'{self.base_url}/subscribe')
                    i = 0
    
    def pull(self):
        response = requests.post(f'{self.base_url}/pull')
        response = response.text[1:-1]
        response = response.split(",")
        key_value_tuples = [extract_key_value(message) for message in response]
        return key_value_tuples
    
