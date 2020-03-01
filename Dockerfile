#
# Build stage
#
FROM maven:3.6.3-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean install package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/honza-botner-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/local/lib/botner.jar
COPY .env /home/app/.env
COPY config.json /home/app/config.json
EXPOSE 8080:80
WORKDIR /home/app
ENTRYPOINT ["java","-jar","/usr/local/lib/botner.jar"]

