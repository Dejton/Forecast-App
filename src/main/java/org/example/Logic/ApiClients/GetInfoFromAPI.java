package org.example.Logic.ApiClients;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Date.Model.Forecast;

import java.io.IOException;
import java.net.http.HttpResponse;

public interface GetInfoFromAPI {
 HttpResponse<String> getInfoFromApi(String value) throws IOException, InterruptedException;
 Forecast apiMapper(HttpResponse<String> value) throws JsonProcessingException;
}
