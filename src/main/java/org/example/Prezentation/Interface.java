package org.example.Prezentation;

import org.example.Date.dbaccess.LocationFileService;
import org.example.Logic.Service.Service;

import java.io.IOException;
import java.util.Scanner;

public class Interface {
    public static void startApp() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int input;

        do {
            System.out.println("PROGNOZA POGODY");
            System.out.println();
            System.out.println("Wybierz opcję:");
            System.out.println("1. Dodaj lokalizację do bazy danych.");
            System.out.println("2. Sprawdź listę dostępnych miast.");
            System.out.println("3. Wyświetl uśrednione pade pogodowe z zewnętrznej Api.");
            System.out.println("4. Wyświetl prognozę pogody z bazy danych.");
            System.out.println("5. Wyświetl prognozę pogody, wg geolokacji.");
            System.out.println("6. Zamknij aplikację.");

            input = scanner.nextInt();
            scanner.nextLine();

            switch (input) {
                case 1:
                    Service.addCityToList();
                    break;
                case 2:
                    System.out.println("Lista dostępnych miast: ");
                    System.out.println(Service.getCitiesList());
                case 3:
                    System.out.println("Podaj miasto: ");
                    String checkCity = scanner.nextLine();
                    System.out.println(Service.getStringForSaving(Service.getForecastFromApi(checkCity)));
            }
        } while(input != 6);
            scanner.close();
            System.out.println("Aplikacja zamknięta.");

    }
}
