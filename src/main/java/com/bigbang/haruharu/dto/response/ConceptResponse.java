package com.bigbang.haruharu.dto.response;

import com.bigbang.haruharu.dto.entityDto.ConceptDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ConceptResponse {
    private List<ConceptDto> conceptList;
}
