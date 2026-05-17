FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . .

WORKDIR /app/PicasyFijasK

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "target/*.jar"]
