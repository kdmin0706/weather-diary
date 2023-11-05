package zerobase.weather.service.impl;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.exception.InvalidDateException;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;
import zerobase.weather.service.ApiService;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;
    private final ApiService apiService;

    private static final Logger logger = LoggerFactory.getLogger(DiaryServiceImpl.class);

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text) {
        logger.info("started to create diary");
        //날씨 데이터 가져오기
        DateWeather dateWeather = getDateWeather(date);

        //파싱된 데이터 + 일기 값 DB에 넣기
        Diary diary = new Diary();
        diary.setDateWeather(dateWeather);
        diary.setText(text);

        diaryRepository.save(diary);
        logger.info("end to create diary");
    }

    //(API 에서 가져오기? or DB 에서 가져오기?)
    @Override
    public DateWeather getDateWeather(LocalDate date) {
        List<DateWeather> dateWeatherList = dateWeatherRepository.findAllByDate(date);

        //새로 api에서 날씨 정보를 가져와야한다.
        //정책 상으로, 현재 날씨를 가져오도록 하거나, 날씨없이 일기를 쓰도록 하는 방법
        if (dateWeatherList.isEmpty()) {
            return getWeatherFromApi(date); //오버로딩을 사용하여 일기 작성 시에 해당 날짜를 대입한다.
        } else {
            return dateWeatherList.get(0);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        if (date.isAfter(LocalDate.ofYearDay(2050, 1))) {
            throw new InvalidDateException();
        }

        logger.debug("readDiary");
        return diaryRepository.findAllByDate(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    @Override
    public void updateDiary(LocalDate date, String text) {
        Diary diary = diaryRepository.getFirstByDate(date);
        diary.setText(text);

        diaryRepository.save(diary);
    }

    @Override
    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }

    @Override
    public Map<String, Object> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);

        }  catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> resultMap = new HashMap<>();

        JSONArray weatherArray = (JSONArray) jsonObject.get("weather"); //[]
        JSONObject jsonWeather = (JSONObject) weatherArray.get(0);  //{}
        resultMap.put("main", jsonWeather.get("main"));
        resultMap.put("icon", jsonWeather.get("icon"));

        JSONObject jsonMain = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", jsonMain.get("temp"));

        return resultMap;
    }

    @Override
    public void saveWeatherDate() {
        dateWeatherRepository.save(getWeatherFromApi());
    }

    @Override
    public DateWeather getWeatherFromApi() {
        //open weather map에서 날씨 데이터 가져오기
        String weatherData = apiService.getWeatherString();

        //받아온 날씨 데이터 파싱하기
        Map<String, Object> parsedWeather = parseWeather(weatherData);

        //파싱된 데이터 DB에 넣기
        DateWeather dateWeather = new DateWeather();

        dateWeather.setDate(LocalDate.now());
        dateWeather.setWeather(parsedWeather.get("main").toString());
        dateWeather.setIcon(parsedWeather.get("icon").toString());
        dateWeather.setTemperature((Double) parsedWeather.get("temp"));

        return dateWeather;
    }

    @Override
    public DateWeather getWeatherFromApi(LocalDate date) {
        //open weather map에서 날씨 데이터 가져오기
        String weatherData = apiService.getWeatherString();

        //받아온 날씨 데이터 파싱하기
        Map<String, Object> parsedWeather = parseWeather(weatherData);

        //파싱된 데이터 DB에 넣기
        DateWeather dateWeather = new DateWeather();

        dateWeather.setDate(date);
        dateWeather.setWeather(parsedWeather.get("main").toString());
        dateWeather.setIcon(parsedWeather.get("icon").toString());
        dateWeather.setTemperature((Double) parsedWeather.get("temp"));

        return dateWeather;
    }
}
