#!/usr/bin/env bash

##get current version
currentVersion=$(mvn help:evaluate -Dexpression=project.version | grep -e '^[^\[]')

./docker-build.sh

winpty docker run -it -p 8080:8080 demo:${currentVersion}