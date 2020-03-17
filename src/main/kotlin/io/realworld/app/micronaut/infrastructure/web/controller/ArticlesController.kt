package io.realworld.app.micronaut.infrastructure.web.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
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
        val articleData = ArticleDto.Request.Create.toData(articleDto)

        val article = articleBusiness.save(articleData, userId)

        return HttpResponse.created(
            ArticleDto.Response.Single.fromData(article),
            URI.create("/api/articles/${article.slug}")
        )
    }

}