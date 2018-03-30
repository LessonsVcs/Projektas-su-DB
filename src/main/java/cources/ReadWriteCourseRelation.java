package cources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ReadWriteCourseRelation {
    private HashMap<Integer,List<String>> courseRealtions = new HashMap<>();
    private String file;
    public ReadWriteCourseRelation (){
        this.file = "CourseRelations.csv";
    }

    public void ReadCourseRealation(){
        try (
                BufferedReader br = new BufferedReader(new FileReader(this.file))
        ) {
            String line = br.readLine();
            Integer index = 0 ;
            Integer tempInt;
            while ((line= br.readLine()) != null){
                List<String> list = new ArrayList<>();
                String[] lines= line.split(";");

                try {
                    tempInt=Integer.parseInt(lines[0]);
                    String[] idList = lines[1].split(",");
                    for (int i = 0; i < idList.length; i++) {
                        list.add(idList[i]);
                    }
                    this.courseRealtions.put(tempInt,list);
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void writeCourseRealation(){
        String lineToWrite;
        try (
                BufferedWriter bw = new BufferedWriter(new FileWriter(this.file))
        ) {
            /*String firstName, String lastName, String password, String username,
         String role, String email, Date dateOfBirth, Integer personalNumber,
         String address, StringArray courses){
    */
            bw.write("UserID, Courses IDs \n");
            for (Integer i: courseRealtions.keySet()) {

                try {
                    String temp = i.toString();
                    String temp2 = String.join(",",courseRealtions.get(i));
                    bw.write(temp + ";" +temp2 + "\n");

                }catch (Exception e){
                    System.out.println(e);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public HashMap<Integer, List<String>> getCourseRealtions() {
        return courseRealtions;
    }

    public void setCourseRealtions(HashMap<Integer, List<String>> courseRealtions) {
        this.courseRealtions = courseRealtions;
    }
}
