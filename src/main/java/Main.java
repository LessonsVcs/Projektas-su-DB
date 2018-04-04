import menu.Menu;

import static dbUtils.DBUtils.initDB;
import static dbUtils.DBUtils.initDriver;

public class Main {
    public static void main(String[] args) {
        //Menu menu = new Menu();
        //menu.selectionMenu();

        initDriver();
        initDB();
        //newUserToDB("romas","romas","romas", "romas4", Roles.USER);
        //getUsers();
        Menu menu = new Menu();
        menu.selectionMenu();
        //newCourseDB("ccc","ccc",null,"1");


    }
}
