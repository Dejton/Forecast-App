package org.example.Date.dbaccess;

import org.example.Date.Model.Location;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationFileService implements SaveAndReadLocationFile{

    public static boolean isValuePresent(String value) {
        try (BufferedReader reader = new BufferedReader(new FileReader("cities_list.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (line.contains(value.toLowerCase())) {
                    return true;
                }
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void addFileToDataBase(String city, String country, double lat, double lon) throws IOException {
        Location newLocation = new Location(country, city, lat, lon);
        StringBuilder newData = new StringBuilder()
                .append("City: ")
                .append(newLocation.getCity())
                .append(", ")
                .append("Country: ")
                .append(country)
                .append(", ")
                .append("Latitude: ")
                .append(lat)
                .append(", ")
                .append("Longitude: ")
                .append(lon)
                .append("\n");

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("cities_list.csv", true))) {
            if (!isValuePresent(city)) {
                writer.write(newData.toString());
            }
        }
    }
    @Override
    public String readFileToDataBase(){
      return null;
    }


}
