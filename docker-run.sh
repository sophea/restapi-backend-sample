#!/usr/bin/env bash
basedir=$(dirname $0)

##get current version
currentVersion=$(mvn help:evaluate -Dexpression=project.version | grep -e '^[^\[]')

./docker-build.sh

REPO_NAME=`$basedir/repo-name.sh`

case $OSTYPE in
  linux*)
    export OS_NAME=linux
    ;;
  darwin*)
    export OS_NAME=osx
    ;;
  msys*)
    export OS_NAME=windows
    ;;
  *)
    export OS_NAME=notset
    ;;
esac

window_command=""
if [[ "$OS_NAME" == "windows" ]]; then
    window_command="winpty"
fi
#echo "$OS_NAME  - $window_command"


$window_command docker run --name $REPO_NAME -p 8080:8080 -p 443:443 -it  --rm $REPO_NAME:${currentVersion}
