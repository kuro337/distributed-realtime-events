# Redpanda Cluster + Real Time Applications 

- Repo:
  - Full Redpanda Cluster with 3 Brokers running on Kubernetes

  - Applications to consume and produce events from the Central Cluster

- Prerequisites

- `k3` Running locally
- `kubectl` + `helm` cli
- `Docker`


```bash

# Install k3d
wget -q -O - https://raw.githubusercontent.com/k3d-io/k3d/main/install.sh | bash
```

### Installation

#### 1. Create cluster

```bash

# Create cluster
k3d cluster create test-cluster

```

#### 2. Create Red Panda deployment


```bash
# One time setup for red panda repo
helm repo add redpanda https://charts.redpanda.com/
helm repo update

# Install red-panda
helm upgrade --install redpanda redpanda/redpanda --namespace redpanda --create-namespace --wait --values=./redpanda/k3d-values.yaml

```

#### 3. OPTIONAL: Install Kubernets Dashboard

```bash
helm repo add kubernetes-dashboard https://kubernetes.github.io/dashboard/

helm upgrade --install kubernetes-dashboard kubernetes-dashboard/kubernetes-dashboard --create-namespace --namespace kubernetes-dashboard

```
This command deploys kubernetes-dashboard on the Kubernetes cluster in the ```kubernetes-dashboard``` namespace with default configuration. 


#### 3a. Creat sample user

```
kubectl apply -f ./redpanda/dashboard-adminuser.yaml
kubectl apply -f ./redpanda/cluster-role-binding.yaml

```


#### 3b. Start Daskboard

```
export POD_NAME=$(kubectl get pods -n kubernetes-dashboard -l "app.kubernetes.io/name=kubernetes-dashboard,app.kubernetes.io/instance=kubernetes-dashboard" -o jsonpath="{.items[0].metadata.name}")
echo https://127.0.0.1:8443/

kubectl -n kubernetes-dashboard port-forward $POD_NAME 8443:8443
```

***NOTE***: You need a user token to log into the dashboard.


#### 3c. Get token

```
kubectl -n kubernetes-dashboard create token admin-user
```
 ***NOTE***: You need to periodically log into dashboard again with new token.


#### 4 Build custom image and import it to cluster

```bash
# BUILD image
 docker build -t redpanda-app .

 # Import image

 k3d image import redpanda-app -c test-cluster

```

***NOTE***: You need to repeate these steps if you change source code.

***NOTE***: This image also includes the "rpk" redpanda CLI.
  

***NOTE***: ```cluster info``` is the rpk command. You can use any other rpk commands here as well.

#### 5 Create deployment with the app

 ```bash
kubectl create namespace redpanda

kubectl apply -f ./redpanda/deploy-app.yaml
```

Make sure everything is working...
### 1 Pods
 ```bash
kubectl get pod -n redpanda
```

This should show result like this:
``` 
NAME                           READY   STATUS      RESTARTS   AGE
redpanda-0                     2/2     Running     0          3h43m
redpanda-configuration-7586j   0/1     Completed   0          3h43m
kefka-app-57776b785d-58pkg     1/1     Running     0          33m

```
##### 2 Replica Set (app)
 ```bash
kubectl get replicaset -n redpanda
```

This should show result like this:
```
NAME                   DESIRED   CURRENT   READY   AGE
kefka-app-57776b785d   1         1         1       35m
```


##### 3 Stateful Set (redpanda)

 ```bash
kubectl get statefulset -n redpanda
```
This should show result like this:
```
NAME       READY   AGE
redpanda   1/1     3h45m
```


#### 6 Testing

- Get the name of "kafla app" pod (using ```kubectl get pod -n redpanda```)

- Log into the "kafka app" pod
    ```
    kubectl exec -n redpanda -it kefka-app-6d46cb8bb6-f6rpv -- /bin/sh
    ```

- check redpanda cluster (using rpk)

    ```bash
    ./rpk cluster info --brokers redpanda-0.redpanda.redpanda.svc.cluster.local.:9093
    ```
- Run the chat room sample app (You need to log into two separate terminals and run the same app)
    ```bash
    node ./src/index.mjs
    ```
    
