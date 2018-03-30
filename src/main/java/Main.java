import menu.extras.Roles;
import user.Login;


import static menu.extras.DBUtils.initDB;
import static menu.extras.DBUtils.initDriver;
import static menu.extras.DBUtils.newUserToDB;

public class Main {
    public static void main(String[] args) {
        Roles role;
        //Menu menu = new Menu();
        //menu.selectionMenu();
        initDriver();
        initDB();
        newUserToDB("romas","romas","romas", "romas7", Roles.USER);
        Login login = new Login();
        login.loginToSystem();




    }
}
