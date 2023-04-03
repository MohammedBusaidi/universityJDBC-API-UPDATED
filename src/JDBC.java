import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class JDBC {
    Scanner dataSc = new Scanner(System.in);
    public void initializeDatabase() {
        //establish connection to sql server
        String url = "jdbc:sqlserver://" + "localhost:1433;" + "encrypt=true;" + "trustServerCertificate=true";
        Connection con = null;
            try {
                Driver driver = (Driver) Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
                DriverManager.registerDriver(driver);

                con = DriverManager.getConnection(url, Access.user, Access.pass);
                Statement st = con.createStatement();

                //Check if the database exists
                String sql = "SELECT * FROM sys.databases WHERE name='" + Access.databaseName + "'";
                ResultSet rs = st.executeQuery(sql);

                // Update url with the existing database name
                if (rs.next()) {
                    url += ";databaseName=" + Access.databaseName;
                    con = DriverManager.getConnection(url, Access.user, Access.pass);
                    Statement st2 = con.createStatement();
                    // Check if the table exists
                    String sql1 = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'universities'";
                    rs = st2.executeQuery(sql1);

                    if (!rs.next()) {
                        // Create table if it doesn't exist
                        String sql2 = "CREATE TABLE universities (\r\n" + "  ID INTEGER IDENTITY PRIMARY KEY,\r\n"
                                + "  Name VARCHAR(255),\r\n" + "  Country VARCHAR(255),\r\n"
                                + "  State_Province VARCHAR(255),\r\n" + "  Domains VARCHAR(MAX),\r\n"
                                + "  Web_Pages VARCHAR(MAX),\r\n" + "  Alpha_Two_Code VARCHAR(2)\r\n" + ");";
                        st2.executeUpdate(sql2);
                        System.out.println("TABLE CREATED!");
                    } else {
                        System.out.println("Table already exists!");
                    }
                }
            } catch (Exception ex) {
                System.err.println(ex);
            }
    }
    public void insertData() {
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
            University[] universities = gson.fromJson(json.toString(), University[].class);

            // establish database connection
            String url1 = "jdbc:sqlserver://" + "localhost:1433;" + "encrypt=true;" + "trustServerCertificate=true";
            Connection con = null;
            Driver driver = (Driver) Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            DriverManager.registerDriver(driver);

            con = DriverManager.getConnection(url1, Access.user, Access.pass);
            Statement st = con.createStatement();

            url1 += ";databaseName=" + Access.databaseName;
            con = DriverManager.getConnection(url1, Access.user, Access.pass);

            // loop through all universities and insert them into the table
            String sql = "INSERT INTO universities(Name, Country, State_Province, Domains, Web_Pages, Alpha_Two_Code) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            for (University uni : universities) {
                ps.setString(1, uni.name);
                ps.setString(2, uni.country);
                ps.setString(3, uni.state_province);
                ps.setString(4, String.join(",", uni.domains));
                ps.setString(5, String.join(",", uni.web_pages));
                ps.setString(6, uni.alpha_two_code);
                ps.executeUpdate();
            }
            System.out.println("DATA INSERTED!");
            con.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
