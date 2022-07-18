FROM openjdk:8-jdk-alpine

EXPOSE 8080

ARG JAR_FILE=build/libs/authorizer-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} authorizer.jar

ENTRYPOINT ["java","-jar","/authorizer.jar"]