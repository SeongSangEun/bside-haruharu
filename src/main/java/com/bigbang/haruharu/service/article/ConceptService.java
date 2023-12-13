package com.bigbang.haruharu.service.article;

import com.bigbang.haruharu.dto.entityDto.ConceptDto;
import com.bigbang.haruharu.dto.response.ApiResponse;
import com.bigbang.haruharu.dto.response.ConceptResponse;
import com.bigbang.haruharu.repository.concept.ConceptRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConceptService {
    private final ConceptRepositorySupport conceptRepositorySupport;

    public ResponseEntity<?> findConceptList() {
        List<ConceptDto> conceptList = conceptRepositorySupport.findConceptList();

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information(new ConceptResponse(conceptList))
                .build();

        return ResponseEntity.ok(response);
    }

}
