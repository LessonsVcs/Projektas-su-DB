package menu;

import cources.Course;
import user.EditUserMenu;
import user.Login;
import user.User;
import menu.extras.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static menu.extras.dbUtils.CourseDB.*;
import static menu.extras.dbUtils.RelationDB.addToCourse;
import static menu.extras.dbUtils.RelationDB.removeFromCourse;
import static menu.extras.dbUtils.UserDB.*;

public class MenuForAdmin implements AdminInterface,LecturerInterface,UserInterface {
    private String username;
    private int myID;
    private boolean running = true;
    private PrintTable printTable = new PrintTable();
    MenuForAdmin(){
        Login login = new Login();
        this.username= login.getUsername();
        this.myID=getUserID(username);
    }

    public void menu(){
        //Menu for selecting operation
        while (running) {
            System.out.println("Select option");
            System.out.println("1) create user      2) delete user      3) edit user      \n" +
                    "4) delete course    5) create course    6) show user list \n" +
                    "7) register user to course    8) remove user from course  \n" +
                    "9) show course list 10)show course     11) Edit course    \n" +
                    "12)Exit");
            selectOperation(ScannerUntils.scanString(""));
        }
    }

    private void selectOperation(String selected) {
        //Selecting operation from menu
        switch (Integer.parseInt(selected)){
            case 1:
                createUser();
                break;
            case 2:
                deleteUser();
                break;
            case 3:
                editUser();
                break;
            case 4:
                deleteCourse();
                break;
            case 5:
                addCourse();
                break;
            case 6:
                viewUsers();
                break;
            case 7:
                register();
                break;
            case 8:
                removeUserFromCourse();
                break;
            case 9:
                viewCourses();
                break;
            case 10:
                showCourse();
                break;
            case 11:
                editCourses();
                break;
            case 12:
                exit();
                break;
            default:
                System.out.println("Wrong input");
        }
    }

    @Override
    public void exit() {
        //exist admin menu
        this.running = false;
    }

    @Override
    public void createUser() {
        User newUser = new User();
        String input = ScannerUntils.scanString("Create simple user? Yes/No or exit");
        //Create user simple or express. Simple doesn't have: email, dateOfBirth, address,
        if (input.equalsIgnoreCase("exit")){
            return;
        }else {
            if (input.equalsIgnoreCase("yes")) {
                //Check if username is free
                newUser = basicUserCreation(newUser);
                newUserToDB(newUser.getFirstName(),newUser.getLastName(),newUser.getPassword(),
                        newUser.getUsername(), newUser.getRole());

            } else if (input.equalsIgnoreCase("no")) {
                newUser = basicUserCreation(newUser);
                newUser.setEmail(ScannerUntils.scanString("enter email"));
                newUser.setAddress(ScannerUntils.scanString("enter address"));
                newUser.setDateOfBirth(getBirthDate());
                newUserToDBexpress(newUser.getFirstName(),newUser.getLastName(),newUser.getPassword(),
                        newUser.getUsername(), newUser.getRole(), newUser.getEmail(), newUser.getDateOfBirth(), newUser.getAddress());
            } else {
                System.out.println("wrong input");
                createUser();
            }
        }


    }

    private User basicUserCreation(User newUser) {
        newUser.setUsername(getUsername());
        newUser.setPassword(ScannerUntils.scanString("enter password"));
        newUser.setFirstName(ScannerUntils.scanString("enter first name"));
        newUser.setLastName(ScannerUntils.scanString("enter last name"));
        //Assign role from ENUM
        newUser.setRole(getRoles());
        return newUser;
    }

    private String getUsername() {
        String username;
        while (true){
            System.out.println();
            username = ScannerUntils.scanString("enter username");
            if(checkUsername(username)!=null){
                System.out.println("this username is already taken");
            } else {
                break;
            }
        }
        return username;
    }

    private Roles getRoles() {
        Roles role = Roles.USER;
        while (true){
            System.out.println();
            String tmp = ScannerUntils.scanString("enter role");
            if(tmp.equalsIgnoreCase("admin") || tmp.equalsIgnoreCase("user") ||
                    tmp.equalsIgnoreCase("lecturer")){
                if(tmp.equalsIgnoreCase("admin")){
                    role = Roles.ADMIN;
                }
                if(tmp.equalsIgnoreCase("lecturer")){
                    role = Roles.LECTURER;
                }
                if(tmp.equalsIgnoreCase("user")){
                    role = Roles.USER;
                }
                else {
                    System.out.println("wrong input");
                }
                break;
            } else {
                System.out.println("this role doesn't exist");
            }
        }
        return role;
    }

    private Date getBirthDate() {
        Date dateOfBirth;
        while (true){
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                dateOfBirth = format.parse(ScannerUntils.scanString("enter new birth date. Year-Month-day Ex: 2000-10-10"));
                break;
            }catch (Exception e){
                System.out.println("wrong input");
            }
        }
        return dateOfBirth;
    }

    @Override
    public void addCourse() {
        Course newCourse = new Course();
        newCourse = creatingNewCourse(newCourse);

        newCourseDB(newCourse.getName(),newCourse.getDescription(),newCourse.getStartDate(),newCourse.getCredits());
    }

    private Course creatingNewCourse(Course newCourse) {
        String name;
        while (true){
            //checks if course name already exists
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

    @Override
    public void viewUsers() {
        //Prints out all users : ID, First name, Last name
        ResultSet users = getUsers();
        printTable.printUserHeader();
        try {
            while (users.next()) {
                printTable.printUserList(users.getString("ID"),users.getString("NAME"),users.getString("LASTNAME"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void deleteCourse() {
        while (true) {
            String input = ScannerUntils.scanString("Enter course id or exit");
            if (input.equalsIgnoreCase("exit")){
                break;
            }
            if(courseExist(input)){
                deleteCourseDB(input);
                break;
            } else {
                System.out.println("Course doesn't exist");
            }
        }

    }

    @Override
    public void deleteUser() {
        String input = ScannerUntils.scanString("Enter user id");
        //Checks if user with that ID exist and removes
        deleteUserDB(input);
    }

    @Override
    public void editUser() {
        EditUserMenu editUserMenu = new EditUserMenu();
        while (true) {
            String input = ScannerUntils.scanString("Enter user id or exit");
            //Checks if user exits
            if (input.equalsIgnoreCase("exit")){
                break;
            }

            if(userExist(input)){
                editUserMenu.menu(Integer.parseInt(input));
                break;
            }else {
                System.out.println("user doesn't exist");
            }
        }


    }

    @Override
    public void viewCourses() {
        //Prints out table : ID, Name, Description
        ResultSet courses = getCourses();
        printTable.printCoursesHeader();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            while (courses.next()) {
                printTable.printCoursesList(courses.getString("ID_COURSE"),courses.getString("NAME"),courses.getString("DESCRIPTION"),
                        format.format(courses.getDate("STARTDATE")),courses.getString("CREDITS"));
            }
        } catch (SQLException e){
            e.printStackTrace();
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
    public void register() {
        while (true) {
            String input = ScannerUntils.scanString("Enter course id or exit");
            if (input.equalsIgnoreCase("exit")){
                break;
            }
            if(courseExist(input)){
                checkIfUserExists(Integer.parseInt(input));
                break;
            } else {
                System.out.println("Course doesn't exist");
            }
        }
    }

    private void checkIfUserExists(Integer courseID){
        while (true) {
            String input = ScannerUntils.scanString("Enter person id or exit");
            if (input.equalsIgnoreCase("exit")){
                break;
            }
            if(userExist(input)){
                addToCourse(Integer.parseInt(input),courseID);
                break;
            }else {
                System.out.println("user doesn't exist");
            }
        }

    }

    @Override
    public void removeUserFromCourse() {
        //removes selected user from course

        String course_id = ScannerUntils.scanString("Enter course id");

        if(courseExist(course_id)){
            while (true) {
                String user_id = ScannerUntils.scanString("Enter person id or exit");
                if (user_id.equalsIgnoreCase("exit")){
                    break;
                }
                if(userExist(user_id)){
                    removeFromCourse(Integer.parseInt(user_id),Integer.parseInt(course_id));
                    break;
                }else {
                    System.out.println("user doesn't exist");
                }
            }
        } else {
            System.out.println("Course doesn't exist");
        }

    }

    @Override
    public void editCourses() {
        while (true) {
            System.out.println();
            String input = ScannerUntils.scanString("Enter course id or exit");
            if (input.equalsIgnoreCase("exit")){
                break;
            }
            if(courseExist(input)){
                editCourseMenu(Integer.parseInt(input));
                break;
            } else {
                System.out.println("Course doesn't exist");
            }
        }

    }

    private void editCourseMenu(Integer id){

        boolean running = true;
        //Menu for editing course
        while (running){
            String input = ScannerUntils.scanString("1) Change name 2) Change description 3) Change start Date \n" +
                    "4)Change credits 5) Exit");
            switch (Integer.parseInt(input)){
                case 1:
                    changeCourseName(id);
                    break;
                case 2:
                    editCourseDescription(ScannerUntils.scanString("Enter new description"),id);
                    break;
                case 3:
                    changeDate(id);
                    break;
                case 4:
                    editCourseCredits(Integer.parseInt(ScannerUntils.scanString("Enter new credits")),id);
                case 5:
                    //Checks if anything changed, if so asks to save
                    running = false;
                    break;
                default:
                    System.out.println("Wrong input");
            }
        }

    }

    private void changeCourseName(Integer id) {
        String name = ScannerUntils.scanString("Enter new name");
        if (courseNameExist(name)){
            System.out.println("this name already exists");
        } else {
            editCourseName(name,id);
        }
    }

    private void changeDate(Integer id) {
        while (true){
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                editCourseDate(format.parse(ScannerUntils.scanString("Enter start date yyyy-MM-dd")),id);
                break;
            }catch (Exception e){
                System.out.println("Wrong format");
            }
        }
    }

}
