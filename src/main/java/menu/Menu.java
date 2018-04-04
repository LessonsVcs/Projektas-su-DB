package menu;

import extras.Roles;
import models.Login;

import static dbUtils.UserDB.getRole;


public class Menu {
    private String username;
    private Roles role;


    public void selectionMenu() {
        Login login = new Login();
        if (login.loginToSystem()) {
            this.username = login.getUsername();
            this.role = Roles.valueOf(getRole(false, username));
            selectMenu();
        }
    }

    private void selectMenu() {
        switch (role) {
            case ADMIN:
                MenuForAdmin menuForAdmin = new MenuForAdmin(username);
                menuForAdmin.menu();
                break;
            case LECTURER:
                MenuForLecturer menuForLecturer = new MenuForLecturer(username);
                menuForLecturer.menu();
                break;
            case USER:
                MenuForUser menuForUser = new MenuForUser(username);
                menuForUser.menu();
                break;
            default:
                return;

        }
    }
}
