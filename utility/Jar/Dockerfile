FROM openjdk:17-jdk-slim
MAINTAINER Hadows

WORKDIR /home

COPY PokemonAO-0.0.1-SNAPSHOT.jar ./PokemonAO-0.0.1-SNAPSHOT.jar
COPY unknownDataType.sql ./properties/unknownDataType.sql
COPY insertPokemonData.sql ./properties/insertPokemonData.sql


CMD ["java","-jar","./PokemonAO-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080