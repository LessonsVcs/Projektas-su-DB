package dbUtils;

import extras.Roles;
import models.Course;

import java.sql.*;
import java.util.HashMap;

import static dbUtils.DBUtils.convertToUtilDate;

public class RelationDB {
    private static final String urlOfDB = "jdbc:h2:~/projektinis6";
    private static final String login = "admin";

    public static void removeFromRelation(Boolean removeUser, int id){
        if(removeUser) {
            try (
                    Connection con = DriverManager.getConnection(urlOfDB, login, login)
            ) {
                PreparedStatement statement = con.prepareStatement("DELETE FROM COURSERELATION where ID_USER = ? ; ");
                statement.setInt(1, id);
                statement.execute();
                System.out.println("User deleted");
            } catch (SQLException e) {
                System.out.println("failed to delete user from relation");
            }
        } else {
            try (
                    Connection con = DriverManager.getConnection(urlOfDB,login,login)
            ){
                PreparedStatement statement = con.prepareStatement("DELETE FROM COURSERELATION where ID_COURSE = ? ; ");
                statement.setInt(1,id);
                statement.execute();
                System.out.println("Course deleted");
            } catch (SQLException e){
                System.out.println("failed to delete course from relation");
            }
        }

    }

    public static void addToCourse(int user_id,int course_id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("INSERT INTO COURSERELATION  (ID_USER, ID_COURSE) " +
                    "VALUES (?,?); ");
            statement.setInt(1,user_id);
            statement.setInt(2,course_id);
            statement.execute();
            System.out.println("User added to course");


        } catch (Exception e){
            System.out.println("failed to add user to course");
        }
    }

    public static void removeFromCourse(int user_id,int course_id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB, login, login)
        ) {
            PreparedStatement statement = con.prepareStatement("DELETE FROM COURSERELATION where ID_USER = ? AND ID_COURSE = ? ; ");
            statement.setInt(1, user_id);
            statement.setInt(2, course_id);
            statement.execute();
            System.out.println("User deleted from course");
        } catch (SQLException e) {
            System.out.println("failed to delete user from course");
        }
    }

    public static boolean isInCourse(int user_id, int user_course){
        boolean value= false;
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT * FROM COURSERELATION " +
                    "WHERE ID_USER  = ? AND ID_COURSE = ? ; ");
            statement.setInt(1,user_id);
            statement.setInt(2,user_course);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            if (resultSet.getInt("ID_COURSE")>0){
                value = true;
            }
        } catch (SQLException e){
            //e.printStackTrace();
        }
        return value;
    }

    public static HashMap getUserCourses(int user_id){
        HashMap<Integer, Course> courseHashMap = new HashMap<>();
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT COURSES.ID_COURSE, NAME, DESCRIPTION, STARTDATE, CREDITS" +
                    " From COURSERELATION JOIN COURSES ON COURSERELATION.ID_COURSE = COURSES.ID_COURSE WHERE ID_USER = ?");
            statement.setInt(1,user_id);
            ResultSet resultSet = statement.executeQuery();
            int counter = 0;
            while (resultSet.next()){
                Course course = new Course();
                course.setID(String.valueOf(resultSet.getInt("ID_COURSE")));
                course.setName(resultSet.getString("NAME"));
                course.setDescription(resultSet.getString("DESCRIPTION"));
                course.setStartDate(convertToUtilDate(resultSet.getDate("STARTDATE")));
                course.setCredits(String.valueOf(resultSet.getInt("CREDITS")));
                courseHashMap.put(counter++,course);
            }

        } catch (Exception e){
            System.out.println("failed to get courses");
        }
        return courseHashMap;
    }


    public static boolean lecturerInCourse ( int user_course){
        boolean value = false;
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT role FROM USERS " +
                    "join COURSERELATION on ID_USER  = ID where ID_COURSE = ? ; ");
            statement.setInt(1,user_course);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                if (Roles.valueOf(resultSet.getString("ROLE")) == Roles.LECTURER){
                    value = true;
                }
        } catch (SQLException e){
        }
        return value;
    }



}
