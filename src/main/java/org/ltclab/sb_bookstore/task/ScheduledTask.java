package org.ltclab.sb_bookstore.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class ScheduledTask {
//    @Scheduled(fixedRate = 1000)
    @Scheduled(cron = "30 7 15 9 3 ?")
    public void printMessage() {
        log.info(String.valueOf(new Date()));
    }
}
