FROM eclipse-temurin:26-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "target/Coursework_Deadline_Optimiser-0.0.1-SNAPSHOT.jar"]