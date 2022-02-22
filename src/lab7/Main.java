package lab7;

import java.io.IOException;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        CSVReader reader = null;
        String path = "/path/to/lab7/examples/";
        try {
            reader = new CSVReader(path + "admin-units.csv", ",", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (reader != null && reader.next()) {
            int id, population;
            String nazwa = reader.get("name");

            try {
                id = reader.getInt("id");
            } catch (Exception e) {
                id = -1;
            }

            try {
                population = reader.getInt("population");
            } catch (Exception e) {
                population = -1;
            }

            System.out.printf(Locale.US, "%d %s %d\n", id, nazwa, population);
        }
    }
}
