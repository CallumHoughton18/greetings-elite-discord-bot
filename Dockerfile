FROM openjdk:8-jdk-alpine
ARG BOT_TOKEN
ENV BOT_TOKEN ${BOT_TOKEN}
ADD /build/libs/Greetings-Elite-Discord-Bot.jar run.jar
RUN echo $BOT_TOKEN
ENTRYPOINT ["java", "-jar", "run.jar"]