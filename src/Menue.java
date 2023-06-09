import java.util.HashMap;
import java.util.Scanner;

public class Menue {
    Scanner menueSc = new Scanner(System.in);
    APIConsumer newAPI = new APIConsumer();
    JDBC newJDBC = new JDBC();

    public void showMenue() {
        boolean menueLoop = true;
        while (menueLoop) {
            HashMap<Integer, String> menueOptions = new HashMap<>();
            menueOptions.put(1, "Initialize Database");
            menueOptions.put(2, "Search the name of the country to fetch universites");
            menueOptions.put(3, "Get list of countries");
            menueOptions.put(4, "Insert the Data");
            menueOptions.put(5, "Backup Database");
            menueOptions.put(6, "Remove Table");
            menueOptions.put(7, "Show all universities");
            menueOptions.put(8, "fetch Data");
            menueOptions.put(9, "Search by: ");
            menueOptions.put(10, "Dump data into file");
            menueOptions.put(11, "Retrieve data From file");
            menueOptions.put(12, "Exit");

            int choice = 0;
            while (choice != 12) {
                System.out.println("==================UNIVERSITIES DATABASE==================");
                for (int i = 1; i <= 12; i++) {
                    System.out.println(i + ". " + menueOptions.get(i));
                }
                System.out.println("=========================================================");
                System.out.print("Enter your choice: ");
                choice = menueSc.nextInt();
                switch (choice) {
                    case 1:
                        newJDBC.initializeDatabase();
                        break;
                    case 2:
                        newAPI.searchByCountry();
                        break;
                    case 3:
                        newAPI.getListOfCountries();
                        break;
                    case 4:
                        newJDBC.insertData();
                        break;
                    case 5:
                        newJDBC.backUpData();
                        break;
                    case 6:
                        newJDBC.removeTable();
                        break;
                    case 7:
                        newAPI.printAllUni();
                        break;
                    case 8:
                        boolean fetchLoop = true;
                        while (fetchLoop) {
                            System.out.println("1- By Database");
                            System.out.println("2- By API");
                            int fetchInput = menueSc.nextInt();
                            if (fetchInput == 1) {
                                newJDBC.fetchDataFromDatabase();
                                break;
                            } else if (fetchInput == 2) {
                                newAPI.fetchDataFromApi();
                                break;
                            } else {
                                fetchLoop = false;
                            }
                        }
                        break;
                    case 9:
                        newJDBC.searchData();
                        break;
                    case 10:
                        newAPI.saveToFile();
                        break;
                    case 11:
                        break;
                    case 12:
                       System.out.println("GOOD BYE!");
                       System.exit(0);
                        break;
                }
            }
        }
    }
}
