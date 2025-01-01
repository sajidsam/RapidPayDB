package org.example;

public class User {
    private final String username;
    private final String password;
    private final String email;
    private final int id;
    private final String account_number;

    // Constructor
    public User(int id, String username, String email, String password, String account_number) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.account_number = account_number;


    }

    public String getEmail() {
        return email;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }
    public int getId() {
        return id;
    }
    public String getAccount_number(){
        return account_number;
    }
}
