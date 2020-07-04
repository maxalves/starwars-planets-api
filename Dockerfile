FROM openjdk:11-jdk-slim as build
RUN mkdir app
WORKDIR /app

COPY ["gradlew", "build.gradle", "./"]
COPY gradle gradle
RUN ./gradlew dependencies

COPY src src
RUN ./gradlew build -x test && \
 mkdir -p build/dependency && \
  (cd build/dependency; jar -xf ../libs/*.jar)

FROM openjdk:11-jdk-slim
VOLUME /tmp
COPY --from=build /app/build/libs/*.jar /app.jar

ENV SPRING_PROFILES_ACTIVE=docker

EXPOSE 8080
CMD ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
