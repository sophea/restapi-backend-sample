#!/usr/bin/env bash

basedir=$(dirname $0)
mvn clean install

##get current version
currentVersion=$(mvn help:evaluate -Dexpression=project.version | grep -e '^[^\[]')

REPO_NAME=`$basedir/repo-name.sh`
echo "docker build with tag $REPO_NAME:${currentVersion}"

#DOCKER_BUILDKIT=1 docker build  . --tag demo:${currentVersion}

docker build  . --tag $REPO_NAME:${currentVersion}
docker build  . --tag $REPO_NAME:latest
