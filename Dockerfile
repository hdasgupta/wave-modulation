#

FROM ubuntu:java

COPY ./target/Modulations-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT java -jar Modulations-0.0.1-SNAPSHOT.jar

