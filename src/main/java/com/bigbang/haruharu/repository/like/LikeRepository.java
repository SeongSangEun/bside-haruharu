package com.bigbang.haruharu.repository.like;

import com.bigbang.haruharu.domain.entity.like.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
