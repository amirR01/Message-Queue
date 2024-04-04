import client as cl
import random
print("start with 2 broker")
print("start to push some data")

names = ["amir","sepehr","ali","baghieHamKeHichi"]

for i in range(100):
    name = random.choice(names)
    cl.push(name,str(i))

print("check the data partitions")
print("add new broker")
print("check partitions balance and data consistency")
print("press enter to continue")
input()

def print_data(key,value):
    print(key,value)

cl.subscribe(print_data)

