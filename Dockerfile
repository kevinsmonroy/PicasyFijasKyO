# =========================
# ETAPA 1: BUILD
# =========================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar TODO el proyecto
COPY . .

# Entrar directamente donde está el pom.xml
WORKDIR /app/PicasyFijasK

# Compilar (SIN wrapper para simplificar)
RUN mvn clean package -DskipTests

# =========================
# ETAPA 2: RUN
# =========================
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiar el JAR correcto (NO wildcard peligroso)
COPY --from=build /app/PicasyFijasK/target/*jar /app/app.jar

# Puerto dinámico de Render
ENV PORT=10000

EXPOSE 10000

ENTRYPOINT ["java","-jar","/app/app.jar"]



