FROM amazoncorretto:21-alpine3.19

ADD target/scala-3.3.1/calculator-assembly-0.1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
