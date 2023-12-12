package com.bigbang.haruharu.scheduler;

import com.bigbang.haruharu.domain.entity.user.User;
import com.bigbang.haruharu.repository.user.UserRepository;
import com.bigbang.haruharu.repository.user.UserRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserScheduler {
    private final UserRepository userRepository;
    private final UserRepositorySupport userRepositorySupport;

    @Scheduled(cron = "1 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void resetDailyCount() {

        List<User> userList = userRepositorySupport.findAllWithNonDefaultDailyCount();

        userList.stream().forEach(user -> user.resetDailyCount());

        userRepository.saveAll(userList);
    }
}
