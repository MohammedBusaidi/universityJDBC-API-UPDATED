import java.util.Scanner;

public class Access {
    static String databaseName = "uni";
    static String user = "sa";
    static String pass = "root";

    public void getAccess() {
        Scanner accessSc = new Scanner(System.in);
        System.out.println("==================LOGIN TO THE DATABASE==================");
        boolean accessGranted = false;
        while(!accessGranted) {
            System.out.print("Enter your Database name: ");
            String databaseInput = accessSc.next();
            System.out.print("Enter your user name: ");
            String userInput = accessSc.next();
            System.out.print("Enter your password: ");
            String passInput = accessSc.next();
            System.out.println("=========================================================");

            // Check if the entered credentials match the expected values
            if(databaseInput.equals(databaseName) && userInput.equals(user) && passInput.equals(pass)) {
                System.out.println("Access Granted!");
            }
            else {
                System.out.println("Access Denied! check your input");
            }

        }

    }
}
