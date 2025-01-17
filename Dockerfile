FROM alpine:latest
RUN apk update && apk add --no-cache openjdk21
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Duser.timezone=America/Sao_Paulo","-jar","app.jar"]