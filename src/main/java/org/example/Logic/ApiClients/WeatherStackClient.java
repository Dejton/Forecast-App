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

public class WeatherStackClient implements GetInfoFromAPI{
    String API_URL = "http://api.weatherstack.com/current?access_key=";
    String API_PARAMS = "%s&query=%s";
    String API_KEY = "74fc690f1bc72dbb8e5b811dd4460c42";
    @Override
    public HttpResponse<String> getInfoFromApi(String city) throws IOException, InterruptedException {
        String url = String.format(API_URL + API_PARAMS, API_KEY,city);
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
                jsonNode.get("location").get("country").asText(),
                jsonNode.get("location").get("name").asText(),
                LocalDate.now(),
                jsonNode.get("current").get("temperature").asInt(),
                jsonNode.get("current").get("pressure").asDouble(),
                jsonNode.get("current").get("humidity").asDouble(),
                jsonNode.get("current").get("wind_degree").asText(),
                jsonNode.get("current").get("wind_speed").asDouble(),
                jsonNode.get("location").get("lat").asDouble(),
                jsonNode.get("location").get("lon").asDouble()
        );
        return newForecast;
    }


}
