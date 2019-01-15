# docker build -t seyun95/mocha .

FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 80
COPY . /
ENTRYPOINT ["java", "-XX:+UseParallelGC", "-jar",  "/mocha.jar"]
