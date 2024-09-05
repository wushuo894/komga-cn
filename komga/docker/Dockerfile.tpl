FROM eclipse-temurin:17-jre
COPY build/libs/komga-*.jar /usr/app/komga.jar
WORKDIR /usr/app
VOLUME /tmp
VOLUME /config
ENV KOMGA_CONFIGDIR="/config"
ENV TZ="Asia/Shanghai"
ENV CHS="FALSE"
EXPOSE 25600
RUN mkdir /usr/java
RUN ln -s /opt/java/openjdk /usr/java/openjdk-17
CMD ["java", "-jar", "komga.jar"]
