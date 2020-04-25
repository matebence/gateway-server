From openjdk:8
copy ./target/gateway-server-0.0.1-SNAPSHOT.jar gateway-server-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","gateway-server-0.0.1-SNAPSHOT.jar"]