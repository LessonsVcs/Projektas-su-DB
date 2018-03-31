package menu.extras.dbUtils;

import java.sql.*;

public class RelationDB {
    private static final String urlOfDB = "jdbc:h2:~/projektinis4";
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
}
