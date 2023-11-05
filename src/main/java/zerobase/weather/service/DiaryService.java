package zerobase.weather.service;

import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DiaryService {
    void createDiary(LocalDate date, String text);

    //(API 에서 가져오기? or DB 에서 가져오기?)
    DateWeather getDateWeather(LocalDate date);

    List<Diary> readDiary(LocalDate date);

    List<Diary> readDiaries(LocalDate startDate, LocalDate endDate);

    void updateDiary(LocalDate date, String text);

    void deleteDiary(LocalDate date);

    Map<String, Object> parseWeather(String jsonString);

    void saveWeatherDate();

    DateWeather getWeatherFromApi();

    DateWeather getWeatherFromApi(LocalDate date);
}
