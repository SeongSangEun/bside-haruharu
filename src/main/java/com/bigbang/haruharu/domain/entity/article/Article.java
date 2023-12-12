package com.bigbang.haruharu.domain.entity.article;

import com.bigbang.haruharu.domain.entity.base.BaseEntity;
import com.bigbang.haruharu.domain.entity.concept.Concept;
import com.bigbang.haruharu.domain.entity.like.Like;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleSeq;
    @Column(length = 1000)
    private String subject;
    @Column(length = 3000)
    private String originText;
    @Column(length = 3000)
    private String convertedText;
    @Column(length = 500)
    private String imageUrl;
    @Column(length = 1000)
    private String hashTagSet;
    private Integer likeCount;
    private Long conceptSeq;
    @OneToMany(mappedBy = "likeSeq")
    @Setter
    private List<Like> likeList;
    private Long userSeq;

    public void newLikeArticle(Like newLike) {
        this.likeList.add(newLike);
        this.likeCount ++;
    }
    public void unlikeArticle() {
        this.likeCount --;
    }
    public void reLikeArticle() {
        this.likeCount ++;
    }
}
