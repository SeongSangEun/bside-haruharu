package com.bigbang.haruharu.service.user;

import com.bigbang.haruharu.advice.assertThat.DefaultAssert;
import com.bigbang.haruharu.config.security.token.UserPrincipal;
import com.bigbang.haruharu.domain.entity.user.User;
import com.bigbang.haruharu.dto.response.ApiResponse;
import com.bigbang.haruharu.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<?> readByUser(UserPrincipal userPrincipal){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(user);
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(user.get()).build();
        return ResponseEntity.ok(apiResponse);
    }
}
