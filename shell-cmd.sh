#!/usr/bin/env bash

export IMAGE=$1
export DOCKER_USERNAME=$2
export DOCKER_PASSWORD=$3
echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin
docker-compose -f docker-compose.yaml up --detach
echo 'success'