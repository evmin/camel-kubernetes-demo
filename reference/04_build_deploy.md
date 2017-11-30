

# Build and Deploy

## Environment Setup

All demo operations depend on PROJECT_ID envionrment variable. Make sure it is set to **YOUR** project.

```bash
export PROJECT_ID=silver-ribbon-717
```



## Build

The project is managed by Gradle, please refer to to the [Tooling Section](02_toolstack_required.md) section for installation instructions.

```bash
# Authenticate for Google Cloud Registry push
gcloud docker -a

# Build and push
./gradlew dockerPush

# Generate Config in the build folder
./gradlew k8s
```

The last command - k8s - is a custom gradle build task that generates the Kubernetes Deployment YAML substituting the placeholders with actual project values.

## Deploy

```bash
kubectl create -f ./build/k8s-gke-camel-template-1.0.0-SNAPSHOT.yaml
kubectl get pods
```


