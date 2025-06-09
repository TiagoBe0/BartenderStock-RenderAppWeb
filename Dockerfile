# ----- Etapa 1: Compilación (Builder) -----
# Usamos la misma imagen base
FROM maven:3.9.6-eclipse-temurin-11 AS builder

# Establecemos el directorio de trabajo
WORKDIR /app

# 1. Copiamos solo el pom.xml
COPY pom.xml .

# 2. Creamos una capa SOLO para las dependencias. 
# Esta capa solo se reconstruirá si el pom.xml cambia.
RUN mvn dependency:go-offline

# 3. Ahora copiamos el código fuente.
# Si solo cambias el código, Docker reutilizará la capa de dependencias de arriba.
COPY src ./src

# 4. Compilamos y empaquetamos. 'clean' no es necesario en el entorno limpio de Docker.
# Maven usará las dependencias ya descargadas en el paso anterior.
RUN mvn package -DskipTests

# ----- Etapa 2: Ejecución (Runtime) -----
# La segunda etapa se mantiene igual, es perfecta.
FROM eclipse-temurin:11-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/SpringUsoSesionesUsuario-0.0.1-SNAPSHOT.war app.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.war"]
