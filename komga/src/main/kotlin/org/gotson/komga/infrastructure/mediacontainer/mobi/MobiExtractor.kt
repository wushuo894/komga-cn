package org.gotson.komga.infrastructure.mediacontainer.mobi

import cn.hutool.core.util.ObjUtil
import org.gotson.komga.domain.model.Dimension
import org.gotson.komga.domain.model.MediaContainerEntry
import org.gotson.komga.domain.model.TypedBytes
import org.gotson.komga.infrastructure.image.ImageType
import org.rr.mobi4java.MobiReader
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.nio.file.Path


@Service
class MobiExtractor(
  @Qualifier("mobiImageType")
  private val imageType: ImageType,
) {
  fun getPages(
    path: Path,
    analyzeDimensions: Boolean,
  ): List<MediaContainerEntry> {
    val reader = MobiReader().read(path.toFile())
    val list = ArrayList<MediaContainerEntry>()
    for (i in 0 until reader.images.size) {
      list.add(MediaContainerEntry(name = "${i + 1}", dimension = Dimension(1, 1)))
    }
    return list
  }

  fun getCover(
    path: Path,
  ): TypedBytes {
    val reader = MobiReader().read(path.toFile())
    var cover = reader.cover
    if (ObjUtil.isNull(cover)) {
      cover = reader.images[0]
    }
    return TypedBytes(cover, imageType.imageIOFormat)
  }

  fun getPageContentAsImage(
    path: Path,
    pageNumber: Int,
  ): TypedBytes {
    val reader = MobiReader().read(path.toFile())
    val images = reader.images
    return TypedBytes(images[pageNumber - 1], imageType.imageIOFormat)
  }
}
