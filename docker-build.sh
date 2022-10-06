#!/usr/bin/env bash

basedir=$(dirname $0)
#mvn clean package

##get current version
currentVersion=$(mvn help:evaluate -Dexpression=project.version | grep -e '^[^\[]')

REPO_NAME=`$basedir/repo-name.sh`
echo "docker build with tag $REPO_NAME:${currentVersion}"

#DOCKER_BUILDKIT=1 docker build  . --tag demo:${currentVersion}

### docker Dockerfile_alpine
#docker build  . --tag $REPO_NAME:${currentVersion}  --file Dockerfile_alpine
#docker build  . --tag $REPO_NAME:latest  --file Dockerfile_alpine

docker build  . --tag $REPO_NAME:${currentVersion}
docker build  . --tag $REPO_NAME:latest
