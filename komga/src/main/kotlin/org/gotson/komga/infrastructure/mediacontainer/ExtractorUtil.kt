package org.gotson.komga.infrastructure.mediacontainer;

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.text.NumberFormat
import java.util.Objects
import java.util.function.Supplier
import javax.imageio.ImageIO

class ExtractorUtil {
  companion object {
    fun getProportionCover(images: List<ByteArray>, supplier: Supplier<ByteArray>): ByteArray {
      val imagesList = images.stream().map { image ->
        try {
          ImageIO.read(ByteArrayInputStream(image))
        } catch (e: Exception) {
          null
        }
      }.filter(Objects::nonNull).toList()

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
        val index = (1.0 * proportionList.size / 2).toInt()
        proportion = proportionList[index].toDouble()
      } catch (e: Exception) {
        return supplier.get()
      }

      // 取第一个与中位数匹配上下浮动0.1的图片当作封面
      val findFirst = imagesList.stream()
        .filter { img ->
          val width = img?.width ?: 0
          val height = img?.height ?: 0
          val pro = instance.format(1.0 * width / height).toDouble()
          pro + 0.1 >= proportion && pro - 0.1 <= proportion
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
  }
}
