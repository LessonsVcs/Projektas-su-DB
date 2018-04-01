package dbUtils;

import extras.Roles;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static dbUtils.DBUtils.convertToMysqlDate;
import static dbUtils.RelationDB.removeFromRelation;

public class UserDB {
    private static final String urlOfDB = "jdbc:h2:~/projektinis5";
    private static final String login = "admin";
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


    public static void newUserToDB(String name, String lastName, String password, String userName, Roles role){

        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("INSERT INTO Users (Name,LastName,Password,Username,Role) " +
                    "VALUES (?,?,?,?,?); ");
            statement.setString(1,name);
            statement.setString(2,lastName);
            statement.setString(3,password);
            statement.setString(4,userName);
            statement.setString(5,role.toString());
            statement.execute();
//            ResultSet generatedKeys = statement.getGeneratedKeys();
//            if (generatedKeys.next()) {
//                long id = generatedKeys.getLong(1);
//                System.out.println(id);
//            } else {
//                System.out.println("Error on retrieving ID");
//            }
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public static void newUserToDBexpress(String name, String lastName, String password, String userName, Roles role,
                                          String email, java.util.Date dateOfBirth, String address){

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
            statement.setDate(7,convertToMysqlDate(dateOfBirth));
            statement.setString(8,address);

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

    public static String getRole(String input){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT role from Users where username = ? ; ");
            statement.setString(1,input);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getString("role");
        } catch (SQLException e){
            return null;
        }
    }

    public static void deleteUserDB(String input){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("DELETE FROM Users where ID = ? ; ");
            statement.setInt(1,Integer.parseInt(input));
            statement.execute();
            removeFromRelation(true,Integer.parseInt(input));
            System.out.println("User deleted");
        } catch (SQLException e){
            System.out.println("failed to delete models");
        }
    }

    public static boolean userExist(String input){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("Select ID FROM Users where ID = ? ;");
            statement.setInt(1,Integer.parseInt(input));
            statement.executeQuery();
            return true;
        } catch (SQLException e){
            return false;
        }
    }

    public static void editUserName(String input, Integer id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("UPDATE Users SET name = ? WHERE ID = ?; ");
            statement.setString(1,input);
            statement.setInt(2,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();

        }

    }

    public static void editUserLastname(String input, Integer id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("UPDATE Users SET lastname = ? WHERE ID = ?; ");
            statement.setString(1,input);
            statement.setInt(2,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();

        }

    }

    public static void editUserPassword(String input, Integer id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("UPDATE Users SET password = ? WHERE ID = ?; ");
            statement.setString(1,input);
            statement.setInt(2,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();

        }

    }

    public static void editUserEmail(String input, Integer id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("UPDATE Users SET email = ? WHERE ID = ?; ");
            statement.setString(1,input);
            statement.setInt(2,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();

        }

    }

    public static void editUserAddress(String input, Integer id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("UPDATE Users SET address = ? WHERE ID = ?; ");
            statement.setString(1,input);
            statement.setInt(2,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();

        }

    }

    public static void editUserRole(Roles input, Integer id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("UPDATE Users SET role = ? WHERE ID = ?; ");
            statement.setString(1,input.toString());
            statement.setInt(2,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();

        }

    }

    public static void editUserUsername(String input, Integer id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("UPDATE Users SET username = ? WHERE ID = ?; ");
            statement.setString(1,input);
            statement.setInt(2,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();

        }

    }

    public static void editUserDate(java.util.Date input, Integer id){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("UPDATE Users SET dateofbirth = ? WHERE ID = ?; ");
            statement.setDate(1,convertToMysqlDate(input));
            statement.setInt(2,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();

        }

    }

    public static ResultSet getUsers(){
        ResultSet resultSet;
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT ID, NAME, LASTNAME from Users; ");
            resultSet = statement.executeQuery();
            return resultSet;

        } catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public static int getUserID(String username){
        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT ID from Users where username = ? ; ");
            statement.setString(1,username);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("ID");
        } catch (SQLException e){
            return 0;
        }
    }

    public static int getUserCredits(int ID){

        try (
                Connection con = DriverManager.getConnection(urlOfDB,login,login)
        ){
            PreparedStatement statement = con.prepareStatement("SELECT sum(CREDITS) FROM COURSES " +
                    "join COURSERELATION on COURSERELATION .ID_COURSE = COURSES .ID_COURSE where ID_USER  = ?; ");
            statement.setInt(1,ID);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("SUM(CREDITS)");
        } catch (SQLException e){
            return 0;
        }
    }

}
