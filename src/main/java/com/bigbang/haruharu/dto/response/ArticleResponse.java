package com.bigbang.haruharu.dto.response;

import com.bigbang.haruharu.dto.entityDto.ArticleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ArticleResponse {
    List<ArticleDto> articleList;
}
