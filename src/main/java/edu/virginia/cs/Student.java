package edu.virginia.cs;

import org.w3c.dom.Text;

public class Student {
    private String Name;
    private int id;
    private String password;

    public Student(String name, int id, String password) {
        Name = name;
        this.id = id;
        this.password = password;
    }

    public String getName() {
        return Name;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
