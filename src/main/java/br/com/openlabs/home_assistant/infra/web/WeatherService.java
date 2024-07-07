package br.com.openlabs.home_assistant.infra.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public double getCurrentTemperature(String latitude, String longitude) {
        try {
            String url = String.format("%s/weather?lat=%s&lon=%s&units=metric&appid=%s", apiUrl, latitude, longitude, apiKey);
            WeatherResponse response = restTemplate.exchange(url, HttpMethod.GET, createHttpEntity(), WeatherResponse.class).getBody();
            return response != null ? response.getMain().getTemp() : 0.0;
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException(e);
            return 0.0;
        }
    }

    private HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
        return new HttpEntity<>(headers);
    }

    private void handleHttpClientErrorException(HttpClientErrorException e) {
        if (e.getStatusCode().value() == 404) {
            System.err.println("City not found. Please check the coordinates and try again.");
        } else {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
