FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /home/maven/src
COPY pom.xml /home/maven/src/
RUN mvn dependency:go-offline -B
COPY . /home/maven/src
RUN mvn -DskipTests=true clean package

FROM eclipse-temurin:17-jre-alpine
EXPOSE 8100

RUN mkdir /app
COPY --from=build /home/maven/src/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
