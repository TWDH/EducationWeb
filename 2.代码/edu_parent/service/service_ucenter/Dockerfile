FROM openjdk:11
EXPOSE 8080

VOLUME /tmp
ADD mysql8.jar /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]
