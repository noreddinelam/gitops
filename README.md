# GitOps Spring Boot Deployment

This project demonstrates a **GitOps workflow** to deploy a simple **Spring Boot application** using **Docker**, **Docker Compose**, **Kubernetes (Kind)**, **Helm**, and **ArgoCD**.

## 🚀 Project Overview

- A basic Spring Boot application connected to a PostgreSQL database exposing some endpoints.
- Dockerized with a `Dockerfile` and `docker-compose.yml` for local container testing.
- Helm chart used for Kubernetes deployment with three separate profiles: `dev`, `staging`, and `production`.
- GitOps structure powered by **ArgoCD**, managing the lifecycle of three different environments.
- Kubernetes cluster created locally using [Kind](https://kind.sigs.k8s.io/).

---

## 🧱 Project Structure

```
.
├── Dockerfile                     # Docker image for the Spring Boot app
├── docker-compose.yml             # Local container orchestration
├── gitops-learn/                  # Helm chart for the application
│   └── templates/                 # Kuberenetes manifests
│   └── values-{env}.yaml          # Values for dev, staging, and production
├── apps/
│   ├── dev-app.yaml               # ArgoCD app for dev
│   ├── staging-app.yaml           # ArgoCD app for staging
│   └── production-app.yaml        # ArgoCD app for production
└── README.md
```

## ⚙️ Local Development
### Prerequisites
Docker

```
Docker Compose

Java & Maven (for building the Spring Boot app)

Kubernetes with Kind

kubectl

Helm

ArgoCD CLI (optional just for debugging purposes)
```

## 🐳 Docker Setup
### Build the Docker Image
```bash
docker build -t gitops-spring-boot-app:<tag-here> . # Build the Docker image for the Spring Boot app and for the tag use the values from `gitops-learn/values-{env}.yaml`
```
### Push the Docker Image
```bash
docker push gitops-spring-boot-app:<tag-here> # Push the Docker image to your Docker registry (you must be logged in to Docker Hub or your private registry)
```

## ☸️ Kubernetes Setup with Kind
We use Kind (Kubernetes IN Docker) to simulate a local cluster.

To create the cluster:
```bash
kind create cluster --name gitops-cluster # Create a Kind cluster named gitops-cluster
```

## 🌐 NGINX Ingress Controller
### Install NGINX Ingress Controller (Helm OR Manifests choose only one)
```bash
helm upgrade --install ingress-nginx ingress-nginx \
  --repo https://kubernetes.github.io/ingress-nginx \
  --namespace ingress-nginx --create-namespace # Install NGINX Ingress Controller Using Helm
 
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.12.3/deploy/static/provider/cloud/deploy.yaml # Apply the NGINX Ingress Controller manifests
```

## 🎯 ArgoCD & GitOps (Your repository should be added to argocd and your code is published to github)
### Install ArgoCD
```bash
kubectl create namespace argocd # Create a namespace for ArgoCD
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml # Install ArgoCD
```
Then apply the apps:
```bash
kubectl apply -f apps/dev-app.yaml # Deploy the dev application
kubectl apply -f apps/staging-app.yaml # Deploy the staging application
kubectl apply -f apps/production-app.yaml # Deploy the production application
```
### Accessing ArgoCD
To access the ArgoCD UI, you can port-forward the service:
```bash
kubectl port-forward svc/argocd-server -n argocd 8080:443
```
Then open your browser and go to `http://localhost:8080`.

### Login to ArgoCD
The default username is `admin`. To get the password, run:
```bash
kubectl get pods -n argocd # Get the list of pods in the argocd namespace
kubectl logs -n argocd <argocd-server-pod-name> -c argocd-server | grep "admin password" # Get the admin password
```

## 📦 Helm Chart (these instructions are to be used if you forgot to push your code to Github before creating the ArgoCD apps)
The Helm chart is located in the `gitops-learn` directory. It contains templates for Kubernetes manifests and values files for different environments.
### Install the Helm Chart
```bash
helm install gitops-spring-boot-app gitops-learn --namespace gitops --create-namespace -f gitops-learn/values-dev.yaml # Install the Helm chart for the dev environment
```
### Upgrade the Helm Chart
```bash
helm upgrade gitops-spring-boot-app gitops-learn --namespace gitops -f gitops-learn/values-dev.yaml # Upgrade the Helm chart for the dev environment
```
### Uninstall the Helm Chart
```bash
helm uninstall gitops-spring-boot-app --namespace gitops # Uninstall the Helm chart
```

## Expose the Application via Ingress
The application is exposed via an Ingress resource. The Ingress resource is defined in the Helm chart templates and uses the NGINX Ingress Controller. It has an SSL self-signed certificate for HTTPS access.
```bash
kubectl port-forward svc/ingress-nginx-controller -n ingress-nginx 8081:443 # Port-forward the NGINX Ingress Controller service to access the application
```
Then you can access the application at `http://localhost:8081`.
