FROM openjdk:8-alpine
COPY ./build/libs/app.jar /
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
CMD ["--spring.profiles.active=prod"]
