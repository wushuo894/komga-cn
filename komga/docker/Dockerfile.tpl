FROM openjdk:17-jdk
COPY build/libs/komga-*.jar /usr/app/
COPY docker/run.sh /usr/app/
WORKDIR /usr/app
VOLUME /tmp
VOLUME /config
ENV KOMGA_CONFIGDIR="/config"
ENV TZ="Asia/Shanghai"
ENV CHS="FALSE"
EXPOSE 25600
CMD ["bash", "/usr/app/run.sh"]
