package menu;

import user.Login;
import menu.extras.Roles;


public class Menu {
    private String ID;
    private Roles role;


    public void selectionMenu(){
        Login login = new Login();
        login.loginToSystem();
        try {
            this.ID   = login.getID();
            this.role = login.getRole();
        }catch (Exception e){
            System.out.println("Goodbye");
        }

        selectMenu();
    }

    private void selectMenu(){
        switch (role){
            case ADMIN:
                MenuForAdmin menuForAdmin = new MenuForAdmin(ID);
                menuForAdmin.menu();
                break;
            case LECTURER:
                MenuForLecturer menuForLecturer = new MenuForLecturer(ID);
                menuForLecturer.menu();
                break;
            case USER:
                MenuForUser menuForUser = new MenuForUser(ID);
                menuForUser.menu();
                break;
            default:
                return;

        }
    }
}
