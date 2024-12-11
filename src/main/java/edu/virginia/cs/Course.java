package edu.virginia.cs;

public class Course {
    private int id;
    private String Department;
    private String CatlogNumber;

    public Course(int id, String department, String catlogNumber) {
        this.id = id;
        Department = department;
        CatlogNumber = catlogNumber;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getCatlogNumber() {
        return CatlogNumber;
    }

    public void setCatlogNumber(String catlogNumber) {
        CatlogNumber = catlogNumber;
    }
}
