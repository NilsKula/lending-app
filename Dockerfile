FROM gradle:5.3.1-jdk11

VOLUME /tmp

COPY . /home/gradle/lending-app
WORKDIR /home/gradle/lending-app

ENTRYPOINT ["gradle"]
