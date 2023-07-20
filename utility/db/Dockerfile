FROM mysql:5.7.15

MAINTAINER Hadows

ENV MYSQL_DATABASE=pokemon_ao_db \
    MYSQL_ROOT_PASSWORD=root

ADD init.sql /docker-entrypoint-initdb.d

EXPOSE 3306