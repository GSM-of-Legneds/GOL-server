package dev.yangsijun.gol.common.common.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

abstract class BaseTimeEntity(
    @CreatedDate var createdDate: LocalDateTime? = null,
    @LastModifiedDate var modifiedDate: LocalDateTime? = null
)
