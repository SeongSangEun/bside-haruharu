package com.bigbang.haruharu.repository.concept;

import com.bigbang.haruharu.domain.entity.concept.Concept;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConceptRepository extends JpaRepository<Concept, Long> {
}
