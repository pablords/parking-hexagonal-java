# Use uma imagem base do JDK
FROM openjdk:17-jdk-slim

# Copia o arquivo JAR gerado para a imagem
COPY target/parking-application.jar app.jar

# Define o comando de inicialização
ENTRYPOINT ["java", "-jar", "/app.jar"]
