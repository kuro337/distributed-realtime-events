FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

# Copy the pre-built JAR into the image
COPY ./build/libs/java-kafka-1.0-SNAPSHOT.jar /app/app.jar

# Command to run the application automatically in case of a Batch Consumer
#CMD ["java", "-jar", "/app/app.jar"]

# in order to run container and exec in
CMD ["sleep", "infinity"]

# ./gradlew fatJar

# docker build -t redpanda-java-app:latest -f Dockerfile ..
# docker run --rm -it redpanda-java-app:latest /bin/sh
# docker build -t redpanda-java-app:latest .
# docker tag redpanda-java-app:latest ghcr.io/kuro337/redpanda-java-app:latest
# docker push ghcr.io/kuro337/redpanda-java-app:latest
# k3d image import redpanda-java-app:latest -c event-streaming