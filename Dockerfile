FROM openjdk:8-jdk-alpine
ADD /build/libs/Greetings-Elite-Discord-Bot-all.jar run.jar
ENTRYPOINT ["java", "-jar", "run.jar"]