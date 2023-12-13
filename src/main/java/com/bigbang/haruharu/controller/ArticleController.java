package com.bigbang.haruharu.controller;

import com.bigbang.haruharu.config.security.token.CurrentUser;
import com.bigbang.haruharu.config.security.token.UserPrincipal;
import com.bigbang.haruharu.dto.request.article.CreateArticleRequest;
import com.bigbang.haruharu.dto.request.auth.ChangePasswordRequest;
import com.bigbang.haruharu.service.article.ArticleService;
import com.bigbang.haruharu.service.article.ConceptService;
import com.bigbang.haruharu.service.s3.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final UploadService uploadService;
    private final ConceptService conceptService;

    @Operation(summary = "글 작성", description = "글 작성 api 입니다.")
    @PostMapping
    public ResponseEntity<?> createArticle(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 CreateArticleRequest 를 참고해주세요.", required = true) @Valid @RequestBody CreateArticleRequest createArticleRequest
            ) {
        return articleService.createArticle(createArticleRequest, userPrincipal.getId());
    }

    @Operation(summary = "글 삭제", description = "글 삭제 api 입니다.")
    @PutMapping
    public ResponseEntity<?> deleteArticle(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "ArticleSeq를 입력해주세요.", required = true) @PathVariable(name = "articleSeq") Long articleSeq
    ) {
        return articleService.deleteArticle(articleSeq, userPrincipal.getId());
    }

    @Operation(summary = "이미지 저장", description = "이미지 저장 후 url 발급, 글 저장시 imageUrl부분에 해당 url 기입")
    @GetMapping("/upload/image")
    public ResponseEntity<?> uploadImageFile(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @RequestParam MultipartFile multipartFile
    ) {
        return uploadService.imageUpload(multipartFile);
    }

    @Operation(summary = "컨셉 목록들을 가져옵니다.", description = "유효한 컨셉명, 컨셉 seq를 획득 후 글쓸때 컨셉 seq 전달 용도로 사용")
    @GetMapping("/concepts")
    public ResponseEntity<?> findConceptList(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return conceptService.findConceptList();
    }

    @Operation(summary = "좋아요 상태를 변경합니다.", description = "좋아요 상태를 변경합니다.")
    @PutMapping("/like/{articleSeq}")
    public ResponseEntity<?> changeLikeStatusArticle(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "ArticleSeq를 입력해주세요.", required = true) @PathVariable(name = "articleSeq") Long articleSeq
    ) {
        return articleService.changeLikeArticle(articleSeq, userPrincipal.getId());
    }

    @Operation(summary = "내가 쓴 삭제되지 않은 글들을 가져옵니다.", description = "삭제되지 않은 내가 쓴 글 불러오기")
    @GetMapping("/me")
    public ResponseEntity<?> getMyArticles(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        //todo 페이징, 정렬 추가

        return articleService.getMyArticles(userPrincipal);
    }
    @Operation(summary = "삭제되지 않은 랜덤한 글들을 가져옵니다.", description = "삭제되지 않은 내가 쓴 글 불러오기")
    @GetMapping("/random")
    public ResponseEntity<?> getRandomArticles(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        //todo 한번에 불러오는 수 확인
        return articleService.getRandomArticles(userPrincipal);
    }



}
