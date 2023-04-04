import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
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
        String url = "jdbc:sqlserver://" + "localhost:1433;" + "encrypt=true;" + "trustServerCertificate=true";
        Connection con = null;
        try {
            Driver driver = (Driver) Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            DriverManager.registerDriver(driver);

            con = DriverManager.getConnection(url, Access.user, Access.pass);
            Statement st = con.createStatement();

            url += ";databaseName=" + Access.databaseName;
            con = DriverManager.getConnection(url, Access.user, Access.pass);

            String sql = "INSERT INTO universities(Name, Country, State_Province, Domains, Web_Pages, Alpha_Two_Code) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            University[] uni = APIConsumer.uni;
            for (University myUni : uni) {
                ps.setString(1, myUni.name);
                ps.setString(2, myUni.country);
                ps.setString(3, myUni.state_province);
                ps.setString(4, String.join(",", myUni.domains));
                ps.setString(5, String.join(",", myUni.web_pages));
                ps.setString(6, myUni.alpha_two_code);
                ps.executeUpdate();
            }
            System.out.println("DATA INSERTED!");
            con.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
    public void backUpData() {
        // create a new file
        String url = "jdbc:sqlserver://" + "localhost:1433;" + "encrypt=true;" + "trustServerCertificate=true";
        Connection con = null;
        try {

            Driver driver = (Driver) Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            DriverManager.registerDriver(driver);

            con = DriverManager.getConnection(url, Access.user, Access.pass);

            String sql = "BACKUP DATABASE " + Access.databaseName + "\r\n"
                    + "TO DISK = 'C:\\Users\\moody\\OneDrive\\Desktop\\java\\University-Updated" + Access.databaseName + ".bak'\r\n"
                    + "WITH DESCRIPTION = 'Full Backup for" + Access.databaseName + " Database'";


            PreparedStatement statement = con.prepareStatement(sql);

            statement.executeUpdate();

            statement.close();
            con.close();

            System.out.println("Backup Successfully :)");
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
    public void removeTable() {
        String url = "jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true";
        Connection con = null;
        try {
            Driver driver = (Driver) Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            DriverManager.registerDriver(driver);

            url += ";databaseName=" + Access.databaseName;
            con = DriverManager.getConnection(url, Access.user, Access.pass);
            Statement st = con.createStatement();

            String sql = "DROP TABLE universities;";

            st.executeUpdate(sql);

            st.close();
            con.close();

            System.out.println("TABLE REMOVED!");
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
    public void fetchDataFromDatabase() {
        String url = "jdbc:sqlserver://" + "localhost:1433;" + "encrypt=true;" + "trustServerCertificate=true";
        Connection con = null;

        try {
            Driver driver = (Driver) Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            DriverManager.registerDriver(driver);

            // Update url with the database name
            url += ";databaseName=" + Access.databaseName;
            con = DriverManager.getConnection(url, Access.user, Access.pass);
            Statement st = con.createStatement();

            String sql = "SELECT * FROM universities";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                String country = rs.getString("Country");
                String state_province = rs.getString("State_Province");
                String domains = rs.getString("Domains");
                String web_pages = rs.getString("Web_Pages");
                String alpha_two_code = rs.getString("Alpha_Two_Code");

                System.out.println("=========================================================");
                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Country: " + country);
                System.out.println("State/Province: " + state_province);
                System.out.println("Domains: " + domains);
                System.out.println("Web Pages: " + web_pages);
                System.out.println("Alpha Two Code: " + alpha_two_code);
                System.out.println("=========================================================");

            }
            System.out.println("DATA FETCHED!");
            con.close();
            st.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }

    }
}
