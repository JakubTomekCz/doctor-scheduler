FROM openjdk:16
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Dserver.port=80","/app.jar"]
EXPOSE 80
