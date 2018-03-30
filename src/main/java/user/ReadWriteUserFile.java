package user;

import menu.extras.Roles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ReadWriteUserFile {
    /*String firstName, String lastName, String password, String username,
         String role, String email, Date dateOfBirth, Integer personalNumber,
         String address, StringArray courses){
    */

    public HashMap<Integer, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<Integer, User> users) {
        this.users = users;
    }
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private HashMap<Integer,User> users = new HashMap();
    private String file;
    public ReadWriteUserFile(){

        this.file = "users.csv";
    }
    public void readUserFile(){
        try (
                BufferedReader br = new BufferedReader(new FileReader(this.file))
        ) {
            String line = br.readLine();
            Integer index = 0 ;
            Date date;
            while ((line= br.readLine()) != null){
//                line = br.readLine();
                String[] lines= line.split(";");
                try {

                    date = format.parse(lines[6]);
                }catch (Exception e){
                    date = null;
                }
                this.users.put(Integer.parseInt(lines[8]),new User(lines[0], lines[1], lines[2], lines[3], Roles.valueOf(lines[4]),
                        lines[5], date, lines[7], lines[8]));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void writeUserFile(){
        String date;
        String lineToWrite;
        try (
                BufferedWriter bw = new BufferedWriter(new FileWriter(this.file))
        ) {
            /*String firstName, String lastName, String password, String username,
         String role, String email, Date dateOfBirth, Integer personalNumber,
         String address, StringArray courses){
    */
            bw.write("firstName, lastName, password, username, role, email, dateOfBirth, address, personalNumber \n");
            for (Integer i : users.keySet() ){
                try {
                    date = format.format(users.get(i).getDateOfBirth());
                }catch (Exception e){
                    date = null;
                }
                lineToWrite = users.get(i).getFirstName() + ";" + users.get(i).getLastName() + ";" +
                        users.get(i).getPassword() + ";" + users.get(i).getUsername() + ";" +
                        users.get(i).getRole().toString() + ";" + users.get(i).getEmail() + ";" + date
                         + ";" + users.get(i).getAddress() + ";" + users.get(i).getPersonalNumber();
                bw.write(lineToWrite + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
