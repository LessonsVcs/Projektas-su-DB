package cources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class ReadWriteCourseFile {
    private String file;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private HashMap<Integer, Course> courses= new HashMap();
    public ReadWriteCourseFile(){
        this.file = "courses.csv";
    }
    public void readCoursefile(){
        try (
                BufferedReader br = new BufferedReader(new FileReader(this.file))
        ) {
            String line = br.readLine();
            while ((line= br.readLine()) != null){
                String[] lines= line.split(";");
                Date startDate = null;

                try {
                    startDate = format.parse(lines[3]);
                }catch (Exception e){
                    System.out.println(e);
                }
                this.courses.put(Integer.parseInt(lines[2]),new Course(lines[0],lines[1],lines[2],startDate,lines[4]));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setCourses(HashMap<Integer, Course> courses) {
        this.courses = courses;
    }

    public HashMap<Integer, Course> getCourses() {
        return courses;
    }

    public void writeCourseFile(){
        String lineToWrite;
        try (
                BufferedWriter bw = new BufferedWriter(new FileWriter(this.file))
        ) {
            /*String firstName, String lastName, String password, String username,
         String role, String email, Date dateOfBirth, Integer personalNumber,
         String address, StringArray courses){
    */

            bw.write("name, description, ID, start date \n");
            for (Integer i: courses.keySet()) {
                lineToWrite = this.courses.get(i).getName() + ";" + this.courses.get(i).getDescription()
                + ";" + this.courses.get(i).getCourseID() + ";" + format.format(this.courses.get(i).getStartDate())
                + ";" + this.courses.get(i).getCredits();
                bw.write(lineToWrite + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
