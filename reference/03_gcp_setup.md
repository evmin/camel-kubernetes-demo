#Google Cloud Platform Setup

If required signup for the [Google Cloud Platform trial](https://cloud.google.com/free/). 

Google Cloud Platform products will be used:

* [Google Kubernetes Engine](https://cloud.google.com/kubernetes-engine/)
* [Google Container Registry](https://cloud.google.com/container-registry/)
* [Google Stackdriver Logging](https://cloud.google.com/logging/)
* [Google Stackdriver Monitoring](https://cloud.google.com/monitoring/)


## CLI Setup

While almost everything described in this demo can be achieved through the Google Cloud Console UI, I would like to provide the command line code - for consistency.

Please refer to the [Tooling Section](02_toolstack_required.md) for the detailed instructions on command line installation.

Start with setting up the environment and authenticating to GCP. Make sure to **replace `PROJECT_ID`** in the script below:

```bash
export PROJECT_ID=silver-ribbon-717
export CLUSTER_NAME=camel-demo
export CLUSTER_ZONE=australia-southeast1-b

gcloud config set project $PROJECT_ID
gcloud config set compute/zone $CLUSTER_ZONE
gcloud config set container/cluster $CLUSTER_NAME
```

### Authenticate to Google Cloud

This step can be skipped if running in Google Cloud Shell environment.

```bash
# Generic gcloud access
gcloud auth login

# Default application access for python or JDK
gcloud auth application-default login
```

## Enable the APIs

This, as everything else with GCP, can be done either via [API management console](https://console.cloud.google.com/apis/library) or through the command line:

```bash
gcloud services enable --project $PROJECT_ID bigquery-json.googleapis.com
gcloud services enable --project $PROJECT_ID monitoring.googleapis.com
gcloud services enable --project $PROJECT_ID containerregistry.googleapis.com
gcloud services enable --project $PROJECT_ID pubsub.googleapis.com
```

## Create the Kubernetes cluster

```bash
gcloud container clusters create "$CLUSTER_NAME" \
  --cluster-version "1.8.2-gke.0" \
  --machine-type "n1-standard-2" \
  --disk-size 10 \
  --scopes https://www.googleapis.com/auth/compute,\
https://www.googleapis.com/auth/devstorage.read_only,\
https://www.googleapis.com/auth/logging.write,\
https://www.googleapis.com/auth/monitoring \
  --zone $CLUSTER_ZONE \
  --num-nodes 1 \
  --max-nodes 3 \
  --disable-addons KubernetesDashboard \
  --enable-autoupgrade \
  --enable-autoscaling \
  --network "default" \
  --enable-cloud-logging \
  --enable-cloud-monitoring

gcloud container clusters get-credentials $CLUSTER_NAME
```

### Grant cluster service account access to the Container Registry

Granting the Service Account that owns and runs the cluster the access to Google Cloud Storage - that's where Container Registry will put the uploaded Docker images.

```bash
export PROJECT_NUMBER=$(gcloud projects list --filter='projectId='$PROJECT_ID --format='value(projectNumber)')

gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member serviceAccount:$PROJECT_NUMBER-compute@developer.gserviceaccount.com \
    --role roles/storage.objectViewer
```



## PubSub

```bash
gcloud beta pubsub topics create demo.event
gcloud beta pubsub subscriptions create demo.event.bigquery --topic demo.event --ack-deadline 60
```



## BigQuery

The schema is also available as `schemas/bigquery/demo_event.json`. 

```bash
echo 'project_id = '$PROJECT_ID >> ~/.bigqueryrc
bq mk -d --data_location=US demo
cat << EOF > ./bigquery_demo_event.json
[
  {"name":"id",      "type":"STRING",  "mode": "REQUIRED", "description": "Unique Correlation ID"},
  {"name":"updated", "type":"INTEGER", "mode": "REQUIRED", "description": "Timestamp in milliseconds"},
  {"name":"text",    "type":"STRING"}
]
EOF
bq mk --time_partitioning_type=DAY --schema=bigquery_demo_event.json demo.demo_event
```



## Camel Application Service Account

This is the service account Camel Application will be using to connect to PubSub and BigQuery. It is different from the Service Account used by the cluster - the blueprint allows for multiple service accounts with different permissions, when required.

```bash
gcloud iam service-accounts create demo-cluster
gcloud iam service-accounts keys create \
    ./demo-cluster-key.json \
    --iam-account demo-cluster@$PROJECT_ID.iam.gserviceaccount.com
```



## Grant permissions to the Application Service Account

```bash
# BigQuery
gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member serviceAccount:demo-cluster@$PROJECT_ID.iam.gserviceaccount.com \
    --role "roles/bigquery.dataEditor"

gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member serviceAccount:demo-cluster@$PROJECT_ID.iam.gserviceaccount.com \
    --role "roles/bigquery.user"

gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member serviceAccount:demo-cluster@$PROJECT_ID.iam.gserviceaccount.com \
    --role "roles/bigquery.jobUser"

# PubSub
gcloud beta pubsub subscriptions add-iam-policy-binding \
    demo.event.bigquery \
    --member=serviceAccount:demo-cluster@$PROJECT_ID.iam.gserviceaccount.com \
    --role "roles/pubsub.subscriber"
```



## Cluster Application Configuration

The newly created service account key needs to be accessible by the applications from within the Kubernetes cluster. 

Adding one as cluster secret.

```bash
kubectl create secret generic demo-cluster-key --from-file=./demo-cluster-key.json
```

Adding project id as a configuration map entry:

```bash
kubectl create configmap project --from-literal=id=$PROJECT_ID
```



## Done!

The Google Cloud Project has been set up for the Camel/Kubernetes/PubSub/Bigquery action.