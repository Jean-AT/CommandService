FROM maven:3.8-eclipse-temurin-17 AS builder

WORKDIR /app
COPY . .

# OJO: Aquí usamos 'mvn' directo (del sistema), no './mvnw'
RUN mvn clean package -DskipTests

# --- Etapa Final (Igual que antes) ---
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 1403
ENTRYPOINT ["java","-jar","app.jar"]