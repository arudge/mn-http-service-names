FROM openjdk:14-alpine
COPY build/libs/mn-http-service-name-*-all.jar mn-http-service-name.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "mn-http-service-name.jar"]