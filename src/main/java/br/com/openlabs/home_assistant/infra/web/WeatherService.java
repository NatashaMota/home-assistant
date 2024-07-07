package br.com.openlabs.home_assistant.infra.web;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public double getCurrentTemperature(String city) {
        String url = String.format("%s/weather?id=%s&units=metric&appid=%s", apiUrl, city, apiKey);
        WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
        return response.getMain().getTemp();
    }

    public String getCityName(String city) {
        String url = String.format("%s/weather?id=%s&units=metric&appid=%s", apiUrl, city, apiKey);
        WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
        return response.getName();
    }
}
