FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build /target/fashion_e_commerce-0.0.1-SNAPSHOT.jar fashion_e_commerce.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "fashion_e_commerce.jar"]