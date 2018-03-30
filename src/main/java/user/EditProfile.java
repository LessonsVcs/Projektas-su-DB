package user;

import menu.extras.ScannerUntils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class EditProfile {
    private HashMap<Integer, User> users;

    public void menu(Integer id, HashMap<Integer, User> users){
        this.users = users;
        boolean running = true;
        boolean changes = false;
        Scanner scanner = new Scanner(System.in);

        while (running){
            System.out.println("Select what to edit");
            System.out.println("1) first name     2) last name     3) password  \n" +
                               "4) date of birth  5) email         6) address   \n" +
                               "7) exit edit menu "                                );
            String input = ScannerUntils.scanString("");
            switch (Integer.parseInt(input)){
                case 1:
                    users.get(id).setFirstName(ScannerUntils.scanString("Enter new name"));
                    changes= true;
                    break;
                case 2:
                    users.get(id).setLastName(ScannerUntils.scanString("Enter new last name"));
                    changes= true;
                    break;
                case 3:
                    users.get(id).setPassword(ScannerUntils.scanString("Enter new password"));
                    changes= true;
                    break;
                case 4:
                    setDateOfBirth(id, users);
                    changes= true;
                    break;
                case 5:
                    users.get(id).setEmail(ScannerUntils.scanString("Enter new  email"));
                    changes= true;
                    break;
                case 6:
                    users.get(id).setAddress(ScannerUntils.scanString("Enter new address"));
                    changes = true;
                    break;
                case 7:
                    if(changes){
                        running = saveUsers(users);
                    } else {
                        running = false;
                    }
                    break;

                default:
                    System.out.println("Wrong input");
            }
        }
    }

    private boolean saveUsers(HashMap<Integer, User> users) {
        boolean running;
        while (true){
            String response = ScannerUntils.scanString("Changes are made, do you want to save? Yes/No");
            if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no")){
                running = false;
                if (response.equalsIgnoreCase("yes")){
                    ReadWriteUserFile readWriteUserFile = new ReadWriteUserFile();
                    readWriteUserFile.setUsers(users);
                    readWriteUserFile.writeUserFile();
                    break;
                } else {
                    break;
                }
            }
            else {
                System.out.println("wrong input");
            }
        }
        return running;
    }

    private void setDateOfBirth(Integer id, HashMap<Integer, User> users) {
        while (true){
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date = format.parse(ScannerUntils.scanString("enter new birth date. Year-Month-day Ex: 2000-10-10"));
                users.get(id).setDateOfBirth(date);
                break;
            }catch (Exception e){
                System.out.println("wrong input");
            }
        }
    }
}
