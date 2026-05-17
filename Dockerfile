# ========================================================
# ETAPA 1: Compilación (Necesita Maven)
# ========================================================
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos todos los archivos del repositorio
COPY . .

# Entramos a la subcarpeta correcta donde está el pom.xml
WORKDIR /app/PicasyFijasK

# Damos permisos de ejecución al wrapper compilador de Maven
RUN chmod +x mvnw

# Compilamos omitiendo los tests para evitar problemas de BD en Render
RUN ./mvnw clean package -DskipTests

# ========================================================
# ETAPA 2: Servidor de Ejecución (Liviano y Seguro)
# ========================================================
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copiamos el archivo JAR compilado en la etapa 1 y lo renombramos fijamente a 'app.jar'
COPY --from=build /app/PicasyFijasK/target/*.jar app.jar

# Exponemos el puerto de escucha
EXPOSE 8080

# Comando definitivo sin asteriscos para evitar errores de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]


