#!/bin/bash

CONTAINER_NAME="sn-postgres-db"

POSTGRES_USER="myuser"
POSTGRES_PASSWORD="mypassword"
POSTGRES_DB="fileMetadata"
POSTGRES_IMAGE="postgres:latest"

if [ "$(docker ps -a -q -f name=^/${CONTAINER_NAME}$)" ]; then
    echo "PostgreSQL Container exists"

    if [ "$(docker ps -q -f name=^/${CONTAINER_NAME}$)" ]; then
      echo "PostgreSQL container is running"
    else
      echo "Starting PostgreSQL container"
      docker start $CONTAINER_NAME
    fi
else
  echo "Creating and starting a new PostgreSQL container..."
  docker run -d \
    --name $CONTAINER_NAME \
    -e POSTGRES_USER=$POSTGRES_USER \
    -e POSTGRES_PASSWORD=$POSTGRES_PASSWORD \
    -e POSTGRES_DB=$POSTGRES_DB \
    -p 5432:5432 \
    $POSTGRES_IMAGE
fi

echo "Waiting for PostgreSQL to be ready..."
sleep 5

echo "Starting the Server with arguments: $@"
exec java -jar /storageNode.jar "$@"
