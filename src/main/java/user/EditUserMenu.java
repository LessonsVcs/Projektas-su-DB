package user;

import menu.extras.Roles;
import menu.extras.ScannerUntils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static menu.extras.dbUtils.DBUtils.*;

public class EditUserMenu {

    public void menu(Integer id){

        boolean running = true;

        while (running){
            System.out.println("Select what to edit");
            System.out.println("1) first name    2) last name     3) password  \n" +
                    "4) username      5) date of birth 6) email     \n" +
                    "7) address       8) change role                \n" +
                    "9) exit edit menu   ");
            String input = ScannerUntils.scanString("");
            switch (Integer.parseInt(input)){
                case 1:
                    editUserName(ScannerUntils.scanString("Enter new name"),id);
                    break;
                case 2:
                    editUserLastname(ScannerUntils.scanString("Enter new last name"),id);
                    break;
                case 3:
                    editUserPassword(ScannerUntils.scanString("Enter new password"),id);
                    break;
                case 4:
                    changeUsername(id);
                    break;
                case 5:
                    setDateOfBirth(id);

                    break;
                case 6:
                    editUserEmail(ScannerUntils.scanString("Enter new  email"),id);

                    break;
                case 7:
                    editUserAddress(ScannerUntils.scanString("Enter new address"),id);

                    break;
                case 8:
                    changeRole(id);

                    break;
                case 9:
                    running = false;
                    break;

                default:
                    System.out.println("Wrong input");
            }
        }
    }

    private void changeRole(Integer id) {

        while (true){
            String tmp = ScannerUntils.scanString("Enter role : admin, user, lecturer");
            if(tmp.equalsIgnoreCase("admin") || tmp.equalsIgnoreCase("user") ||
                    tmp.equalsIgnoreCase("lecturer")){
                if(tmp.equalsIgnoreCase("admin")){
                    editUserRole(Roles.ADMIN,id);
                    break;
                } if(tmp.equalsIgnoreCase("lecturer" )){
                    editUserRole(Roles.LECTURER,id);
                    break;
                } if(tmp.equalsIgnoreCase("user" )) {
                    editUserRole(Roles.USER,id);
                    break;
                } else{
                    System.out.println("can't change to same role");
                }
            } else {
                System.out.println("this role doesn't exist");
            }
        }
    }

    private void setDateOfBirth(Integer id) {
        while (true){
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date = format.parse(ScannerUntils.scanString("enter new birth date. Year-Month-day Ex: 2000-10-10"));
                editUserDate(date,id);
                break;
            }catch (Exception e){
                System.out.println("wrong input");
            }
        }
    }

    private void changeUsername(Integer id) {
        while (true){
            String username = ScannerUntils.scanString("Enter new username");
            if (checkUsername(username)!=null){
                System.out.println("this username already exist");
                break;
            } else {
                editUserUsername(username,id);
                break;
            }

        }
    }

}
