package zerobase.weather.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.service.DiaryService;
import zerobase.weather.service.SchedulerService;

@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {
    private final DiaryService diaryService;
    @Override
    @Transactional
    @Scheduled(cron = "0 0 1 * * *")    //[초, 분, 시, 일, 월, 년]
    public void saveWeatherDate() {
        diaryService.saveWeatherDate();
    }
}
