package menu.extras.dbUtils;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static menu.extras.dbUtils.DBUtils.convertToMysqlDate;
import static menu.extras.dbUtils.RelationDB.removeFromRelation;

public class CourseDB {
    private static final String urlOfDB = "jdbc:h2:~/projektinis4";
    private static final String login = "admin";
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public static boolean courseNameExist(String input){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT ID_COURSE from Courses where name = ? ; ");
            statement.setString(1,input);
            statement.execute();
            return true;
        } catch (SQLException e){
            return false;
        }
    }

    public static void deleteCourseDB(String input){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("DELETE FROM Courses where ID = ? ; ");
            statement.setInt(1,Integer.parseInt(input));
            statement.execute();
            removeFromRelation(false,Integer.parseInt(input));
            System.out.println("course deleted");
        } catch (SQLException e){
            System.out.println("failed to delete course");
        }
    }

    public static boolean courseExist(String input){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("Select ID_COURSE FROM Courses where ID_COURSE = ? ; ");
            statement.setInt(1,Integer.parseInt(input));
            statement.executeQuery();
            return true;
        } catch (SQLException e){
            return false;
        }
    }

    public static void newCourseDB(String name, String description, java.util.Date startDate, String credits){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("INSERT INTO Users (Name,Description,StartDate,Credits) " +
                    "VALUES (?,?,?,?); ");
            statement.setString(1,name);
            statement.setString(2,description);
            statement.setDate  (3,convertToMysqlDate(startDate));
            statement.setInt   (4,Integer.parseInt(credits));

            statement.execute();

        } catch (Exception e){

        }
    }


}
