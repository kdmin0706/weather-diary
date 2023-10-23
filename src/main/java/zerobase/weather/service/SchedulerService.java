package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final DiaryService diaryService;

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")    //[초, 분, 시, 일, 월, 년]
    public void saveWeatherDate() {
        diaryService.saveWeatherDate();
    }
}
