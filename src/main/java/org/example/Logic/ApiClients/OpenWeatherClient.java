package org.example.Logic.ApiClients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Date.Model.Forecast;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class OpenWeatherClient implements GetInfoFromAPI{
    String API_URL = "http://api.openweathermap.org/data/2.5/weather";
    String API_PARAMS = "?q=%s&appid=%s";
    String API_KEY = "83ec405af6a651f1bb0943c732b23f18";

    @Override
    public HttpResponse<String> getInfoFromApi(String city) throws IOException, InterruptedException {
    String url = String.format(API_URL + API_PARAMS, city, API_KEY);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    @Override
    public Forecast apiMapper(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());

        Forecast newForecast = new Forecast(
                jsonNode.get("sys").get("country").asText(),
                jsonNode.get("name").asText(),
                LocalDate.now(),
                (jsonNode.get("main").get("temp").asInt() - 273),
                jsonNode.get("main").get("pressure").asDouble(),
                jsonNode.get("main").get("humidity").asDouble(),
                jsonNode.get("wind").get("deg").asText(),
                jsonNode.get("wind").get("speed").asDouble(),
                jsonNode.get("coord").get("lat").asDouble(),
                jsonNode.get("coord").get("lon").asDouble()
        );
        return newForecast;
    }


}

