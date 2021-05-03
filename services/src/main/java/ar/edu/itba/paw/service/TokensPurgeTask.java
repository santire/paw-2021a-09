package ar.edu.itba.paw.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.persistence.UserDao;

@Service
@Transactional
public class TokensPurgeTask {

    @Autowired
    private UserDao userDao;

    @Scheduled(cron = "0 0 5 * * *")
    public void purgeExpired() {
      LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault());
      // All tokens created 2 days before when task is run
      // would be expired by now
      LocalDateTime expiredCreatedAt = now.minusDays(2);

      userDao.purgeAllExpiredTokensSince(expiredCreatedAt);
    }
}
