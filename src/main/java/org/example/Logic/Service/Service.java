package org.example.Logic.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Date.Model.Forecast;
import org.example.Date.Model.Location;
import org.example.Date.dbaccess.LocationFileService;
import org.example.Logic.ApiClients.OpenWeatherClient;
import org.example.Logic.ApiClients.WeatherStackClient;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
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


        locationFileService.addFileToDataBase(cityName.toLowerCase(), countryName, lat, lon);
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

        if (!LocationFileService.isValuePresent(city, "cities_list.csv")) {
            System.out.println("Brak miasta na liście!");
            return null;
        }
        return getAverageData(openWeatherData, weatherStackData);
    }
    public static Forecast getForecastFromApiFor4(String city) throws IOException, InterruptedException {
        OpenWeatherClient openWeatherClient = new OpenWeatherClient();
        WeatherStackClient weatherStackClient = new WeatherStackClient();
        Forecast openWeatherData = openWeatherClient.apiMapper(openWeatherClient.getInfoFromApi(city));
        Forecast weatherStackData = weatherStackClient.apiMapper(weatherStackClient.getInfoFromApi(city));

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

    public static String getStringForSaving(Forecast finalForecast) throws IOException {
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
    public static void saveAverageForecastToFile(Forecast averageForecast) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("C:\\Users\\patry\\IdeaProjects\\ForecastApp\\forecast_list.csv", true))){
            final List<String> forecastInfo = loadWeatherInfosFromFile();
            Files.write(Path.of("C:\\Users\\patry\\IdeaProjects\\ForecastApp\\forecast_list.csv"), new byte[0]);
            final List<String> updatedForecastInfo = new ArrayList<>(forecastInfo.stream()
                    .filter(wf -> !wf.contains(averageForecast.getCity()))
                    .toList());
            updatedForecastInfo.add(averageForecast.toString());

            updatedForecastInfo.forEach(writer::println);
        }
       }

       public static void getForecastFromFile(String city) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\patry\\IdeaProjects\\ForecastApp\\forecast_list.csv"));

            if (LocationFileService.isValuePresent(city, "C:\\Users\\patry\\IdeaProjects\\ForecastApp\\forecast_list.csv")) {
                String line;
                if ((line = reader.readLine()) != null) {
                    if (line.toLowerCase().contains(city)) {
                        String[] newForecastFromFile = line.split(",");
                        System.out.println(getForecastFromCsv(newForecastFromFile));
                    }
                }

            } else {
                System.out.println("Brak miasta liście! Trwa pobieranie pogody z zewnętrznego serwisu.");
                System.out.println(Service.getForecastFromApiFor4(city));
            }

       }

    private static Forecast getForecastFromCsv(String[] newForecastFromFile) {
        if (newForecastFromFile.length == 8) {
            return new Forecast(newForecastFromFile[1],
                    newForecastFromFile[2],
                    LocalDate.now(),
                    Integer.parseInt(newForecastFromFile[4].replaceAll("\\D+", "")),
                    Double.parseDouble(newForecastFromFile[5].replaceAll("\\D+", "")),
                    Double.parseDouble(newForecastFromFile[6].replaceAll("\\D+", "")),
                    newForecastFromFile[7], Double.parseDouble(newForecastFromFile[8].replaceAll("\\D+", "")));
        } else {
            System.out.println("CSV format is incorrect");
            return null;
        }
    }


    static private List<String> loadWeatherInfosFromFile() {
        List<String> weatherInfos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\patry\\IdeaProjects\\ForecastApp\\forecast_list.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                weatherInfos.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherInfos;
    }

    }


















