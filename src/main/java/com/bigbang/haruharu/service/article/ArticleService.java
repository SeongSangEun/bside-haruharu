package com.bigbang.haruharu.service.article;

import com.bigbang.haruharu.advice.error.DefaultException;
import com.bigbang.haruharu.advice.payload.ErrorCode;
import com.bigbang.haruharu.config.security.token.UserPrincipal;
import com.bigbang.haruharu.domain.entity.article.Article;
import com.bigbang.haruharu.domain.entity.base.BaseEntity;
import com.bigbang.haruharu.domain.entity.like.Like;
import com.bigbang.haruharu.domain.entity.user.User;
import com.bigbang.haruharu.dto.entityDto.ArticleDto;
import com.bigbang.haruharu.dto.request.article.CreateArticleRequest;
import com.bigbang.haruharu.dto.response.ApiResponse;
import com.bigbang.haruharu.dto.response.ArticleResponse;
import com.bigbang.haruharu.repository.article.ArticleRepository;
import com.bigbang.haruharu.repository.article.ArticleRepositorySupport;
import com.bigbang.haruharu.repository.like.LikeRepository;
import com.bigbang.haruharu.repository.user.UserRepository;
import com.bigbang.haruharu.service.clova.ClovaApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ClovaApiService clovaApiService;
    private final ArticleRepository articleRepository;
    private final ArticleRepositorySupport articleRepositorySupport;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    public ResponseEntity<?> createArticle(CreateArticleRequest createArticleRequest, Long userSeq) {

        //todo dailyCount에 따른 생성횟수 제한
        //todo 하루 글 생성갯수 제한 -> 이미 가지고 있는 글이 있다면 등록안되게끔


        //todo conceptId에 따른 컨셉별 callClovaApi 호출 서비스 다양화
        //todo 제목 어떻게 할건지 정해지면 호출 서비스 혹은 기입
        String convertedText = clovaApiService.callClovaApi(createArticleRequest.getInputMessage());
        String subject = clovaApiService.callClovaApi(createArticleRequest.getInputMessage());

        Article article = Article.builder()
                .userSeq(userSeq)
                .subject(subject)
                .originText(createArticleRequest.getInputMessage())
                .convertedText(convertedText)
                .imageUrl(createArticleRequest.getImageUrl())
                .hashTagSet("")
                .likeCount(0)
                .conceptSeq(createArticleRequest.getConceptSeq())
                .build();

        User user = userRepository.findById(userSeq).get();
        user.minusDailyCount();

        userRepository.save(user);
        articleRepository.save(article);

        return ResponseEntity.ok(ApiResponse.builder().check(true).information("저장에 성공하였습니다.").build());
    }

    public ResponseEntity<?> deleteArticle(Long articleSeq, Long userSeq) {

        Article article = articleRepositorySupport.findByUserSeqAndArticleSeq(userSeq, articleSeq);

        if(ObjectUtils.isEmpty(article)) {
            throw new DefaultException(ErrorCode.INVALID_ARTICLE);
        }

        article.deleteArticle();
        articleRepository.save(article);

        return ResponseEntity.ok(ApiResponse.builder().check(true).information("저장에 성공하였습니다.").build());
    }

    public ResponseEntity<?> changeLikeArticle(Long articleSeq, Long userSeq) {
        Like like = articleRepositorySupport.existLike(articleSeq, userSeq);

        if(ObjectUtils.isEmpty(like)) {
            like = createNewLike(userSeq, articleSeq);
        } else {
            //기존 삭제되어있던 데이터는 삭제해제 및 카운트 증가
            if(BaseEntity.YN.Y.equals(like.getDeleteYn())) {
                reLikeArticle(like);
            } else {
                unLikeArticle(like);
            }
        }
        likeRepository.save(like);

        return ResponseEntity.ok(ApiResponse.builder().check(true).information("변경에 성공하였습니다.").build());
    }

    public ResponseEntity<?> getMyArticles(UserPrincipal userPrincipal) {
        List<ArticleDto> myArticles = articleRepositorySupport.getMyArticles(userPrincipal.getId());

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information(new ArticleResponse(myArticles))
                .build();

        return ResponseEntity.ok(response);
    }
    public ResponseEntity<?> getRandomArticles(UserPrincipal userPrincipal) {
        List<ArticleDto> randomArticles = articleRepositorySupport.getRandomArticles(userPrincipal.getId());

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information(new ArticleResponse(randomArticles))
                .build();

        return ResponseEntity.ok(response);
    }

    private void unLikeArticle(Like like) {
        like.setDeleteYn(BaseEntity.YN.Y);
        like.getArticle().unlikeArticle();
        articleRepository.save(like.getArticle());
    }

    private void reLikeArticle(Like like) {
        like.setDeleteYn(BaseEntity.YN.N);
        like.getArticle().likeArticle();
        articleRepository.save(like.getArticle());
    }

    private Like createNewLike(Long userSeq, Long articleSeq) {
        Article article = articleRepository.findById(articleSeq).orElseThrow(
                () -> new DefaultException(ErrorCode.INVALID_ARTICLE));
        article.likeArticle();
        return Like.builder()
                .userSeq(userSeq)
                .article(article)
                .build();
    }
}
