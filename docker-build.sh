#!/usr/bin/env bash

##get current version
currentVersion=$(mvn help:evaluate -Dexpression=project.version | grep -e '^[^\[]')

echo "docker build with tag demo:${currentVersion}"

DOCKER_BUILDKIT=1 docker build  . --tag demo:${currentVersion}