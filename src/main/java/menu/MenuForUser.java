package menu;

import menu.extras.ScannerUntils;
import user.Login;
import menu.extras.PrintTable;
import menu.extras.UserInterface;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static menu.extras.dbUtils.CourseDB.*;
import static menu.extras.dbUtils.RelationDB.*;
import static menu.extras.dbUtils.UserDB.editUserPassword;
import static menu.extras.dbUtils.UserDB.getUserCredits;
import static menu.extras.dbUtils.UserDB.getUserID;

public class MenuForUser   implements UserInterface {
    private final String username;
    private boolean running = true;
    private int myID;
    private PrintTable printTable = new PrintTable();
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    MenuForUser(){
        Login login = new Login();
        this.username= login.getUsername();
        this.myID=getUserID(username);
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
                System.out.println("Credits : "+getUserCredits(myID));
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

    @Override
    public void register() {
        while (true) {
            String  course_id = ScannerUntils.scanString("Enter person id or exit");
            if (course_id.equalsIgnoreCase("exit")){
                break;
            } else if(courseExist(course_id)){
                registerToCourse(course_id);
                break;
            } else {
                System.out.println("Course doesn't exist");
            }
        }

    }

    private void registerToCourse(String course_id) {
        if(isInCourse(myID,Integer.parseInt(course_id))){
            System.out.println("You can't register to same course twice");
        } else {
            if(lecturerInCourse(Integer.parseInt(course_id))){
            } else {
                System.out.println("Can't register to course without lecturer");
            }
            if(getCourseStartDate(Integer.parseInt(course_id)).after(Calendar.getInstance().getTime())){
                if(getUserCredits(myID)+getCourseCredits(Integer.parseInt(course_id))>=12){
                    System.out.println("you have to much credits to enroll this course");
                } else {
                    addToCourse(myID,Integer.parseInt(course_id));
                }
            } else {
                System.out.println("Can't register to already started course");
            }
        }
    }


    private void changePassword(){
        editUserPassword(ScannerUntils.scanString("Enter new password"),myID);
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

    @Override
    public void exit() {
        this.running = false;
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
}
