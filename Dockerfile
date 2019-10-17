FROM java:8

COPY build/libs /app/lib
COPY build/libs/grpc-java-demo-1.0-SNAPSHOT.jar /app/grpc-java-demo.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app/grpc-java-demo.jar"]

