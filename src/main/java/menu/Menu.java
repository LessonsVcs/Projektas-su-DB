package menu;

import user.Login;
import menu.extras.Roles;


import static menu.extras.dbUtils.UserDB.getRole;


public class Menu {
    private String username;
    private Roles role;


    public void selectionMenu(){
        Login login = new Login();
        if (login.loginToSystem()) {
            this.username = login.getUsername();
            this.role = Roles.valueOf(getRole(username));
            selectMenu();
        }
    }

    private void selectMenu(){
        switch (role){
            case ADMIN:
                MenuForAdmin menuForAdmin = new MenuForAdmin();
                menuForAdmin.menu();
                break;
            case LECTURER:
                MenuForLecturer menuForLecturer = new MenuForLecturer();
                menuForLecturer.menu();
                break;
            case USER:
                MenuForUser menuForUser = new MenuForUser();
                menuForUser.menu();
                break;
            default:
                return;

        }
    }
}
