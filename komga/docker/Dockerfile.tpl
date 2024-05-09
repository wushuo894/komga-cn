FROM openjdk:17-jdk
COPY build/libs/komga-*.jar /usr/app/
WORKDIR /usr/app
VOLUME /tmp
VOLUME /config
ENV KOMGA_CONFIGDIR="/config"
ENV TZ="Asia/Shanghai"
EXPOSE 25600
CMD ["java", "-jar", "komga-*.jar"]
