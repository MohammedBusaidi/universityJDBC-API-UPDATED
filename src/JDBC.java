import java.sql.*;
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
}
