FROM openjdk:17-alpine

ARG JAR_FILE=target/cognixus_to_do_list_assessment-1.0.jar
ARG JASYPT_ENCRYPTOR_PASSWORD

COPY ${JAR_FILE} app.jar

ENV JASYPT_ENCRYPTOR_PASSWORD=$JASYPT_ENCRYPTOR_PASSWORD

ENTRYPOINT ["java", "-jar", "/app.jar"]
