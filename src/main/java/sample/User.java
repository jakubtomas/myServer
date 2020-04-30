package sample;

public class User {
    private String fname;
    private String lname;
    private String login;
    private String password;

    public User(String fname, String lname, String login, String password) {
        this.fname = fname;
        this.lname = lname;
        this.login = login;
        this.password = password;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}