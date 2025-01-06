#!/bin/bash

DOCKER_REPO="pablords"
VERSION=$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout)
CONTEXT="dev"

echo $VERSION

docker build \
-t $DOCKER_REPO/parking:$VERSION \
--build-arg JAR_FILE=target/parking-$VERSION.jar .


docker run \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=$CONTEXT \
  -v ./src/main/resources/application.yml:/app/config/application.yml \
  pablords/parking:$VERSION \
  --spring.config.location=file:/app/config/application.yml