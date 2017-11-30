#!/usr/bin/env python
from google.cloud import pubsub
import time, os

client = pubsub.SubscriberClient()

subscription_name = 'projects/{project_id}/subscriptions/{sub_id}'.format(
    project_id=os.environ['PROJECT_ID'],
    sub_id='demo.event.bigquery'
)

subscription = client.subscribe(subscription_name)

def callback(message):
    print(message.data)
    message.ack()

subscription.open(callback)

while True:
    time.sleep(0.1)
