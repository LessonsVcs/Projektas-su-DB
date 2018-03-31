package user;

import menu.extras.ScannerUntils;

import static menu.extras.dbUtils.DBUtils.checkUsername;

public class Login {
    private String username;

    public boolean loginToSystem() {
        return checkLogin();
    }

    private boolean checkLogin() {
        boolean success = false;
        String username = ScannerUntils.scanString("Enter username");
        String password = checkUsername(username);
        if (password.equals(null)) {
            System.out.println("user doesn't exit");
            checkLogin();
        } else {
            this.username = username;
            success = checkPassword(password);

        }
        return success;
    }

    private boolean checkPassword(String password) {

        String input = ScannerUntils.scanString("Enter password");
        if (password.equals(input)) {
            System.out.println("login successful");
            return true;

        } else {
            System.out.println("wrong password");
            checkLogin();
            return false;
        }
    }

    public String getUsername() {
        return username;
    }
}

