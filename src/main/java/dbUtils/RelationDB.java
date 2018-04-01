package dbUtils;

import extras.Roles;

import java.sql.*;

public class RelationDB {
    private static final String urlOfDB = "jdbc:h2:~/projektinis5";
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
                System.out.println("failed to delete models");
            }
        } else {
            try (
                    Connection con = DriverManager.getConnection(urlOfDB,login,login)
            ){
                PreparedStatement statement = con.prepareStatement("DELETE FROM COURSERELATION where ID_COURSE = ? ; ");
                statement.setInt(1,id);
                statement.execute();
                System.out.println("User deleted");
            } catch (SQLException e){
                System.out.println("failed to delete course");
            }
        }

    }

    public static void addToCourse(int user_id,int course_id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("INSERT INTO COURSERELATION  (ID_USER, ID_COURSE) " +
                    "VALUES (?,?,?,?,?); ", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1,user_id);
            statement.setInt(2,course_id);
            statement.execute();
            System.out.println("User added to course");


        } catch (Exception e){
            System.out.println("User wasn't added to course");
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
            System.out.println("failed to delete models from course");
        }
    }

    public static boolean isInCourse(int user_id, int user_course){

        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT * FROM COURSERELATION " +
                    "WHERE ID_USER  = ? AND ID_COURSE = ? ; ");
            statement.setInt(1,user_id);
            statement.setInt(2,user_course);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return true;
        } catch (SQLException e){
            return false;
        }
    }

    public static ResultSet getUserCourses(int user_id){
        ResultSet resultSet;
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT ID_COURSE, NAME, DESCRIPTION, STARTDATE, CREDITS" +
                    " From COURSERELATION JOIN COURSES ON COURSERELATION.ID_COURSE = COURSES.ID_COURSE WHERE ID_USER = ?");
            statement.setInt(1,user_id);
            resultSet = statement.executeQuery();
            return resultSet;

        } catch (Exception e){
            System.out.println(e);
            return null;
        }
    }


    public static boolean lecturerInCourse ( int user_course){

        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT role FROM USERS " +
                    "join COURSERELATION on ID_USER  = ID where ID_COURSE = ? ; ");
            statement.setInt(1,user_course);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                if (Roles.valueOf(resultSet.getString("ROLE")) == Roles.LECTURER){
                    return true;
                }
            return false;
        } catch (SQLException e){
            return false;
        }
    }



}
