package extras;

public class PrintTable {
    public void printDescription(String name, String description){
        String leftAlignFormat = "| %-15s | %-33s |%n";
        System.out.format("+-----------------+-----------------+-----------------+%n");
        System.out.format("+   Course name   |     Description                   +%n");
        System.out.format("+-----------------+-----------------+-----------------+%n");
        System.out.format(leftAlignFormat, name,description);

    }
    public void printCourseHeader(){

        System.out.format("+-----------------+-----------------+-----------------+%n");
        System.out.format("|    Name         |   Last name     |       Role      |%n");
        System.out.format("+-----------------+-----------------+-----------------+%n");
    }
    public void printCourse(String name, String lastname, String role){
        String leftAlignFormat = "| %-15s | %-15s | %-15s |%n";
        System.out.format(leftAlignFormat,name,lastname,role);
        System.out.format("+-----------------+-----------------+-----------------+%n");
    }
    public void printCoursesHeader(){
        System.out.format("+------+---------------------+------------------------------+--------+---------------+%n");
        System.out.format("|  ID  |   Name              |        Description           |Credits |  Start date   |%n");
        System.out.format("+------+---------------------+------------------------------+--------+---------------+%n");
    }
    public void printCoursesList(String ID, String name, String description, String date, String credits){
        String leftAlignFormat = "| %-4s | %-19s | %-28s | %-13s | %-6s |%n";
        System.out.format(leftAlignFormat,ID,name,description,date,credits);
        System.out.format("+------+---------------------+------------------------------+--------+---------------+%n");

    }
    public void printUserHeader(){
        System.out.format("+------+---------------+------------------------------+%n");
        System.out.format("|  ID  |   Name        |        Last name             |%n");
        System.out.format("+------+---------------+------------------------------+%n");
    }
    public void printUserList(String ID,String name, String lastName){
        String leftAlignFormat = "|  %-3s | %-13s | %-28s |%n";
        System.out.format(leftAlignFormat,ID,name,lastName);
        System.out.format("+------+---------------+------------------------------+%n");

    }

}
