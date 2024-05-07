package org.gotson.komga.infrastructure.mediacontainer.mobi

import org.apache.pdfbox.pdmodel.PDPage
import org.gotson.komga.domain.model.Dimension
import org.gotson.komga.domain.model.MediaContainerEntry
import org.gotson.komga.domain.model.MediaType
import org.gotson.komga.domain.model.TypedBytes
import org.gotson.komga.infrastructure.image.ImageType
import org.rr.mobi4java.MobiReader
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.file.Path
import java.util.Objects
import java.util.concurrent.atomic.AtomicInteger
import javax.imageio.ImageIO
import kotlin.math.roundToInt


@Service
class MobiExtractor(
  @Qualifier("mobiImageType")
  private val imageType: ImageType,
  @Qualifier("mobiResolution")
  private val resolution: Float,
) {
  fun getPages(
    path: Path,
    analyzeDimensions: Boolean,
  ): List<MediaContainerEntry> {
    val reader = MobiReader().read(path.toFile())
    val index = AtomicInteger(0)
    return reader.images.map { image ->
      try {
        ImageIO.read(ByteArrayInputStream(image))
      } catch (e: Exception) {
        null
      }
    }.filter { imageIO -> Objects.nonNull(imageIO) }
      .map { imageIO ->
        val dimension = if (analyzeDimensions) Dimension(imageIO!!.width, imageIO.height) else null
        MediaContainerEntry(name = "${index.incrementAndGet()}", dimension = dimension)
      }
  }

  fun getPageContentAsImage(
    path: Path,
    pageNumber: Int,
  ): TypedBytes {
    val reader = MobiReader().read(path.toFile())
    val images = reader.images
    return TypedBytes(images[pageNumber], MediaType.MOBI.type)
  }

  fun getPageContentAsPdf(
    path: Path,
    pageNumber: Int,
  ): TypedBytes {
    val reader = MobiReader().read(path.toFile())
    val images = reader.images
    return TypedBytes(images[pageNumber], MediaType.MOBI.type)
  }

  fun getCover(path: Path): TypedBytes? {
    val reader = MobiReader().read(path.toFile())
    val images = reader.images.stream().map { image ->
      try {
        ImageIO.read(ByteArrayInputStream(image))
      } catch (e: Exception) {
        null
      }
    }.filter(Objects::nonNull).toList()

    if (images.isEmpty()) {
      return getPageContentAsImage(path, 1)
    }

    val widthMap = mutableMapOf<Int, Int>()
    val heightMap = mutableMapOf<Int, Int>()

    for (image in images) {
      val width = image?.width ?: 0
      val height = image?.height ?: 0

      val countWidth = widthMap.getOrDefault(width, 0)
      val countHeight = heightMap.getOrDefault(height, 0)

      widthMap[width] = countWidth + 1
      heightMap[height] = countHeight + 1
    }

    val avgWidth = widthMap.entries
      .stream()
      .sorted(Comparator.comparingInt { (_, v) -> v })
      .map { (k, _) -> k }
      .findFirst()
      .get()

    val avgHeight = heightMap.entries
      .stream()
      .sorted(Comparator.comparingInt { (_, v) -> v })
      .map { (k, _) -> k }
      .findFirst()
      .get()


    val findFirst = images.stream()
      .filter { img -> img?.width == avgWidth }
      .filter { img -> img?.height == avgHeight }
      .findFirst()
    if (findFirst.isEmpty) {
      return getPageContentAsImage(path, 1)
    }
    val get = findFirst.get()

    var ret: TypedBytes
    val outputStream = ByteArrayOutputStream()
    try {
      ImageIO.write(get, "jpg", outputStream)
      outputStream.flush()
      val bytes = outputStream.toByteArray()
      ret = TypedBytes(
        bytes,
        MediaType.MOBI.type,
      )
    } catch (e: Exception) {
      ret = getPageContentAsImage(path, 1)
    } finally {
      try {
        outputStream.close()
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
    return ret
  }

  private fun PDPage.getScale() = getScale(cropBox.width, cropBox.height)

  private fun getScale(
    width: Float,
    height: Float,
  ) = resolution / minOf(width, height)

  fun scaleDimension(dimension: Dimension): Dimension {
    val scale = getScale(dimension.width.toFloat(), dimension.height.toFloat())
    return Dimension((dimension.width * scale).roundToInt(), (dimension.height * scale).roundToInt())
  }
}
