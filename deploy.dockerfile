
# Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
# Click nbfs://nbhost/SystemFileSystem/Templates/Other/Dockerfile to edit this template

FROM alpine:latest

CMD ["/bin/sh"]

FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y 
COPY . .

RUN apt-get install maven -y
RUN mvn clean install 

FROM open-jdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /target/deploy_render-1.0.0.jar skawalletAPI.jar

ENTRYPOINT ["java", "-jar", "skawalletAPI.jar"]