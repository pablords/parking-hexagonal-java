

FROM eclipse-temurin:17-jdk-jammy


WORKDIR /app

RUN apt-get update && \
    apt-get install -y inotify-tools dos2unix

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN sed -i 's/\r$//' mvnw 
RUN chmod +x ./mvnw 
RUN export MAVEN_OPTS='-Xmx512m -XX:MaxPermSize=128m'

COPY src ./src
COPY run.sh ./run.sh

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run"]