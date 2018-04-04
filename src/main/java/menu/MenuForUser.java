package menu;

import extras.PrintTable;
import extras.ScannerUntils;
import extras.UserInterface;
import models.Course;
import models.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static dbUtils.CourseDB.*;
import static dbUtils.RelationDB.*;
import static dbUtils.UserDB.*;

public class MenuForUser implements UserInterface {
    private final String username;
    private boolean running = true;
    private int myID;
    private PrintTable printTable = new PrintTable();
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    MenuForUser(String username) {
        this.username = username;
        this.myID = getUserID(this.username);
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
        switch (option) {
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
                System.out.println("Credits : " + getUserCredits(myID));
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
        //Prints out table : ID, Name, Description
        HashMap<Integer, Course> courseHashMap = getCourses();
        printTable.printCoursesHeader();
        for (Integer i : courseHashMap.keySet()) {
            printTable.printCoursesList(courseHashMap.get(i).getID(), courseHashMap.get(i).getName(),
                    courseHashMap.get(i).getDescription(), format.format(courseHashMap.get(i).getStartDate()),
                    courseHashMap.get(i).getCredits());
        }
    }

    @Override
    public void register() {
        while (true) {
            String course_id = ScannerUntils.scanString("Enter course id or exit");
            if (course_id.equalsIgnoreCase("exit")) {
                break;
            } else if (courseExist(course_id)) {
                registerToCourse(course_id);
                break;
            } else {
                System.out.println("Course doesn't exist");
            }
        }

    }

    private void registerToCourse(String course_id) {
        if (isInCourse(myID, Integer.parseInt(course_id))) {
            System.out.println("You can't register to same course twice");
        } else {
            if (lecturerInCourse(Integer.parseInt(course_id))) {
            } else {
                System.out.println("Can't register to course without lecturer");
            }
            if (getCourseStartDate(Integer.parseInt(course_id)).after(Calendar.getInstance().getTime())) {
                if (getUserCredits(myID) + getCourseCredits(Integer.parseInt(course_id)) >= 12) {
                    System.out.println("you have to much credits to enroll this course");
                } else {
                    addToCourse(myID, Integer.parseInt(course_id));
                }
            } else {
                System.out.println("Can't register to already started course");
            }
        }
    }


    private void changePassword() {
        editUserPassword(ScannerUntils.scanString("Enter new password"), myID);
    }


    @Override
    public void showCourse() {
        while (true) {
            String input = ScannerUntils.scanString("Enter course id or exit");
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            if (courseExist(input)) {
                showSelectedCourse(Integer.parseInt(input));
                break;
            } else {
                System.out.println("Course doesn't exist");
            }
        }
    }

    private void showSelectedCourse(Integer i) {
        //Prints out table who goes to course, First name, Last name, Role
        HashMap<Integer, User> users = getUsersInCourses(i);
        Course course = getCourseInfo(i);
        try {
            printTable.printDescription(course.getName(), course.getDescription());
            printTable.printCourseHeader();
            for (Integer j : users.keySet()) {
                printTable.printCourse(users.get(j).getFirstName(),
                        users.get(j).getLastName(), users.get(j).getRole().toString());
            }
        } catch (Exception e) {
            System.out.println("There's no one in course ");
        }
    }

    @Override
    public void exit() {
        this.running = false;
    }

    private void showMyCourses() {
        //Prints out table : ID, Name, Description
        HashMap<Integer, Course> courses = getUserCourses(myID);
        printTable.printCoursesHeader();
        try {
            for (Integer i : courses.keySet()) {
                printTable.printCoursesList(courses.get(i).getID(), courses.get(i).getName(),
                        courses.get(i).getDescription(), format.format(courses.get(i).getStartDate()),
                        courses.get(i).getCredits());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
