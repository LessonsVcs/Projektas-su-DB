package menu;

import cources.Course;
import cources.ReadWriteCourseRelation;
import menu.extras.ScannerUntils;
import user.ReadWriteUserFile;
import user.User;
import menu.extras.PrintTable;
import menu.extras.Roles;
import menu.extras.UserInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static menu.extras.UpdateLists.*;

public class MenuForUser   implements UserInterface {
    private boolean running = true;
    private String myID;
    private HashMap<Integer,List<String>> courseRealtions = new HashMap<>();
    private HashMap<Integer, Course> courses= new HashMap();
    private HashMap<Integer, User> users = new HashMap();
    private PrintTable printTable = new PrintTable();
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    MenuForUser(String myID){
        this.myID=myID;
    }


    @Override
    public void menu() {
        while (running) {

            System.out.println("Select option");
            System.out.println("1) View all courses      2) Register to course      3) Show my courses     \n" +
                               "4) Change password       5) show my credits         6) Exit    ");
            selectOperation(Integer.parseInt(ScannerUntils.scanString("")));
        }
    }

    private void selectOperation(int option) {
        switch (option){
            case 1:
                viewCourses();
                break;
            case 2:
                register();
                break;
            case 3:
                showMyCourses();
                break;
            case 4:
                changePassword();
                break;
            case 5:
                System.out.println("Credits : "+countMyCredits());
                break;
            case 6:
                exit();
                break;

            default:
                System.out.println("Wrong input");
        }
    }

    @Override
    public void viewCourses() {
        //Prints out table : ID, Name, Description, start date
        this.courses = updateCourses();
        printTable.printCoursesHeader();
        for (Integer i: courses.keySet()) {
            printTable.printCoursesList(courses.get(i).getCourseID(),courses.get(i).getName(),courses.get(i).getDescription(),
                    format.format(courses.get(i).getStartDate()),courses.get(i).getCredits());
        }

    }

    @Override
    public void register() {
        this.courseRealtions = updateCourseRelations();
        this.users = updateUsers();
        this.courses = updateCourses();
        Date date =  Calendar.getInstance().getTime();

        boolean courseFound =  false;
        boolean lecturerFound = false;
        while (true) {
            String  input = ScannerUntils.scanString("Enter person id or exit");
            if (input.equalsIgnoreCase("exit")){
                break;
            }
            //Checks if course exists
            for (Integer i : courses.keySet()) {
                if(i==Integer.parseInt(input)){
                    courseFound = true;
                    if(courseRealtions.get(i).contains(myID)){
                        System.out.println("You can't register to same course twice");
                    } else {
                        registerToCourse(date, lecturerFound, i);
                    }
                    break;
                }
            }
            if (courseFound){
                break;
            }else {
                System.out.println("Course doesn't exist");
            }
        }

    }

    private void registerToCourse(Date date, boolean lecturerFound, Integer i) {
        if(isLecturerFound(lecturerFound, i)){
            if(courses.get(i).getStartDate().after(date)){
                if(countMyCredits()+Integer.parseInt(courses.get(i).getCredits())>=12){
                    System.out.println("you have to much credits to enroll this course");
                } else {
                    courseRealtions.get(i).add(myID);
                    ReadWriteCourseRelation readWriteCourseRelation = new ReadWriteCourseRelation();
                    readWriteCourseRelation.setCourseRealtions(courseRealtions);
                    readWriteCourseRelation.writeCourseRealation();
                }
            } else {
                System.out.println("Can't register to already started course");
            }
        } else {
            System.out.println("Can't register to course without Lecturer");
        }
    }

    private void changePassword(){
        users = updateUsers();
        users.get(Integer.parseInt(myID)).setPassword(ScannerUntils.scanString("Enter new password"));
        ReadWriteUserFile readWriteUserFile = new ReadWriteUserFile();
        readWriteUserFile.setUsers(users);
        readWriteUserFile.writeUserFile();

    }

    private boolean isLecturerFound(boolean lecturerFound, Integer i) {
        for(String id: courseRealtions.get(i)){
            if(users.get(Integer.parseInt(id)).getRole()== Roles.LECTURER){
                lecturerFound=true;
            }
        }
        return lecturerFound;
    }

    @Override
    public void showCourse() {
        this.courses = updateCourses();
        this.users   = updateUsers();

        boolean courseFound =  false;
        while (true) {
            String input = ScannerUntils.scanString("Enter course id or exit");
            if (input.equalsIgnoreCase("exit")){
                break;
            }
            //Checks if course exists
            for (Integer i : courses.keySet()) {
                if(i==Integer.parseInt(input)){
                    courseFound = true;
                    showSelectedCourse(i);
                    break;
                }
            }
            if (courseFound){
                break;
            }else {
                System.out.println("Course doesn't exist");
            }
        }

    }

    private void showSelectedCourse(Integer i) {
        //Prints out table who goes to course, First name, Last name, Role
        this.courseRealtions = updateCourseRelations();
        this.users = updateUsers();
        printTable.printDescription(courses.get(i).getName(), courses.get(i).getDescription());
        printTable.printCourseHeader();
        try {
            for (String line : courseRealtions.get(i)) {
                printTable.printCourse(users.get(Integer.parseInt(line)).getFirstName(),
                        users.get(Integer.parseInt(line)).getLastName(), users.get(Integer.parseInt(line)).getRole().toString());
            }
        } catch (Exception e) {
            System.out.println("There's no one in course ");
        }
    }

    private int countMyCredits(){
        this.courses = updateCourses();
        this.courseRealtions = updateCourseRelations();
        int credits=0;
        for(Integer i :courseRealtions.keySet()){
            if (courseRealtions.get(i).contains(myID)){
                credits+=Integer.parseInt(courses.get(i).getCredits());
            }
        }
        return credits;
    }

    @Override
    public void exit() {
        this.running = false;
    }

    private void showMyCourses(){
        //Prints out table : ID, Name, Description
        this.courses = updateCourses();
        printTable.printCoursesHeader();
        this.courseRealtions = updateCourseRelations();
        for(Integer i :courseRealtions.keySet()){
            if (courseRealtions.get(i).contains(myID)){
                printTable.printCoursesList(courses.get(i).getCourseID(),courses.get(i).getName(),courses.get(i).getDescription(),
                        format.format(courses.get(i).getStartDate()),courses.get(i).getCredits());
            }
        }
    }
}
