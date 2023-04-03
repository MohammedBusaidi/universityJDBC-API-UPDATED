import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class APIConsumer {
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
            University uni[] = gson.fromJson(json.toString(), University[].class);
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
}
