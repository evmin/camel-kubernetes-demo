

# Build and Deploy

## Environment Setup

The build procedures depend on `PROJECT_ID` envionrment variable. Make sure it is set to **YOUR** project.

```bash
export PROJECT_ID=silver-ribbon-717
```



## Source Code

Check out the source code:

```bash
git clone https://github.com/evmin/camel-kubernetes-demo.git
```



##Gradle Wrapper

And setup the Gradle Wrapper:

```bash
cd camel-kubernetes-demo/gke-camel-template
gradle wrapper
```



## Build

The project is managed by Gradle, please refer to to the [Tooling Section](02_toolstack_required.md) section for installation instructions.

```bash
# Authenticate for Google Cloud Registry push
gcloud docker -a

# Build docker image and push it to Google Container Registry
./gradlew dockerPush

# Generate Config in the build folder
./gradlew k8s
```

The last command - k8s - is a custom gradle build task that generates the Kubernetes Deployment YAML substituting the placeholders with actual project values.



## Deploy

```bash
kubectl create -f ./build/k8s-gke-camel-template-1.0.0-SNAPSHOT.yaml
```

Check the logs:

```
kubectl logs -f deploy/8s-gke-camel-template
```

