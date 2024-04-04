### this test checks if the cluster performance scales with the size of the cluster
### run the test and press enter untill you see a thourghput cap in the monitoring of the cluster
### manually scale up cluster 
### press enter once more and see if you have a increase in throughput rate of the cluster

import multiprocessing
from client import pull, push, subscribe

TEST_SIZE = 1000 * 1000
KEY_SIZE = 8
SUBSCRIER_COUNT = 4


def to_infinity():
    index = 0
    while True:
        yield index
        index += 1


def push_key(key: str):
    for i in to_infinity():
        push(key, f"{i}".encode("utf-8"))
        print(key, f"{i}".encode("utf-8"))


subscribe(lambda key, val: ...)

for i in to_infinity():
    p = multiprocessing.Process(target=push_key, args=(i,))
    p.start()
    print("did it cap?")
    print("if not, press enter to increase throughput")
    print("if capped, manually scale up the cluster and press enter to see if you can increase the throughput")
    input()
