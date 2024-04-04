import client as cl
import random
print("start to push some data")

names = ["amir","sepehr","ali","baghieHamKeHichi"]

for i in range(100):
    name = random.choice(names)
    cl.push(name,str(i))


print("stop one broker manually")
print("press enter when ever ready")
input()


print("read data and check if all data is there")
all_numbers = set()
def check_health(key,value):
    all_numbers.add(value)
    if len(all_numbers) == 100:
        print("all data is healthy")


cl.subscribe(check_health)