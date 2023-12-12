package com.bigbang.haruharu.controller;

import com.bigbang.haruharu.config.security.token.CurrentUser;
import com.bigbang.haruharu.config.security.token.UserPrincipal;
import com.bigbang.haruharu.dto.request.article.CreateArticleRequest;
import com.bigbang.haruharu.dto.request.auth.ChangePasswordRequest;
import com.bigbang.haruharu.service.article.ArticleService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<?> createArticle(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 CreateArticleRequest 를 참고해주세요.", required = true) @Valid @RequestBody CreateArticleRequest createArticleRequest
            ) {
        articleService.createArticle(createArticleRequest, userPrincipal.getId());

        return ResponseEntity.ok(true);
    }

}
