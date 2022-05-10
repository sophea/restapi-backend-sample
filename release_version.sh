#!/usr/bin/env bash
basedir=$(dirname $0)
##build the project
echo "build phase"
mvn clean package

if [[ $? != 0 ]]; then
 echo ">>>>>>>build is not successful"
 exit 1
fi

##get current version
currentVersion=$(mvn help:evaluate -Dexpression=project.version | grep -e '^[^\[]')
### close version without SNAPSHOT
newVersion=$(echo ${currentVersion} | cut -d\- -f1)
echo "new Version is ${newVersion}"
mvn versions:set -DnewVersion=$newVersion

echo "create tag $newVersion"

git add pom.xml
git commit -m "AUTOMATIC: Closed release $newVersion"
git tag -a sec-$newVersion -m "release version $newVersion"
git push --set-upstream --tags

#echo "upload the artifact"
#mvn deploy

###Increase version +1 and appending SNAPSHOT
currentVersion=$newVersion
versionMinor=$(echo ${currentVersion##*.})
versionMajor=${currentVersion: 0: -${#versionMinor} }
newSnapshotVersion=${versionMajor}$((versionMinor+1))-SNAPSHOT

mvn versions:set -DnewVersion=$newSnapshotVersion
git add pom.xml
git commit -m "AUTOMATIC: Created new snapshot version $newSnapshotVersion"
branch=`git branch | grep \* | cut -d ' ' -f2`
git push --set-upstream origin $branch
