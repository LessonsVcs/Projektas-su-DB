package menu;

import extras.ScannerUntils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import static dbUtils.UserDB.*;

public class EditProfile {

    public void menu(Integer id){
        boolean running = true;

        while (running){
            System.out.println("Select what to edit");
            System.out.println("1) first name     2) last name     3) password  \n" +
                               "4) date of birth  5) email         6) address   \n" +
                               "7) exit edit menu "                                );
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
                    setDateOfBirth(id);
                    break;
                case 5:
                    editUserEmail(ScannerUntils.scanString("Enter new  email"),id);
                    break;
                case 6:
                    editUserAddress(ScannerUntils.scanString("Enter new address"),id);
                    break;
                case 7:
                    running = false;
                    break;

                default:
                    System.out.println("Wrong input");
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
}
