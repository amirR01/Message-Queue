from client import pull, push, subscribe

print("start to push some data")
for i in range(1000):
    push(str(i%9),str(i))

print("start to read ")
dict = {}
all_values = set()
counter = 0
# order test
def check_order(key,value):
    value = int(value)
    if dict.get(key):
        if value <= dict[key]:
            print("tagasom shash")
    dict[key] = value
    all_values.add(value)
    if len(all_values) == 1000:
        print('shash finished')

    

subscribe(check_order)
