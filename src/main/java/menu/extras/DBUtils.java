package menu.extras;

import javax.management.relation.Role;
import java.sql.*;

public class DBUtils {
    private static final String urlOfDB = "jdbc:h2:~/projektinis4";
    private static final String login = "admin";
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

    public static void newUserToDB(String name, String lastName, String password, String userName, Roles role){

        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("INSERT INTO Users (Name,LastName,Password,Username,Role) " +
                    "VALUES (?,?,?,?,?); ", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,name);
            statement.setString(2,lastName);
            statement.setString(3,password);
            statement.setString(4,userName);
            statement.setString(5,role.toString());

            //statement.execute();
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong(1);
                System.out.println(id);
            } else {
                System.out.println("Error on retrieving ID");
            }

        } catch (Exception e){
            System.out.println(e);
        }
    }

    public static void newUserToDBexpress(String name, String lastName, String password, String userName, Roles role,
                                          String email, Date dateOfBirth, String address){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("INSERT INTO Users " +
                    "(Name,LastName,Password,Username,Role,Email,DateOfBirth,address) " +
                    "VALUES (?,?,?,?,?,?,?,?); ");
            statement.setString(1,name);
            statement.setString(2,lastName);
            statement.setString(3,password);
            statement.setString(4,userName);
            statement.setString(5,role.toString());
            statement.setString(6,email);
            statement.setDate(7,dateOfBirth);
            statement.setString(8,address);

            statement.execute();

        } catch (Exception e){

        }
    }

    public static void newCourse(String name, String description, Date startDate, String credits){

        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("INSERT INTO Users (Name,Description,StartDate,Credits) " +
                    "VALUES (?,?,?,?); ");
            statement.setString(1,name);
            statement.setString(2,description);
            statement.setDate  (3,startDate);
            statement.setInt   (4,Integer.parseInt(credits));

            statement.execute();

        } catch (Exception e){

        }
    }

    public static String checkUsername(String input){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT password from Users where username = ? ; ");
            statement.setString(1,input);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getString("password");
        } catch (SQLException e){
            return null;
        }
    }

}
