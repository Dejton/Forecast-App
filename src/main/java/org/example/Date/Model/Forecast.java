package org.example.Date.Model;

import java.time.LocalDate;
import java.util.UUID;

public class Forecast extends AbstractObjectBuilder{
    public Forecast(int temperature, double pressure, double humidity, String windDirection, double windSpeed) {
        super(temperature, pressure, humidity, windDirection, windSpeed);
    }

    public Forecast(String country, String city, LocalDate date, int temperature, double pressure, double humidity, String windDirection, double windSpeed, double lat, double lon) {
        super(country, city, date, temperature, pressure, humidity, windDirection, windSpeed, lat, lon);
    }
}
