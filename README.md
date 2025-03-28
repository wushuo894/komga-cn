# ![app icon](https://github.com/gotson/komga/raw/master/.github/readme-images/app-icon.png) Komga

[![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/wushuo894/komga-cn?color=blue&label=download&sort=semver)](https://github.com/wushuo894/komga-cn/releases/latest)
[![GitHub all releases](https://img.shields.io/github/downloads/wushuo894/komga-cn/total?color=blue&label=github%20downloads)](https://github.com/wushuo894/komga-cn/releases)
[![Docker Pulls](https://img.shields.io/docker/pulls/wushuo894/komga-cn)](https://hub.docker.com/r/wushuo894/komga-cn)

在原版基础上对 MOBI 格式的漫画 做了支持

修复了中文 EPUB 问题

增强封面获取逻辑

支持中文拼音首字母索引(需要使用本镜像建立 "库")

支持繁体自动转换为简体

github: https://github.com/wushuo894/komga-cn

## docker run

```
docker run -d \
    --name komga-cn \
    -v ./config:/config \
    -m 8192m \
    -p 25600:25600  \
    -e TZ=Asia/Shanghai \
    -e CHS=TRUE \
    --restart always \
    wushuo894/komga-cn:latest
```

## docker compose

```
version: "3"
services:
  komga-cn:
    image: wushuo894/komga-cn:latest
    container_name: komga-cn
    network_mode: bridge
    mem_limit: 8192m
    ports:
      - "25600:25600" # web端口
    environment:
      - TZ=Asia/Shanghai
      - CHS=TRUE # 开启繁转简
    volumes:
      - ./config:/config # 配置文件存放位置
    restart: always
```
