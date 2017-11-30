#!/usr/bin/env python
from google.cloud import pubsub
import os

client = pubsub.PublisherClient()

t = 'projects/{project_id}/topics/{topic}'.format(
    project_id=os.environ['PROJECT_ID'],
    topic='demo.event'
)

body = """
{
  "timestamp": 1469663359000,
  "correlationId": "100",
  "text": "Arbitrary Text"
}
"""

print client.publish(t, body).result()
