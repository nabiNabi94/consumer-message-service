FROM openjdk:11
WORKDIR /code
COPY . /code
VOLUME /tmp
EXPOSE 9080
ADD pom.xml /code/pom.xml
ADD src /code/src
ARG JAR_FILE=target/*.jar
ADD target/*.jar consumerapp
COPY ${JAR_FILE} /code/consumer.jar
CMD ["--rm -v --changeLogFile=db/changelog/changelog-master.xml update"]
ENTRYPOINT ["java","-jar","consumerapp"]
