# Apache Camel Template for GKE

Apache Camel solution template to be deployed at [Google Container Engine](https://cloud.google.com/container-engine/).

## Tooling

Following tools will be required:

* [Gradle Build Tool](https://gradle.org/install/)
* [Google Cloud SDK and CLI](https://cloud.google.com/sdk/downloads)




## Setup

Set up the environment variable and install gradle wrapper:

```bash
export PROJECT_ID=<your_gcp_project_id>
gradle wrapper
```



Authenticate to GCP:

```bash
gcloud auth login
gcloud config set project $PROJECT_ID
gcloud config set compute/zone $CLUSTER_ZONE
gcloud config set container/cluster $CLUSTER_NAME
gcloud container clusters get-credentials $CLUSTER_NAME
```



Select Kubernetes cluster context:

```bash
kubectl config get-contexts
kubectl config use-context <KUBERENETES-CONTEXT>
```



Authenticate to GCP Container Registry:

```bash
gcloud docker -a
```



## Build

Build and push the image into the Google Cloud Continer Registry:

```bash
./gradlew dockerPush
```

This is effectively an alias for `docker build` and `docker push` executed through a gradle plugin.

Generate Kubernetes deployment file:

```bash
./gradlew k8s
```



## Deploy

```bash
kubectl create -f k8s-gke-camel-template-1.0.0-SNAPSHOT.yaml
kubectl logs -f deploy/gke-camel-template
```


