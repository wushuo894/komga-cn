package org.gotson.komga.infrastructure.util

import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.StrUtil
import com.hankcs.hanlp.HanLP
import org.apache.commons.compress.archivers.zip.ZipFile
import org.gotson.komga.domain.model.EntryNotFoundException
import java.net.URLDecoder
import java.nio.file.Path

inline fun <R> ZipFile.Builder.use(block: (ZipFile) -> R) = this.get().use(block)

fun getZipEntryBytes(
  path: Path,
  entryName: String,
): ByteArray {
  // fast path. Only read central directory record and try to find entry in it
  val zipBuilder =
    ZipFile.builder()
      .setPath(path)
      .setUseUnicodeExtraFields(true)
      .setIgnoreLocalFileHeader(true)
  var bytes = zipBuilder.use { it.getEntryBytes(entryName) }
  if (bytes == null) {
    bytes = zipBuilder.use { it.getEntryBytes(URLDecoder.decode(entryName, "UTF-8")) }
  }
  if (bytes != null) {
    val extName = FileUtil.extName(entryName)

    if (StrUtil.isBlank(extName)) {
      return bytes
    }

    if (!listOf("html", "txt").contains(extName)) {
      return bytes
    }

    // 转换为简体
    val chs = System.getenv().getOrDefault("CHS", "FALSE")
    if ("TRUE" == chs.trim().uppercase()) {
      bytes = HanLP.convertToSimplifiedChinese(String(bytes, Charsets.UTF_8)).toByteArray(Charsets.UTF_8)
    }
    return bytes
  }


  // slow path. Entry with that name wasn't in central directory record
  // Iterate each entry and, if present, set name from Unicode extra field in local file header
  return zipBuilder.setIgnoreLocalFileHeader(false).use {
    it.getEntryBytes(entryName)
      ?: throw EntryNotFoundException("Entry does not exist: $entryName")
  }
}

private fun ZipFile.getEntryBytes(entryName: String) =
  this.use { zip ->
    zip.getEntry(entryName)?.let { entry ->
      zip.getInputStream(entry).use { it.readBytes() }
    }
  }
