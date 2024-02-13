### this test checks if the order garantee holds, i.e. if (k1,v1) is pushed before (k1,v2)
### then it is read before (k1,v2)

import random
import sys
from typing import Dict, List

from client import pull, push, subscribe

TEST_SIZE = 1000 * 1000
KEY_SIZE = 8
SUBSCRIER_COUNT = 4

key_seq = [random.choice(range(KEY_SIZE)) for _ in range(TEST_SIZE)]

pulled: Dict[str, List[int]] = {}
for i in range(KEY_SIZE):
    pulled[f"{i}"] = []

def validate_pull(key: str, val: bytes):
    next_val = int(val.decode("utf-8"))
    if len(pulled[key]) != 0:
        prev_val = pulled[key][-1]
        if prev_val >= next_val:
            print(f"order violation, seq: [{prev_val}, {next_val}]\tkey: [{key}]")
            sys.exit(255)
    pulled[key].append(next_val)


for _ in range(SUBSCRIER_COUNT):
    subscribe(validate_pull)


for i in range(TEST_SIZE):
    push(f"{key_seq[i]}", f"{i}".encode(encoding="utf-8"))

print("order test passed successfully!")
