FROM wushuo894/eclipse-temurin:25-jre-alpine

COPY docker/run.sh /run.sh
COPY docker/exec.sh /exec.sh
COPY build/libs/komga-*.jar /usr/app/komga.jar

WORKDIR /usr/app
VOLUME /config
ENV PUID=0 PGID=0 UMASK=022
ENV KOMGA_CONFIGDIR="/config" TZ="Asia/Shanghai" CHS="FALSE"
EXPOSE 25600
RUN sed -i 's/\r//g' /*.sh && chmod +x /*.sh
CMD ["exec.sh"]
