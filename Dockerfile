FROM gradle:4.3.0-jdk8-alpine as jar
USER root
WORKDIR /tmp/project
COPY . /tmp/project
RUN ["gradle", "jar"]

FROM java:8
ARG VERSION=1.0
COPY --from=jar /tmp/project/build/libs/sandbox-${VERSION}-SNAPSHOT.jar ./sandbox.jar
ENTRYPOINT ["java", "-jar", "./sandbox.jar"]