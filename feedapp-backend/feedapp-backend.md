## Trello

[Trello](https://trello.com/b/sKPuLBzR/dat250)

## Docker 

- Docker is used to containerize the application.
- To build our docker image, run the following command in the `feedapp-backend` directory of the project: `docker build -t feedapp:latest .`

## Docker compose 

- Docker compose is used to run the application in a containerized environment, with both the application and the database(mongoDB) running in separate containers.
- to use docker compose, run the following command in the `feedapp-backend` directory of the project: `docker-compose up`
- If u dont have docker compose installed, run `brew install docker-compose` in the terminal. (MacOS)

## MongoDB 

- MongoDB is used as the NoSQL database for this project.
- To access the database in the mongoshell, run the following command: `docker exec -it feedapp-backend-mongo-1 mongosh`
- Where `feedapp-mongo-1` is the name of the container running the database.
- The field `_id: ObjectId` is automatically generated by MongoDB when a document is created.

*Commands inside the mongoshell:*
- `show dbs` - shows all databases
- For now our db is called `feedappdb`

- `use feedappdb` - to switch to the database

- `show collections` - shows all collections in the database
- At this moment the collection we have is `pollResult`

- `db.pollResult.find()` - shows all documents in the collection
- `db.pollResult.drop()` - drops the collection
- to delete the elements inside the collection, run `db.pollResult.deleteMany({})`

## Kubernetes 
** U have to locally have the docker images of the application before running the following commands **

- If you dont have kubernetes installed in terminal, run `brew install kubernetes-cli` (MacOS)
- You also have to enable kubernetes inside docker desktop 
  - Go to docker desktop -> settings -> kubernetes -> enable kubernetes
- Inside feedapp-backend folder run `kubectl apply -f deployment/` to create a deployment
- Run `kubectl get deployments` to see the deployment
- Run `kubectl get pods` to see the pods

- To expose the backend `kubectl port-forward service/feedapp 8080:8080` 

- To expose the frontend run `kubectl port-forward service/feedapp-frontend 4200:80`


### Stop 
- To stop the deployment, run `kubectl delete -f deployment.yaml`, inside the deployment folder

**Access MongoDB inside kubernetes pod:**
- Run `kubectl get pods` to see the pods name 
- `kubectl exec -it <pod-name>  -c mongodb-container -- mongosh`

## IotSimulation 
- inside iotSimulation folder run `npm install -g http-server` to install dependencies
- then run `http-server -p 8000` to start the server on `http://localhost:8000/`

**Docker image**
`docker build -t iot-simulation:latest .`

## RabbitMQ
- When application is running, locate to:`http://localhost:15672`
- Username: `guest`
- Password: `guest`