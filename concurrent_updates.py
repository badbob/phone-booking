#!/usr/bin/env python3

import requests
import sys

from concurrent.futures import ThreadPoolExecutor
from requests.auth import HTTPBasicAuth

def book(id, user, password):

    basic = HTTPBasicAuth(user, password)
    resp = requests.put('http://localhost:8080/device/%d/book' % id, allow_redirects=False, auth=basic)

    match resp.status_code:
        case 200:
            print("Device id=%d was booked successfully" % id, flush=True)
            data = resp.json()
            assert data["bookedBy"] == user, "Expected user: %s, but actual was: %s" % (user, data["bookedBy"])
        case 403:
            data = resp.json()
            print("Failed to book device id=%d: %s" % (id, data["message"]), flush=True)
        case 409:
            data = resp.json()
            print("Failed to book device id=%d: %s" % (id, data["message"]), flush=True)
        case _:
            print("Unexpected response code: %d" % resp.status_code, flush=True)
            sys.exit(-1)

def release(id, user, password):

    basic = HTTPBasicAuth(user, password)
    resp = requests.put('http://localhost:8080/device/%d/release' % id, allow_redirects=False, auth=basic)

    match resp.status_code:
        case 200:
            print("Device id=%d was released successfully" % id, flush=True)
            data = resp.json()
            assert data["bookedBy"] is None
        case 403:
            data = resp.json()
            print("Failed to release device id=%d: %s" % (id, data["message"]), flush=True)
        case 409:
            data = resp.json()
            print("Failed to release device id=%d: %s" % (id, data["message"]), flush=True)
        case _:
            print("Unexpected response code: %d" % resp.status_code, flush=True)
            sys.exit(-1)

def book_loop(user, password):
    while True:
        book(2, user, password)

def release_loop(user, password):
    while True:
        release(2, user, password)

if __name__=='__main__':

    # book(2)
    # book(2)
    # release(2)
    # release(2)

    tp = ThreadPoolExecutor(max_workers=10)
    tp.submit(book_loop, 'bob', 'bob')
    tp.submit(release_loop, 'bob', 'bob')
    tp.submit(book_loop, 'alice', 'alice')
    tp.submit(release_loop, 'alice', 'alice')
