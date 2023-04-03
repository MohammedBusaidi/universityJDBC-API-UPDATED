import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class APIConsumer {
    static University uni[];
    Scanner apiSc =new Scanner(System.in);

    public void searchByCountry() {
        System.out.println("Enter the name of the country: ");
        String countryInput = apiSc.next();

        // bring data from API and displays it as an object
        String apiUrl = "http://universities.hipolabs.com/search?country=" + countryInput;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            StringBuilder json = new StringBuilder();

            while ((output = br.readLine()) != null) {
                json.append(output);
            }
            conn.disconnect();
            // switch from Json to object using Gson(Gson reads Json).
            Gson gson = new Gson();
            uni = gson.fromJson(json.toString(), University[].class);
            System.out.println("+----+-------------------------+------------+-----------------------+-----------------------+");
            System.out.println("| ID | University Name         | Location   | Domains               | Website               |");
            System.out.println("+----+-------------------------+------------+-----------------------+-----------------------+");
            for(int i = 0; i < uni.length; i++) {
                University myUni = uni[i];
                String id = String.format("%02d", i + 1);
                String name = myUni.name;
                String location = myUni.state_province + ", " + myUni.country;
                String domains = String.join(", ", myUni.domains);
                String website = String.join(", ", myUni.web_pages);
                System.out.printf("| %s | %-23s | %-10s | %-21s | %-21s |\n", id, name, location, domains, website);
            }
            System.out.println("+----+-------------------------+------------+-----------------------+-----------------------+");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getListOfCountries() {
        String apiUrl = "http://universities.hipolabs.com/search?country=";
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            StringBuilder json = new StringBuilder();

            while ((output = br.readLine()) != null) {
                json.append(output);
            }
            conn.disconnect();
            Gson gson = new Gson();
            uni = gson.fromJson(json.toString(), University[].class);
            Set<String> countries = new HashSet<>();
            int counter = 1;
            for (University u : uni) {
                if (countries.add(u.country)) {
                    System.out.println(counter + ". " + u.country);
                    counter++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void printAllUni() {
        System.out.println("=============================================================================");
        for (int i = 0; i < uni.length; i++) {
            University myUni = uni[i];
            System.out.println((i + 1) + ":\t" + myUni.state_province + " - " + myUni.country + " - " + myUni.name
                    + " - " + myUni.alpha_two_code);
            for (int j = 0; j < myUni.domains.length; j++) {
                System.out.println("\tDomain " + (j + 1) + ": " + myUni.domains[j]);
            }

            for (int m = 0; m < myUni.web_pages.length; m++) {
                System.out.println("\tWeb page " + (m) + ": " + myUni.web_pages[m]);

            }
            System.out.println("=============================================================================");
        }

    }
}
