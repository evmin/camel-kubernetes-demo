# Apache Camel in Google Kubernetes Engine

A practical template on how to deploy [Apache Camel](http://camel.apache.org/) on [Google Kubernetes Engine](https://cloud.google.com/kubernetes-engine/).

* [Why?](reference/00_why.md)
* [How?](reference/00_how.md)
* [Toolstack required](reference/02_toolstack_required.md)
* [Google Cloud Project setup](reference/03_gcp_setup.md)
* [Build and Deploy](reference/04_build_deploy.md)

Structure:

* gke-camel-template : Apache Camel Project
* reference : docuemtnation section
* py-pubsub-util : a couple of python scripts to publish / consumer messages from PubSub
* schemas : BigQuery and PubSub JSON Schemas