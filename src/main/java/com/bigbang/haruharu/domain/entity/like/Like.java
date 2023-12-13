package com.bigbang.haruharu.domain.entity.like;

import com.bigbang.haruharu.domain.entity.article.Article;
import com.bigbang.haruharu.domain.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Entity
@Table(name ="like_table")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeSeq;
    private Long userSeq;
    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;
}
