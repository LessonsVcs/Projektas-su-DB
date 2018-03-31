package menu.extras.dbUtils;

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
                System.out.println("failed to delete user");
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
            System.out.println("failed to delete user from course");
        }
    }



}
