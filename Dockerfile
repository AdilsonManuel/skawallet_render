
# Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
# Click nbfs://nbhost/SystemFileSystem/Templates/Other/Dockerfile to edit this template

FROM alpine:latest

CMD ["/bin/sh"]

# Use a imagem base do OpenJDK
FROM openjdk:17-jdk-slim

# Defina o diretório de trabalho
WORKDIR /app

# Copie o arquivo JAR para o contêiner
COPY target/skawallet-01.jar app.jar

# Exponha a porta que o aplicativo vai rodar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]

