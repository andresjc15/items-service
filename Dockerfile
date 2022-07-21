FROM openjdk:11
VOLUME /tmp
EXPOSE 8888
ADD ./target/item-service-1.0.0.jar item-service-1.0.0.jar
ENTRYPOINT ["java","-jar","/item-service-1.0.0.jar"]