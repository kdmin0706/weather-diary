package zerobase.weather.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zerobase.weather.service.ApiService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ApiServiceImpl implements ApiService {
    @Value("${openweathermap.key}")
    private String apiKey;

    @Override
    public String getWeatherString() {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            //코드에 따른 입력스트림 파악하기!
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            while (true) {
                String inputLine = br.readLine();
                if (inputLine == null) {
                    break;
                }
                sb.append(inputLine);
            }
            br.close();
            return sb.toString();

        } catch (Exception e) {
            return "failed to get response";
        }
    }
}
