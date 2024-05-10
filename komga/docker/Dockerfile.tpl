FROM openjdk:17-jdk
COPY build/libs/komga-*.jar /usr/app/komga.jar
WORKDIR /usr/app
VOLUME /tmp
VOLUME /config
ENV KOMGA_CONFIGDIR="/config"
ENV TZ="Asia/Shanghai"
ENV CHS="FALSE"
EXPOSE 25600
CMD ["java", "-jar", "komga.jar"]
