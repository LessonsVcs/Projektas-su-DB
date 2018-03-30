package user;

import menu.extras.Roles;

import java.util.Date;

public class User {
    private Roles       role;
    private String      firstName;
    private String      lastName;
    private String      password;
    private String      username;
    private Date        dateOfBirth;
    private String      email;
    private String      address;
    private String      personalNumber;


    public User(){

    }
    User(String firstName, String lastName, String password, String username, Roles role){
        this.firstName = firstName;
        this.lastName  = lastName;
        this.password  = password;
        this.username  = username;
        this.role      = role;
    }
    public User(String firstName, String lastName, String password, String username,
            Roles role, String email, Date dateOfBirth, String address,String personalNumber){
        this(firstName,lastName,password,username,role);
        this.email          = email;
        this.dateOfBirth    = dateOfBirth;
        this.address        = address;
        this.personalNumber = personalNumber;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
