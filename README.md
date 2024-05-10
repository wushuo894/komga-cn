# ![app icon](https://github.com/gotson/komga/raw/master/.github/readme-images/app-icon.png) Komga

在原版基础上对 MOBI 格式的漫画 做了支持

修复了中文 EPUB 问题

增强封面获取逻辑

支持中文拼音首字母索引(需要使用本镜像建立 "库")

支持繁体自动转换为简体

github: https://github.com/wushuo894/komga-cn

`docker run -d \
  --name komga-cn \
  -v ./tmp:/tmp \
  -v ./config:/config \
  -p 25600:25600 \
  -e TZ=Asia/Shanghai \
  --restart always \
  wushuo894/komga-cn`


开启繁转简:

`docker run -d \
--name komga-cn \
-v ./tmp:/tmp \
-v ./config:/config \
-p 25600:25600 \
-e TZ=Asia/Shanghai \
-e CHS=TRUE \
--restart always \
wushuo894/komga-cn`
