package menu;

import models.Course;
import dbUtils.RelationDB;
import models.Login;
import extras.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import static dbUtils.CourseDB.*;
import static dbUtils.RelationDB.addToCourse;
import static dbUtils.RelationDB.getUserCourses;
import static dbUtils.RelationDB.isInCourse;
import static dbUtils.UserDB.*;

public class MenuForLecturer  implements LecturerInterface,UserInterface {
    private int myID;
    private String username;
    private PrintTable printTable = new PrintTable();
    private boolean running= true;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    MenuForLecturer(){
        Login login = new Login();
        this.username= login.getUsername();
        this.myID=getUserID(username);
    }

    @Override
    public void menu() {
        while (running) {
            System.out.println("Select option");
            System.out.println("1) edit my profile      2) edit my courses     3) View all courses      \n" +
                               "4) Create course        5) View users          6) Register to course    \n" +
                               "7) Show course          8) Show my courses     9) Exit                  \n" );
            selectOperation(Integer.parseInt(ScannerUntils.scanString("")));
        }
    }

    private void selectOperation(int option) {
        switch (option){
            case 1:
                editUser();
                break;
            case 2:
                editCourses();
                break;
            case 3:
                viewCourses();
                break;
            case 4:
                addCourse();
                break;
            case 5:
                viewUsers();
                break;
            case 6:
                register();
                break;
            case 7:
                showCourse();
                break;
            case 8:
                showMyCourses();
                break;
            case 9:
                exit();
                break;

            default:
                System.out.println("Incorrect input");
        }
    }

    @Override
    public void editUser() {
        EditProfile editProfile = new EditProfile();
        editProfile.menu(myID);
    }

    @Override
    public void editCourses() {
        while (true) {
            String course_id = ScannerUntils.scanString("Enter course id");
            if(courseExist(course_id)){
                String user_id = ScannerUntils.scanString("Enter course id");
                if(isInCourse(Integer.parseInt(user_id),Integer.parseInt(course_id))) {
                    editCourseMenu(Integer.parseInt(course_id));
                } else {
                    System.out.println("You can't edit this course");
                }
                break;
            } else {
                System.out.println("Course doesn't exist");
            }
        }

    }

    @Override
    public void viewCourses() {
        //Prints out table : ID, Name, Description, start date
        ResultSet courses = getCourses();
        printTable.printCoursesHeader();
        try {
            while (courses.next()) {
                printTable.printCoursesList(courses.getString("ID_COURSE"),courses.getString("NAME"),
                        courses.getString("DESCRIPTION"), format.format(courses.getDate("STARTDATE")),
                        courses.getString("CREDITS"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void showMyCourses(){
        //Prints out table : ID, Name, Description
        ResultSet courses = getUserCourses(myID);
        printTable.printCoursesHeader();
        try {
            while (courses.next()){
                printTable.printCoursesList(courses.getString("ID_COURSE"), courses.getString("NAME"),
                        courses.getString("DESCRIPTION"), format.format(courses.getString("STARTDATE")),
                        courses.getString("CREDITS"));

            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void addCourse() {
        Course newCourse = new Course();
        newCourse = creatingNewCourse(newCourse);
        newCourseDB(newCourse.getName(),newCourse.getDescription(),newCourse.getStartDate(),newCourse.getCredits());
        addToCourse(myID,getCourseID(newCourse.getName()));
    }

    private Course creatingNewCourse(Course newCourse) {
        String name;
        while (true){
            name = ScannerUntils.scanString("Enter course name or 'exit' to leave");
            if (name.equalsIgnoreCase("exit")){
            }
            if(courseNameExist(name)){
                System.out.println("This name is already exist");
            } else {
                newCourse.setName(name);
                break;
            }
        }
        while (true){
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                newCourse.setStartDate(format.parse(ScannerUntils.scanString("Enter start date yyyy-MM-dd")));
                break;
            }catch (Exception e){
                System.out.println("Wrong format");
            }
        }
        newCourse.setDescription(ScannerUntils.scanString("Enter description"));
        newCourse.setCredits(ScannerUntils.scanString("Enter credits"));
        return newCourse;
    }


//    private Date getDate() {
//        Date startDate;
//        while (true){
//            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//            try {
//                startDate = format.parse(ScannerUntils.scanString("Enter start date yyyy-MM-dd"));
//                break;
//            }catch (Exception e){
//                System.out.println("Wrong format");
//            }
//        }
//        return startDate;
//    }

    @Override
    public void viewUsers() {
        ResultSet users = getUsers();
        printTable.printUserHeader();
        try {
            while (users.next()) {
                printTable.printUserList(users.getString("ID"),users.getString("NAME"),
                        users.getString("LASTNAME"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void register() {
        while (true) {
            String course_id = ScannerUntils.scanString("Enter course id or exir");
            if(course_id.equalsIgnoreCase("exit")){
                break;
            }
            if(courseExist(course_id)){
                if(isInCourse(myID,Integer.parseInt(course_id))){
                    String user_id = ScannerUntils.scanString("Enter course id or exir");
                    if(userExist(user_id)){
                        Roles userRole= Roles.valueOf(getRole(user_id));
                        if(userRole!= Roles.ADMIN || userRole!= Roles.LECTURER ){
                            RelationDB.addToCourse(Integer.parseInt(user_id),Integer.parseInt(course_id));
                            break;
                        } else {
                            System.out.println("Can't add models with admin/lecturer role");
                        }
                    }
                } else {
                    System.out.println("You can't register to not your courses");
                }
                break;
            } else {
                System.out.println("Course doesn't exist");
            }
        }
    }

    @Override
    public void showCourse() {
        while (true) {
            String input = ScannerUntils.scanString("Enter course id or exit");
            if (input.equalsIgnoreCase("exit")){
                break;
            }
            if(courseExist(input)){
                showSelectedCourse(Integer.parseInt(input));
                break;
            }else {
                System.out.println("Course doesn't exist");
            }
        }
    }

    @Override
    public void exit() {
        this.running = false;
    }



    private void showSelectedCourse(Integer i){
        //Prints out table who goes to course, First name, Last name, Role
        ResultSet users = getUsersInCourses(i);
        ResultSet course = getUsersInCourses(i);
        try {
            printTable.printDescription(course.getString("NAME"),course.getString("DESCRIPTION"));
            printTable.printCourseHeader();
            while (users.next()) {
                printTable.printCourse(users.getString("NAME"),
                        users.getString("LASTNAME"), users.getString("ROLE"));
            }
        } catch (Exception e){
            System.out.println("There's no one in course ");
        }
    }

    private void editCourseMenu(Integer id){
        boolean running = true;
        //Menu for editing course
        while (running){
            String input = ScannerUntils.scanString("1) Change name 2) Change description 3) Change start Date 4) Exit");
            switch (Integer.parseInt(input)){
                case 1:
                    editCourseName(ScannerUntils.scanString("Enter new name"),id);
                    break;
                case 2:
                    editCourseDescription(ScannerUntils.scanString("Enter new course description"),id);
                    break;
                case 3:
                    changeDate(id);
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Wrong input");
            }
        }

    }

    private void changeDate(Integer id ) {
        while (true){
            try {
                editCourseDate(format.parse(ScannerUntils.scanString("Enter start date yyyy-MM-dd")),id);
                break;
            }catch (Exception e){
                System.out.println("Wrong format");
            }
        }
    }
}
