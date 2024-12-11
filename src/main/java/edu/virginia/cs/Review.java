package edu.virginia.cs;

public class Review {
    private int id;
    private int rating;
    private Student student;
    private Course course;
    private String reviewMessage;

    public Review(int id, int rating, Student student, Course course, String reviewMessage) {
        this.id = id;
        this.rating = rating;
        this.student = student;
        this.course = course;
        this.reviewMessage = reviewMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    public int getStudentId(){
        return student.getId();
    }

    public int getCourseId(){
        return course.getId();
    }

    public String getReviewMessage() {
        return reviewMessage;
    }

    public void setReviewMessage(String reviewMessage) {
        this.reviewMessage = reviewMessage;
    }
}
