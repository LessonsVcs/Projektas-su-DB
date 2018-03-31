
import menu.extras.Roles;


import static menu.extras.dbUtils.CourseDB.newCourseDB;
import static menu.extras.dbUtils.DBUtils.initDB;
import static menu.extras.dbUtils.DBUtils.initDriver;
import static menu.extras.dbUtils.UserDB.newUserToDB;

public class Main {
    public static void main(String[] args) {
        //Menu menu = new Menu();
        //menu.selectionMenu();

        initDriver();
        initDB();
        //newUserToDB("romas","romas","romas", "romas4", Roles.USER);
        //Menu menu = new Menu();
        //menu.selectionMenu();
        newCourseDB("ccc","ccc",null,"1");



    }
}
