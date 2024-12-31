# Use uma imagem base do JDK
FROM openjdk:17-jdk-slim

# Define o nome do arquivo JAR como argumento
ARG JAR_FILE

# Copia o arquivo JAR gerado para a imagem
COPY target/${JAR_FILE} app.jar

# Define o comando de inicialização
ENTRYPOINT ["java", "-jar", "/app.jar"]
