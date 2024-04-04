import client as cl
import client2 as cl2
import random
import threading

def print_data_for_consumer1(key,value):
    print("consumer1",key,value)

def print_data_for_consumer2(key,value):
    print("consumer2",key,value)


thread1 = threading.Thread(target=cl.subscribe, args=(print_data_for_consumer1,))
thread2 = threading.Thread(target=cl2.subscribe, args=(print_data_for_consumer2,))
thread2.start()
thread1.start()
input()
print("start to push some data")

names = ["amir","sepehr","ali","baghieHamKeHichi"]

for i in range(1000):
    name = random.choice(names)
    cl.push(name,str(i))

print("start to read data")
consumer1_read_count = 0


