package br.com.openlabs.home_assistant.infra.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Service
public class GeoLocationService {

    @Value("${ipinfo.api.key}")
    private String API_KEY;
    private String API_URL = "https://ipinfo.io?token=";

    public List<String> getGeoLocation() {
        RestTemplate restTemplate = new RestTemplate();
        GeoLocation geoLocation = restTemplate.getForObject(API_URL + API_KEY, GeoLocation.class);
        if (geoLocation != null && geoLocation.getLoc() != null) {
            return Arrays.asList(geoLocation.getLoc().split(","));
        }
        return null;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeoLocation {
        private String loc;}


}