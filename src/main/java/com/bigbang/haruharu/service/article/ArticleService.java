package com.bigbang.haruharu.service.article;

import com.bigbang.haruharu.domain.entity.article.Article;
import com.bigbang.haruharu.domain.entity.base.BaseEntity;
import com.bigbang.haruharu.domain.entity.like.Like;
import com.bigbang.haruharu.dto.request.article.CreateArticleRequest;
import com.bigbang.haruharu.dto.response.ApiResponse;
import com.bigbang.haruharu.repository.article.ArticleRepository;
import com.bigbang.haruharu.repository.article.ArticleRepositorySupport;
import com.bigbang.haruharu.repository.like.LikeRepository;
import com.bigbang.haruharu.service.clova.ClovaApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ClovaApiService clovaApiService;
    private final ArticleRepository articleRepository;
    private final ArticleRepositorySupport articleRepositorySupport;
    private final LikeRepository likeRepository;
    public ResponseEntity<?> createArticle(CreateArticleRequest createArticleRequest, Long userSeq) {

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

        articleRepository.save(article);

        return ResponseEntity.ok(ApiResponse.builder().check(true).information("저장에 성공하였습니다.").build());
    }

    public void changeLikeArticle(Long articleSeq, Long userSeq) {
        Like like = articleRepositorySupport.existLike(articleSeq, userSeq);

        if(ObjectUtils.isEmpty(like)) {
            createNewLike(userSeq, like);
        } else {
            //기존 삭제되어있던 데이터는 삭제해제 및 카운트 증가
            if(BaseEntity.YN.Y.equals(like.getDeleteYn())) {
                reLikeArticle(like);
            } else {
                unLikeArticle(like);
            }
        }
        likeRepository.save(like);
    }

    private static void unLikeArticle(Like like) {
        like.setDeleteYn(BaseEntity.YN.Y);
        like.getArticle().unlikeArticle();
    }

    private static void reLikeArticle(Like like) {
        like.setDeleteYn(BaseEntity.YN.N);
        like.getArticle().reLikeArticle();
    }

    private static void createNewLike(Long userSeq, Like like) {
        Like newLike = Like.builder()
                .userSeq(userSeq)
                .article(like.getArticle())
                .build();
        like.getArticle().newLikeArticle(newLike);
    }
}
