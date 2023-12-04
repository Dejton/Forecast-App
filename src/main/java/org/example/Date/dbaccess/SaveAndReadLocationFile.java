package org.example.Date.dbaccess;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface SaveAndReadLocationFile {
    void addFileToDataBase(String city, String country, double lat, double lon) throws IOException;
    String readFileToDataBase() throws IOException;
}
