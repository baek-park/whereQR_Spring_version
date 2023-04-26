FROM openjdk:11-jdk as builder

WORKDIR /builder

COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle ./
COPY settings.gradle ./

COPY src ./src

RUN ./gradlew bootJar

FROM openjdk:11-jdk as runner

WORKDIR /app

COPY --from=builder /builder/build/libs/app.jar ./

EXPOSE 8080

CMD ["java","-jar","app.jar"]
