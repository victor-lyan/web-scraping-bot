FROM gradle:jdk8-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon 

FROM openjdk:8-jre-alpine
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/*.jar /app/web-scraping-bot.jar

ENTRYPOINT ["sh", "-c", "java -jar /app/web-scraping-bot.jar $TELEGRAM_TOKEN"]