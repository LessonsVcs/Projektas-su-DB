package menu.extras.dbUtils;

import menu.extras.Roles;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DBUtils {
    private static final String urlOfDB = "jdbc:h2:~/projektinis5";
    private static final String login = "admin";
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public static void initDriver(){
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void initDB() {

        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            //firstName, lastName, password, username, role, email, dateOfBirth, address, personalNumber
            //dropTables(con);

            PreparedStatement statement;
            initUserTable(con);
            initCourseTable(con);
            initCourseRelationTable(con);
            statement = con.prepareStatement("INSERT INTO Users (Name,LastName,Password,Username,Role) " +
                    "VALUES ('admin','admin','admin','admin','ADMIN'); ");
            try {
                statement.execute();
            }catch (Exception e){

            }



        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void dropTables(Connection con) {
        try {
            PreparedStatement drop = con.prepareStatement("DROP TABLE users");
            drop.execute();
        } catch (Exception e){

        }
        try {
            PreparedStatement drop2 = con.prepareStatement("DROP TABLE Courses");
            drop2.execute();
        } catch (Exception e){

        }
        try {
            PreparedStatement drop2 = con.prepareStatement("DROP TABLE CourseRelation");
            drop2.execute();
        } catch (Exception e){

        }
    }

    private static void initCourseRelationTable(Connection con) throws SQLException {
        PreparedStatement statement =
                con.prepareStatement("CREATE TABLE IF NOT EXISTS CourseRelation" +
                        "(ID_course INT not null, " +
                        "ID_user INT," +
                        "UNIQUE KEY acct_usernameCourse_UNIQUE (ID_course,ID_user)," +
                        "FOREIGN KEY(ID_course) REFERENCES Courses(ID_course), " +
                        "FOREIGN KEY(ID_user) REFERENCES Users(ID)  )");
        statement.execute();
    }

    private static void initCourseTable(Connection con) throws SQLException {
        PreparedStatement statement =
                con.prepareStatement("CREATE TABLE IF NOT EXISTS Courses" +
                        "(ID_course INT auto_increment PRIMARY KEY , " +
                        "Name VARCHAR(150)," +
                        "Description VARCHAR(500)," +
                        "StartDate DATE, " +
                        "Credits INT )");
        statement.execute();
    }

    private static void initUserTable(Connection con) throws SQLException {
        PreparedStatement statement =
                con.prepareStatement("CREATE TABLE IF NOT EXISTS Users" +
                        "(ID INT auto_increment PRIMARY KEY , " +
                        "Name VARCHAR(100)," +
                        "LastName VARCHAR(100)," +
                        "Password VARCHAR(100) NOT NULL," +
                        "Username VARCHAR_IGNORECASE(100) NOT NULL," +
                        "Role VARCHAR(50) NOT NULL," +
                        "Email VARCHAR(50)," +
                        "DateOfBirth DATE," +
                        "address VARCHAR(255)," +
                        "UNIQUE KEY acct_username_UNIQUE (Username) )");
        statement.execute();
    }

    public static Date convertToMysqlDate(java.util.Date in){
        Date sqlDate;
        try {
            sqlDate = Date.valueOf(format.format(in));
        }catch (Exception e){
            sqlDate  = null;
        }

        return sqlDate;
    }



}
