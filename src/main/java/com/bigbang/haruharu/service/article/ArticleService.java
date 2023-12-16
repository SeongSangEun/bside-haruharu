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
import com.bigbang.haruharu.util.DistributeLock;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ClovaApiService clovaApiService;
    private final ArticleRepository articleRepository;
    private final ArticleRepositorySupport articleRepositorySupport;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    @DistributeLock(key = "#key", waitTime = 60L, leaseTime = 55L)
    public ResponseEntity<?> createArticle(String key, CreateArticleRequest createArticleRequest, Long userSeq) {

        //1. 오늘 쓴 글이 이미 작성되어있는지 확인 -> 있으면 하루에 글 작성은 1개 (삭제 후 재 생성 가능)
        //2. 하루 글 생성갯수 제한 -> dailyCount가 0인 경우 글 작성 불가
        User user1 = userRepository.findById(userSeq)
                .orElseThrow(
                        () -> new DefaultException(ErrorCode.INVALID_AUTHENTICATION)
                );
        if(!user1.canCreateArticle()) {
            throw new DefaultException(ErrorCode.OVER_DAILYCOUNT);
        }

        articleRepositorySupport.getArticleInToday(userSeq)
                .ifPresent(
                    a -> {throw new DefaultException(ErrorCode.EXIST_TODAY_ARTICLE);}
                );

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
    public ResponseEntity<?> changeArticleSubject(Long articleSeq, Long userSeq, String changedSubject) {
        Article article = articleRepositorySupport.findByUserSeqAndArticleSeq(userSeq, articleSeq);

        if(ObjectUtils.isEmpty(article)) {
            throw new DefaultException(ErrorCode.INVALID_ARTICLE);
        }
        String subject = StringUtils.hasText(changedSubject)
                ? changedSubject
                : "";

        article.updateSubject(subject);
        articleRepository.save(article);

        return ResponseEntity.ok(ApiResponse.builder().check(true).information("저장에 성공하였습니다.").build());
    }

    @DistributeLock(key = "#key", waitTime = 10L, leaseTime = 8L)
    public ResponseEntity<?> changeLikeArticle(String key, Long articleSeq, Long userSeq) {
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
