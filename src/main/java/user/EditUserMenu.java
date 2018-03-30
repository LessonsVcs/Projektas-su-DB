package user;

import menu.extras.Roles;
import menu.extras.ScannerUntils;
import user.ReadWriteUserFile;
import user.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class EditUserMenu {
    private HashMap<Integer, User> users;

    public void menu(Integer id, HashMap<Integer, User> users){
        this.users = users;
        boolean running = true;
        boolean changes = false;
        Scanner scanner = new Scanner(System.in);

        while (running){
            System.out.println("Select what to edit");
            System.out.println("1) first name    2) last name     3) password  \n" +
                    "4) username      5) date of birth 6) email     \n" +
                    "7) address       8) change role                \n" +
                    "9) exit edit menu   ");
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
                    changeUsername(id, users);
                    changes= true;
                    break;
                case 5:
                    setDateOfBirth(id, users);
                    changes= true;
                    break;
                case 6:
                    users.get(id).setEmail(ScannerUntils.scanString("Enter new  email"));
                    changes= true;
                    break;
                case 7:
                    users.get(id).setAddress(ScannerUntils.scanString("Enter new address"));
                    changes = true;
                    break;
                case 8:
                    changeRole(id, users);
                    changes = true;
                    break;
                case 9:
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

    private void changeRole(Integer id, HashMap<Integer, User> users ) {

        while (true){
            String tmp = ScannerUntils.scanString("Enter role : admin, user, lecturer");
            if(tmp.equalsIgnoreCase("admin") || tmp.equalsIgnoreCase("user") ||
                    tmp.equalsIgnoreCase("lecturer")){
                if(tmp.equalsIgnoreCase("admin") && users.get(id).getRole()!= Roles.ADMIN){
                    users.get(id).setRole(Roles.ADMIN);
                    break;
                } if(tmp.equalsIgnoreCase("lecturer" )&& users.get(id).getRole()!=Roles.LECTURER){
                    users.get(id).setRole(Roles.LECTURER);
                    break;
                } if(tmp.equalsIgnoreCase("user" )&& users.get(id).getRole()!=Roles.USER) {
                    users.get(id).setRole(Roles.USER);
                    break;
                } else{
                    System.out.println("can't change to same role");
                }
            } else {
                System.out.println("this role doesn't exist");
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

    private void changeUsername(Integer id, HashMap<Integer, User> users) {
        while (true){
            String username = ScannerUntils.scanString("Enter new username");
            if (checkName(username)){
                System.out.println("this username already exist");
                break;
            } else {
                users.get(id).setUsername(username);
                break;
            }

        }
    }

    private boolean checkName(String input){
        //returns true if username exists
        for (Integer i : users.keySet()) {
            if (input.equalsIgnoreCase(users.get(i).getUsername())){
                return true;
            }
        }
        return false;
    }
}
