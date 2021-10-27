package kr.bunda.user.scheduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class UserScheculer {

    @Scheduled(cron = "0 0 12 * * ?") // cron에 따라 실행
    public void scheduleRefreshToken() {
        long now = System.currentTimeMillis() / 1000;
        System.out.println(
                "schedule tasks using cron jobs - " + now);
    }

}