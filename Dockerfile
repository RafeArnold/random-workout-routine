FROM openjdk:11-alpine
COPY ./jar/app.jar /
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
CMD ["--spring.profiles.active=prod"]
