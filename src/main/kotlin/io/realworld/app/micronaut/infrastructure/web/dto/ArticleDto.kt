package io.realworld.app.micronaut.infrastructure.web.dto

import com.fasterxml.jackson.annotation.JsonRootName
import io.realworld.app.micronaut.application.data.ArticleData
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

@JsonRootName("article")
sealed class ArticleDto {

    sealed class Request : ArticleDto() {
        data class Create(
            @get:NotBlank val title: String,
            @get:NotBlank val description: String,
            @get:NotBlank val body: String,
            val tagList: List<String> = mutableListOf()
        ) : Request() {
            companion object {
                fun toData(articleDto: Create) : ArticleData {
                    return ArticleData(
                        title = articleDto.title,
                        description = articleDto.description,
                        body = articleDto.body,
                        tagList = articleDto.tagList,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )
                }
            }
        }
    }

    sealed class Response : ArticleDto() {
        data class Single(
            val slug: String,
            val title: String,
            val description: String,
            val body: String,
            val tagList: List<String>,
            val createdAt: LocalDateTime,
            val updatedAt: LocalDateTime,
            val favorited: Boolean,
            val favoritesCount: Int,
            val author: ProfileDto.Response
        ) : Response() {
            companion object {
                fun fromData(articleData: ArticleData) : Single {
                    return Single(
                        slug = articleData.slug,
                        title = articleData.title,
                        description = articleData.description,
                        body = articleData.body,
                        tagList = articleData.tagList,
                        createdAt = articleData.createdAt,
                        updatedAt = articleData.updatedAt,
                        favorited = articleData.favorited,
                        favoritesCount = articleData.favoritesCount,
                        author = ProfileDto.Response.fromData(articleData.author!!)
                    )
                }
            }
        }
    }

}
