FROM openjdk:8
COPY ./target/gateway-server-0.0.1-SNAPSHOT.jar gateway-server-0.0.1-SNAPSHOT.jar
COPY ./wait-for-it.sh wait-for-it.sh 
RUN chmod +x wait-for-it.sh
CMD ["./wait-for-it.sh" , "config-server:8888" , "--strict" , "--timeout=105" , "--" , "java", "-jar", "gateway-server-0.0.1-SNAPSHOT.jar"]