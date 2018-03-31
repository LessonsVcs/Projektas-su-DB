package menu;

import cources.Course;
import cources.ReadWriteCourseFile;
import cources.ReadWriteCourseRelation;
import menu.extras.dbUtils.DBUtils;
import menu.extras.dbUtils.UserDB;
import user.EditUserMenu;
import user.User;
import menu.extras.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static menu.extras.dbUtils.CourseDB.*;
import static menu.extras.dbUtils.DBUtils.*;
import static menu.extras.UpdateLists.*;
import static menu.extras.dbUtils.UserDB.*;

public class MenuForAdmin implements AdminInterface,LecturerInterface,UserInterface {
    private String myID;
    private boolean running = true;
    private HashMap<Integer,List<String>> courseRealtions = new HashMap<>();
    private HashMap<Integer, Course> courses= new HashMap();
    private HashMap<Integer, User> users = new HashMap();
    private PrintTable printTable = new PrintTable();
    MenuForAdmin(){
        this.myID=myID;
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
        users = updateUsers();
        printTable.printUserHeader();
        for (Integer i: users.keySet()) {
            printTable.printUserList(users.get(i).getPersonalNumber(),users.get(i).getFirstName(),users.get(i).getLastName());
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
        this.courses = updateCourses();
        printTable.printCoursesHeader();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        for (Integer i: courses.keySet()) {
            printTable.printCoursesList(courses.get(i).getCourseID(),courses.get(i).getName(),courses.get(i).getDescription(),
                format.format(courses.get(i).getStartDate()),courses.get(i).getCredits());
        }

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

    private void showSelectedCourse(Integer i){
        //Prints out table who goes to course, First name, Last name, Role
        this.courseRealtions = updateCourseRelations();
        this.users = updateUsers();
        printTable.printDescription(courses.get(i).getName(),courses.get(i).getDescription());
        printTable.printCourseHeader();
        try {
            for (String line : courseRealtions.get(i)) {
                printTable.printCourse(users.get(Integer.parseInt(line)).getFirstName(),
                        users.get(Integer.parseInt(line)).getLastName(), users.get(Integer.parseInt(line)).getRole().toString());
            }
        } catch (Exception e){
            System.out.println("There's no one in course ");
        }
    }

    @Override
    public void register() {
        this.courseRealtions = updateCourseRelations();
        this.users = updateUsers();
        this.courses = updateCourses();

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
                    checkIfUserExists(i);
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

    private void checkIfUserExists(Integer courseID){

        boolean found = false;
        while (true) {
            String input = ScannerUntils.scanString("Enter person id or exit");
            if (input.equalsIgnoreCase("exit")){
                break;
            }
            //Checks if user Exist
            for (Integer i : users.keySet()) {
                if(i==Integer.parseInt(input)){
                    found = true;
                    addToCourse(i,courseID);
                    break;
                }
            }
            if (found){
                break;
            }else {
                System.out.println("user doesn't exist");
            }
        }

    }

    private void addToCourse(Integer userID, Integer courseID){
        //Adds selected user to selected course
        boolean found = false;

        for (Integer i : courseRealtions.keySet()) {
            if(i==courseID){
                found = true;
                if(isAlreadyIncourse(userID, i)){
                    System.out.println("user already is in this course");
                } else {
                    courseRealtions.get(i).add(userID.toString());
                }
                break;
            }
        }
        if (!found){
            List<String> list = new ArrayList<>();
            list.add(userID.toString());
            courseRealtions.put(courseID,list);
        }
        ReadWriteCourseRelation readWriteCourseRelation = new ReadWriteCourseRelation();
        readWriteCourseRelation.setCourseRealtions(courseRealtions);
        readWriteCourseRelation.writeCourseRealation();

    }

    private boolean isAlreadyIncourse(Integer userID, Integer i) {

        for(String user: courseRealtions.get(i)){
            if (user.equalsIgnoreCase(userID.toString())){
                return true;
            }
        }
        return false;
    }


    private void checkIfUserExistsForRemove(Integer courseID){
        boolean found = false;
        while (true) {
            String input = ScannerUntils.scanString("Enter person id or exit");
            if (input.equalsIgnoreCase("exit")){
                break;
            }
            //Checks if user Exists
            for (Integer i : users.keySet()) {
                if(i==Integer.parseInt(input)){
                    found = true;
                    removeFromCourse(i,courseID);
                    break;
                }
            }
            if (found){
                break;
            }else {
                System.out.println("user doesn't exist");
            }
        }

    }

    private void removeFromCourse(Integer userID, Integer courseID){
        boolean found = false;

        for (Integer i : courseRealtions.keySet()) {
            if(i==courseID){
                found = true;
                courseRealtions.get(i).remove(userID.toString());
                break;
            }
        }
        if (found){
            ReadWriteCourseRelation readWriteCourseRelation = new ReadWriteCourseRelation();
            readWriteCourseRelation.setCourseRealtions(courseRealtions);
            readWriteCourseRelation.writeCourseRealation();
        } else {
            System.out.println("Entered user isn't in this course");
        }


    }

    @Override
    public void removeUserFromCourse() {
        //removes selected user from course
        this.courseRealtions = updateCourseRelations();
        this.users = updateUsers();
        this.courses = updateCourses();
        String input = ScannerUntils.scanString("Enter course id");
        boolean courseFound =  false;

        for (Integer i : courses.keySet()) {
            if(i==Integer.parseInt(input)){
                courseFound = true;
                checkIfUserExistsForRemove(i);
                break;
            }
        }
        if (!courseFound){
            System.out.println("Course doesn't exist");
        }

    }

    @Override
    public void editCourses() {
        courses = updateCourses();
        boolean courseFound =  false;
        while (true) {
            System.out.println();
            String input = ScannerUntils.scanString("Enter course id or exit");
            if (input.equalsIgnoreCase("exit")){
                break;
            }
            //Checks if course exists
            for (Integer i : courses.keySet()) {
                if(i==Integer.parseInt(input)){
                    courseFound = true;
                    editCourseMenu(i);
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

    private void editCourseMenu(Integer id){

        boolean changes = false;
        boolean running = true;
        //Menu for editing course
        while (running){
            String input = ScannerUntils.scanString("1) Change name 2) Change description 3) Change start Date \n" +
                    "4)Change credits 5) Exit");
            switch (Integer.parseInt(input)){
                case 1:
                    courses.get(id).setName(ScannerUntils.scanString("Enter new name"));
                    changes= true;
                    break;
                case 2:
                    courses.get(id).setDescription(ScannerUntils.scanString("Enter new description"));
                    changes= true;
                    break;
                case 3:
                    changes = changeDate(id);
                    break;
                case 4:
                    courses.get(id).setCredits(ScannerUntils.scanString("Enter new credits"));
                    changes= true;
                case 5:
                    //Checks if anything changed, if so asks to save
                    if (changes){
                        running = toSaveCourseChanges();
                    } else {
                      running = false;

                    }
                    break;
                default:
                    System.out.println("Wrong input");
            }
        }

    }

    private boolean toSaveCourseChanges() {
        //Asks if user wants to save changes
        boolean running;
        while (true){
            String response = ScannerUntils.scanString("Changes are made, do you want to save? Yes/No");
            if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no")){
                running = false;
                if (response.equalsIgnoreCase("yes")){
                    ReadWriteCourseFile readWriteCourseFile = new ReadWriteCourseFile();
                    readWriteCourseFile.setCourses(courses);
                    readWriteCourseFile.writeCourseFile();
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

    private boolean changeDate(Integer id) {
        boolean changes;
        while (true){
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                courses.get(id).setStartDate(format.parse(ScannerUntils.scanString("Enter start date yyyy-MM-dd")));
                changes=true;
                break;
            }catch (Exception e){
                System.out.println("Wrong format");
            }
        }
        return changes;
    }

}
