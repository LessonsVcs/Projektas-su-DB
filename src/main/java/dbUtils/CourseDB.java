package dbUtils;

import models.Course;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static dbUtils.DBUtils.convertToMysqlDate;
import static dbUtils.DBUtils.convertToUtilDate;
import static dbUtils.RelationDB.removeFromRelation;

public class CourseDB {
    private static final String urlOfDB = "jdbc:h2:~/projektinis6";
    private static final String login = "admin";
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public static boolean courseNameExist(String input){
        boolean value = false;

        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT ID_COURSE from Courses where name = ? ; ");
            statement.setString(1,input);
            ResultSet resultSet =statement.executeQuery();

            if(resultSet.getInt("ID_COURSE") >=1){
                value = true;
            }
            //return true;
        } catch (SQLException e){
            //return false;
        }
        return value;
    }

    public static void deleteCourseDB(String input){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("DELETE FROM Courses where ID_COURSE = ? ; ");
            statement.setInt(1,Integer.parseInt(input));
            statement.execute();
            removeFromRelation(false,Integer.parseInt(input));
            System.out.println("course deleted");
        } catch (SQLException e){
            System.out.println("failed to delete course");
        }
    }

    public static boolean courseExist(String input){
        boolean value = false;
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("Select ID_COURSE FROM Courses where ID_COURSE = ? ; ");
            statement.setInt(1,Integer.parseInt(input));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.getInt("ID_COURSE") >=1){
                value = true;
            }
        } catch (SQLException e){
            //return false;
        }
        return value;
    }

    public static void newCourseDB(String name, String description, java.util.Date startDate, String credits){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("INSERT INTO Courses (NAME ,DESCRIPTION ,StartDate,Credits) " +
                    "VALUES (?,?,?,?); ");
            statement.setString(1,name);
            statement.setString(2,description);
            statement.setDate  (3,convertToMysqlDate(startDate));
            statement.setInt   (4,Integer.parseInt(credits));

            statement.execute();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void editCourseName(String input, Integer id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("UPDATE Courses SET name = ? WHERE ID_COURSE = ?; ");
            statement.setString(1,input);
            statement.setInt(2,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void editCourseDescription(String input, Integer id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("UPDATE Courses SET DESCRIPTION = ? WHERE ID_COURSE = ?; ");
            statement.setString(1,input);
            statement.setInt(2,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void editCourseDate(java.util.Date input, Integer id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("UPDATE Courses SET STARTDATE = ? WHERE ID_COURSE = ?; ");
            statement.setDate(1,convertToMysqlDate(input));
            statement.setInt(2,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();

        }

    }

    public static HashMap getCourses(){
        HashMap<Integer, Course> courseHashMap = new HashMap<>();
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT * from Courses; ");
            ResultSet resultSet = statement.executeQuery();

            int counter = 0;
            while (resultSet.next()){
                Course course = new Course();
                course.setName(resultSet.getString("NAME"));
                course.setID(resultSet.getString("ID_COURSE"));
                course.setCredits(resultSet.getString("CREDITS"));
                course.setDescription(resultSet.getString("DESCRIPTION"));
                course.setStartDate(resultSet.getDate("STARTDATE"));

                courseHashMap.put(counter++,course);
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return courseHashMap;
    }

    public static ResultSet getUsersInCourses(int courseID){
        ResultSet resultSet;
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT ID, NAME, LASTNAME, ROLE from COURSERELATION " +
                    "JOIN USERS ON  ID_USER = ID where ID_COURSE = ?");
            statement.setInt(1,courseID);
            resultSet = statement.executeQuery();
            return resultSet;

        } catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public static ResultSet getCourseInfo(int courseID){
        ResultSet resultSet;
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){

            PreparedStatement statement = con.prepareStatement("SELECT NAME, DESCRIPTION  from COURSES  " +
                    "WHERE ID_COURSE = ?");
            statement.setInt(1,courseID);
            resultSet = statement.executeQuery();
            return resultSet;

        } catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public static int getCourseID(String name){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT ID_COURSE from Courses where NAME = ?; ");
            statement.setString(1,name);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("ID_COURSE");

        } catch (Exception e){
            System.out.println(e);
            return 0;
        }
    }

    public static Date getCourseStartDate(int course_id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT STARTDATE from Courses where ID_COURSE = ?; ");
            statement.setInt(1,course_id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return convertToUtilDate(resultSet.getDate("STARTDATE"));

        } catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public static int getCourseCredits(int ID){

        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT CREDITS FROM COURSES " +
                    "where ID_COURSE  = ?; ");
            statement.setInt(1,ID);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("CREDITS");
        } catch (SQLException e){
            return 0;
        }
    }

    public static void editCourseCredits(int credits, Integer id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("UPDATE Courses SET CREDITS = ? WHERE ID_COURSE = ?; ");
            statement.setInt(1,credits);
            statement.setInt(2,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}
