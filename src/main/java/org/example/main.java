package org.example;

import org.example.Date.Model.Forecast;
import org.example.Date.Model.Location;
import org.example.Logic.ApiClients.OpenWeatherClient;
import org.example.Logic.ApiClients.WeatherStackClient;
import org.example.Logic.Service.Service;
import org.example.Prezentation.Interface;

import java.io.IOException;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Date;
import java.util.UUID;

public class main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Interface.startApp();
    }
}
