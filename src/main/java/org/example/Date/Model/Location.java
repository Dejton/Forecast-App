package org.example.Date.Model;

import java.time.LocalDate;
import java.util.UUID;

public class Location extends AbstractObjectBuilder{
    public Location(String country, String city, LocalDate date) {
        super(country, city, date);
    }

    public Location(String country, String city, double lat, double lon) {
        super(country, city, lat, lon);
    }
}
