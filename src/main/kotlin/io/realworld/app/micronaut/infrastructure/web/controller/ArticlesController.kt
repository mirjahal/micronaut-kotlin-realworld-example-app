package io.realworld.app.micronaut.infrastructure.web.controller

import io.micronaut.context.annotation.Parameter
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.validation.Validated
import io.realworld.app.micronaut.domain.business.ArticleBusiness
import io.realworld.app.micronaut.infrastructure.extensions.toUUID
import io.realworld.app.micronaut.infrastructure.web.dto.ArticleDto
import java.net.URI
import java.security.Principal
import javax.validation.Valid

@Validated
@Controller("/articles")
class ArticlesController(
    private val articleBusiness: ArticleBusiness
) {

    @Post
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun create(@Body @Valid articleDto: ArticleDto.Request.Create, principal: Principal) : HttpResponse<ArticleDto.Response.Single> {
        val userId = principal.name.toUUID()
        val articleData = ArticleDto.Request.Create.toData(articleDto).let { articleData ->
            articleBusiness.save(articleData, userId)
        }

        return HttpResponse.created(
            ArticleDto.Response.Single.fromData(articleData),
            URI.create("/api/articles/${articleData.slug}")
        )
    }

    @Post("/{slug}/favorite")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun favorite(@Parameter slug: String, principal: Principal) : HttpResponse<ArticleDto.Response.Single> {
        val userId = principal.name.toUUID()
        val articleData = articleBusiness.favorite(slug, userId)

        return HttpResponse.created(
            ArticleDto.Response.Single.fromData(articleData)
        )
    }

    @Delete("/{slug}/favorite")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun unfavorite(@Parameter slug: String, principal: Principal) : HttpResponse<ArticleDto.Response.Single> {
        val userId = principal.name.toUUID()
        val articleData = articleBusiness.unfavorite(slug, userId)

        return HttpResponse.ok(
            ArticleDto.Response.Single.fromData(articleData)
        )
    }

}