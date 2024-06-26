package org.gotson.komga.infrastructure.mediacontainer.mobi

import org.apache.pdfbox.pdmodel.PDPage
import org.gotson.komga.domain.model.Dimension
import org.gotson.komga.domain.model.MediaContainerEntry
import org.gotson.komga.domain.model.MediaType
import org.gotson.komga.domain.model.TypedBytes
import org.gotson.komga.infrastructure.image.ImageType
import org.gotson.komga.infrastructure.mediacontainer.ExtractorUtil
import org.rr.mobi4java.MobiReader
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
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

  /**
   * 获取封面
   */
  fun getCover(path: Path): TypedBytes? {
    val reader = MobiReader().read(path.toFile())
    val byteArrays = reader.images
    return TypedBytes(
      ExtractorUtil.getProportionCover(byteArrays) { byteArrays[0] },
      MediaType.MOBI.type,
    )
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
