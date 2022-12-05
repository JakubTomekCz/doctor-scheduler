FROM openjdk:16
RUN addgroup -S javauser && adduser -S javauser -G javauser
USER javauser:javauser
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

