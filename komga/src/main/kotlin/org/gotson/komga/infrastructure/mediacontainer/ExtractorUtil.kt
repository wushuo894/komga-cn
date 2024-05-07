package org.gotson.komga.infrastructure.mediacontainer;

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.text.NumberFormat
import java.util.Objects
import java.util.function.Supplier
import javax.imageio.ImageIO

class ExtractorUtil {
  companion object {
    fun getProportionCover(images: List<ByteArray>, supplier: Supplier<ByteArray>): ByteArray {
      // 取前十个
      val imagesList = images.take(10).stream().map { image ->
        try {
          ImageIO.read(ByteArrayInputStream(image))
        } catch (e: Exception) {
          null
        }
      }
        .filter(Objects::nonNull)
        .filter { img -> img?.let { getImageColorPercentage(it) } == false }
        .toList()

      if (imagesList.isEmpty()) {
        return supplier.get()
      }

      val instance = NumberFormat.getInstance()
      instance.maximumFractionDigits = 2
      instance.minimumFractionDigits = 2

      val proportion: Double

      try {
        // 取中位数
        val proportionList = imagesList.stream()
          .map { img ->
            val width = img?.width ?: 0
            val height = img?.height ?: 0
            instance.format(1.0 * width / height)
          }
          .distinct()
          .sorted(Comparator.comparingDouble { v -> v.toDouble() })
          .toList()
        val average = proportionList.stream()
          .mapToDouble(String::toDouble)
          .average().asDouble

        // 与平均数数差异最小
        proportion = proportionList.stream()
          .sorted(
            Comparator.comparingDouble { v ->
              val toDouble = v.toDouble()
              if (toDouble < average) {
                average - toDouble
              }
              toDouble - average
            },
          ).findFirst().get().toDouble()
      } catch (e: Exception) {
        return supplier.get()
      }

      // 取第一个与中位数匹配上下浮动0.2的图片当作封面
      val findFirst = imagesList.stream()
        .filter { img ->
          val width = img?.width ?: 0
          val height = img?.height ?: 0
          val pro = instance.format(1.0 * width / height).toDouble()
          pro + 0.25 >= proportion && pro - 0.25 <= proportion
        }
        .findFirst()
      if (findFirst.isEmpty) {
        return supplier.get()
      }
      val get = findFirst.get()

      val ret: ByteArray
      val outputStream = ByteArrayOutputStream()
      try {
        ImageIO.write(get, "jpg", outputStream)
        outputStream.flush()
        ret = outputStream.toByteArray()
      } catch (e: Exception) {
        return supplier.get()
      } finally {
        try {
          outputStream.close()
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
      return ret
    }

    /**
     * 白色或黑色占比是否过高
     */
    private fun getImageColorPercentage(image: BufferedImage): Boolean {
      val width = image.width
      val height = image.height

      var whitePixels = 0
      var blackPixels = 0

      for (x in 0 until width) {
        for (y in 0 until height) {
          val rgb = image.getRGB(x, y)
          val red = (rgb shr 16) and 0xFF
          val green = (rgb shr 8) and 0xFF
          val blue = rgb and 0xFF

          if (red == 255 && green == 255 && blue == 255) {
            whitePixels++
          }

          if (red == 0 && green == 0 && blue == 0) {
            blackPixels++
          }
        }
      }

      val totalPixels = width * height
      val whitePercentage = whitePixels.toDouble() / totalPixels * 100
      val blackPercentage = blackPixels.toDouble() / totalPixels * 100

      return whitePercentage >= 80 || blackPercentage >= 80
    }
  }

}
