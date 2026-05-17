# ========================================================
# ETAPA 1: Compilar la aplicación con Maven
# ========================================================
FROM maven:3.8.8-eclipse-temurin-17 AS build

# Definimos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos los archivos del proyecto al contenedor
COPY . .

# Compilamos el proyecto saltando los tests genéricos
# (Obligatorio porque en Render no hay BD activa durante el build)
RUN mvn clean package -DskipTests

# ========================================================
# ETAPA 2: Crear la imagen de ejecución liviana
# ========================================================
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copiamos el archivo .jar generado en la etapa anterior
# Buscamos dinámicamente dentro de la carpeta del proyecto Spring Boot
COPY --from=build /app/PicasyFijasK/target/*.jar app.jar

# Exponemos el puerto estándar
EXPOSE 8080

# Comando para arrancar el juego de Picas y Fijas
ENTRYPOINT ["java", "-jar", "app.jar"]

