package user;

import menu.extras.Roles;
import menu.extras.ScannerUntils;

import java.util.HashMap;

import static menu.extras.DBUtils.checkUsername;

public class Login {
    private ReadWriteUserFile readWriteUserFile = new ReadWriteUserFile();
    private HashMap <Integer,User> users = readWriteUserFile.getUsers();
    private String ID;
    private Roles role;
    public void loginToSystem(){
        readWriteUserFile.readUserFile();

        checkLogin();

    }
    private void checkLogin(){
        String username = ScannerUntils.scanString("Enter username");
        String password = checkUsername(username);
        if (password.equals(null)){
            System.out.println("user doesn't exit");
            checkLogin();
        }
        else {
            checkPassword(password);

        }
    }

    private void checkPassword(String password){

        String input = ScannerUntils.scanString("Enter password");
        if (password.equals(input)){
            System.out.println("login successful");

        } else {
            System.out.println("wrong password");
            checkLogin();
        }
    }

    private void setCurrentUserInfo(Integer i) {
        this.ID = i.toString();
        this.role = users.get(i).getRole();
    }

    public String getID() {
        return ID;
    }

    public Roles getRole() {
        return role;
    }
}
