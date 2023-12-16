package com.bigbang.haruharu.domain.entity.concept;

import com.bigbang.haruharu.domain.entity.article.Article;
import com.bigbang.haruharu.domain.entity.base.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@DynamicUpdate
public class Concept extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conceptSeq;
    @Column(length = 1000)
    private String name;

    @Column(length = 3000)
    private String systemScript;
    @Column(length = 3000)
    private String userScript;
    //    private String languageModel;
    private Double topP;

    private String suffixScript;
    private Integer topK;
    private Integer maxTokens;
    private Double temperature;
    private Double repeatPenalty;
}
