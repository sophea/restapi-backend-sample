# syntax=docker/dockerfile:experimental
FROM public.ecr.aws/amazoncorretto/amazoncorretto:11 as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
COPY target target
#RUN --mount=type=cache,target=/root/.m2 ./mvnw  install -DskipTests
#./mvnw install -DskipTests
FROM public.ecr.aws/amazoncorretto/amazoncorretto:11

#RUN addgroup -S springboot && adduser -S springboot -G springboot
#USER springboot

VOLUME /tmp
ARG JAR_FILE=/workspace/app/target/*.war
COPY --from=build ${JAR_FILE} app.war
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.war"]
