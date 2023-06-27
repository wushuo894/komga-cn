package org.gotson.komga.domain.model

import java.time.LocalDateTime

class PageHashKnown(
  hash: String,
  size: Long? = null,
  val action: Action,
  val deleteCount: Int = 0,

  override val createdDate: LocalDateTime = LocalDateTime.now(),
  override val lastModifiedDate: LocalDateTime = createdDate,
) : Auditable, PageHash(hash, size) {
  enum class Action {
    DELETE_AUTO,
    DELETE_MANUAL,
    IGNORE,
  }

  fun copy(
    hash: String = this.hash,
    size: Long? = this.size,
    action: Action = this.action,
    deleteCount: Int = this.deleteCount,
  ) = PageHashKnown(
    hash = hash,
    size = size,
    action = action,
    deleteCount = deleteCount,
  )
}
