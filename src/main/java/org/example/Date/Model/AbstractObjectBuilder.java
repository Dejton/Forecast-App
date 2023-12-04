package org.example.Date.Model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractObjectBuilder {
    private UUID id;
    private String country;
    private String city;
    private LocalDate date;
    private int temperature;
    private double pressure;
    private double humidity;
    private String windDirection;
    private double windSpeed;
    private double lat;
    private double lon;

    public AbstractObjectBuilder(String country, String city, LocalDate date, int temperature, double pressure, double humidity, String windDirection, double windSpeed, double lat, double lon) {
        this.id = createUuid(country, city);
        this.country = country;
        this.city = city;
        this.date = date;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.lat = lat;
        this.lon = lon;
    }

    public AbstractObjectBuilder(String country, String city, double lat, double lon) {
        this.country = country;
        this.city = city;
        this.lat = lat;
        this.lon = lon;
    }

    public AbstractObjectBuilder(String country, String city, LocalDate date) {
        this.id = createUuid(country, city);
        this.country = country;
        this.city = city;
        this.date = date;
    }

    public AbstractObjectBuilder(int temperature, double pressure, double humidity, String windDirection, double windSpeed) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public UUID getId() {
        return id;
    }


    public String getCountry() {
        return country;
    }


    public String getCity() {
        return city;
    }


    public LocalDate getDate() {
        return date;
    }


    public int getTemperature() {
        return temperature;
    }


    public double getPressure() {
        return pressure;
    }


    public double getHumidity() {
        return humidity;
    }


    public String getWindDirection() {
        return windDirection;
    }


    public double getWindSpeed() {
        return windSpeed;
    }


    private UUID createUuid(String country, String city) {
        return UUID.nameUUIDFromBytes((country + city).getBytes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AbstractObjectBuilder that = (AbstractObjectBuilder) o;
        return Double.compare(temperature, that.temperature) == 0 && Double.compare(pressure, that.pressure) == 0 && Double.compare(humidity, that.humidity) == 0 && Double.compare(windSpeed, that.windSpeed) == 0 && Objects.equals(id, that.id) && Objects.equals(country, that.country) && Objects.equals(city, that.city) && Objects.equals(date, that.date) && Objects.equals(windDirection, that.windDirection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country, city, date, temperature, pressure, humidity, windDirection, windSpeed);
    }

    @Override
    public String toString() {
        return "AbstractObjectBuilder{" + "id=" + id + ", country='" + country + '\'' + ", city='" + city + '\'' + ", date=" + date + ", temperature=" + temperature + ", pressure=" + pressure + ", humidity=" + humidity + ", windDirection=" + windDirection + ", windSpeed=" + windSpeed + '}';
    }
}
