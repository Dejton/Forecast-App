package org.example.Logic.Service;

import org.example.Date.Model.Forecast;
import org.example.Date.Model.Location;
import org.example.Date.dbaccess.LocationFileService;
import org.example.Logic.ApiClients.OpenWeatherClient;
import org.example.Logic.ApiClients.WeatherStackClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Service {
    public static void addCityToList() throws IOException {
        Scanner scanner = new Scanner(System.in);
        LocationFileService locationFileService = new LocationFileService();
        double lat;
        double lon;

        System.out.println("Podaj nazwę miasta: ");
        String cityName = scanner.nextLine();
        System.out.println("Podaj nazwę państwa: ");
        String countryName = scanner.nextLine();
        do {
            System.out.println("Podaj szerokość geograficzną: ");
            lat = scanner.nextDouble();
        } while (lat < -90 || lat > 90);
        do {
            System.out.println("Podaj długość geograficzną: ");
            lon = scanner.nextDouble();
        } while (lon < -180 || lon > 180);


        locationFileService.addFileToDataBase(cityName, countryName, lat, lon);
    }

    public static String getCitiesList() throws IOException {
        List<String> cities = new ArrayList<>();
        StringBuilder list = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader("cities_list.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] split = line.split(",");
                String cityName = "";
                for (String e:split) {
                    if (e.startsWith("City: ")) {
                        cityName = e.substring(5);
                    }
                }
                cities.add(cityName);
            }
        }
        for (String e : cities) {
            list.append(e).append(", ");
        }

        if (!list.isEmpty()) {
            list.deleteCharAt(list.length() - 2);
        }
        return list.toString();
    }

    public static Forecast getForecastFromApi(String city) throws IOException, InterruptedException {
        OpenWeatherClient openWeatherClient = new OpenWeatherClient();
        WeatherStackClient weatherStackClient = new WeatherStackClient();
        Forecast openWeatherData = openWeatherClient.apiMapper(openWeatherClient.getInfoFromApi(city));
        Forecast weatherStackData = weatherStackClient.apiMapper(weatherStackClient.getInfoFromApi(city));

        if (!LocationFileService.isValuePresent(city)) {
            System.out.println("Brak miasta na liście!");
            return null;
        }
        return getAverageData(openWeatherData, weatherStackData);
    }

    public static Forecast getAverageData(Forecast firstValue, Forecast secondValue) {
        Forecast forecastWithAverageData = new Forecast(
                secondValue.getCountry(),
                secondValue.getCity(),
                secondValue.getDate(),
                ((firstValue.getTemperature() + secondValue.getTemperature()) / 2),
                ((firstValue.getPressure() + secondValue.getPressure()) / 2),
                ((firstValue.getHumidity() + secondValue.getHumidity()) / 2),
                String.valueOf((Double.parseDouble(firstValue.getWindDirection()) + Double.parseDouble(secondValue.getWindDirection()) / 2)),
                ((firstValue.getWindSpeed() + secondValue.getWindSpeed()) /2),
                secondValue.getLat(),
                secondValue.getLon()
        );
        return forecastWithAverageData;
    }

    public static String getStringForSaving(Forecast finalForecast) {
           if (finalForecast == null) {
               return null;
           } else {
               String finalResult = String.format("%s:\n Temperature: %d\n Pressure: %f\n Humidity: %f\n Wind Direction: %s\n Wind speed: %f\n",
                       finalForecast.getCity(),
                       finalForecast.getTemperature(),
                       finalForecast.getPressure(),
                       finalForecast.getHumidity(),
                       finalForecast.getWindDirection(),
                       finalForecast.getWindSpeed());

               return finalResult;
           }
    }
}
