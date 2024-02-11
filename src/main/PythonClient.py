import requests
import threading

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