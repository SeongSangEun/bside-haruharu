package com.bigbang.haruharu.domain.entity.concept;

import com.bigbang.haruharu.domain.entity.article.Article;
import com.bigbang.haruharu.domain.entity.base.BaseEntity;

import javax.persistence.*;

@Entity
public class Concept extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conceptSeq;
    @Column(length = 1000)
    private String name;
    @Column(length = 3000)
    private String script;
}
