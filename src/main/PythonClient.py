import time

import requests

class PythonClient:
    def __init__(self, base_url):
        self.base_url = base_url

    def pull(self):
        response = requests.post(f'{self.base_url}/pull')
        return response.text

    def push(self, key, message):
        response = requests.post(f'{self.base_url}/push', data={'key': key, 'message': message})
        return response.text

    def subscribe(self, f):
        response = requests.post(f'{self.base_url}/subscribe')
        if response.text == "OK":
            self.pull_as_subscriber(f)

    def pull_as_subscriber(self, f):
        # now we are a subscriber, send this request every 1 sec
        while True:
            response = requests.post(f'{self.base_url}/pull')
            f(response.text)
            time.sleep(1)