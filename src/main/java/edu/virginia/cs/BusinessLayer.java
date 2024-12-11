package edu.virginia.cs;

import net.bytebuddy.dynamic.ClassFileLocator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BusinessLayer {
    private ReviewsDataBaseManager rdbm;
    private List<Student> studentList;
    private List<Course> courseList;
    private List<Review> reviewList;
    protected List<String> printreview;
    protected int rating;

    private boolean initiallized=false;

    public BusinessLayer(){
        rdbm = new ReviewsDataBaseManager();
        if (!initiallized) {
//            initializeList();
//            rdbm.initializeTable();
            studentList=rdbm.getStudentList();
            courseList=rdbm.getCourseList();
            reviewList=rdbm.getReviewList();
        }
    }

//    private void initializeList(){
//        initiallized = true;
//        Student student1 = new Student("Kaich Chu",1,"3123214");
//        Student student2 = new Student("Josh Ma",2,"djeqw21");
//        Student student3 = new Student("Jin Zhao",3,"cjda23123");
//        studentList = new ArrayList<>();
//        studentList.add(student1);
//        studentList.add(student2);
//        studentList.add(student3);
//
//        Course course1 = new Course(1,"CS",3140);
//        Course course2 = new Course(2,"STAT",3250);
//        Course course3 = new Course(3,"APMA",2130);
//        Course course4 = new Course(4,"CHEM",1410);
//        courseList = new ArrayList<>();
//        courseList.add(course1);
//        courseList.add(course2);
//        courseList.add(course3);
//        courseList.add(course4);
//
//        Review review1 = new Review(1,5,student1,course1,"VeryGood!");
//        Review review2 = new Review(2,3,student2,course2,"I like this class!");
//        Review review3 = new Review(3,4,student3,course4,"The material is interesting!");
//        Review review4 = new Review(4,2,student1,course3,"Good class!");
//        reviewList = new ArrayList<>();
//        reviewList.add(review1);
//        reviewList.add(review2);
//        reviewList.add(review3);
//        reviewList.add(review4);
//    }

    public Student loginExistingUser(String loginName, String password) {

        for (Student eachstudent : studentList) {
            if (eachstudent.getName().equals(loginName)) {
                if (eachstudent.getPassword().equals(password)) {
                    return eachstudent;
                } else{
                    throw new IllegalArgumentException("the password is not matched.");
                }
            }
        }
        throw new IllegalStateException("The username is not valid.");
    }

    private boolean containStudent(String loginName) {
        for (Student eachstudent : studentList) {
            if (eachstudent.getName().equals(loginName)) {
                return true;
            }
        }
        return false;
    }

    private Course returncourse(String department, String catalog){
        for(Course eachcourse: courseList) {
            if (eachcourse.getDepartment().equals(department) && eachcourse.getCatlogNumber().equals(catalog)) {
                return eachcourse;
            }
        }
        return null;
    }
    public Course returnCourseByName(String coursename){
        String[] splitcourse = coursename.split(" ");
        return returncourse(splitcourse[0],splitcourse[1]);
    }




    private boolean containCourse(String department, String catalog){
        for(Course eachcourse: courseList){
            if (eachcourse.getDepartment().equals(department) && eachcourse.getCatlogNumber().equals(catalog)){
                return true;
            }
        }
        return false;
    }

    public boolean containCoursebyName(String coursename){
        String[] commentcourse = coursename.split(" ");
        return containCourse(commentcourse[0],commentcourse[1]);
    }
    public Student createUser(String loginName, String password){
        if(!containStudent(loginName)){
            List<Student> userlist = new ArrayList<>();
            Student user = new Student(loginName,studentList.size()+1,password);
            userlist.add(user);
            studentList.add(user);
            rdbm.addStudent(userlist);
            return user;
        }
        else{
            throw new IllegalStateException("The username is existed.");
        }
    }

    public void submitReview(Student student,String coursename, String comment, int rating){
        try {
            // break up the coursename
            String[] commentcourse = coursename.split(" ");
            if (commentcourse.length != 2 || !checkcoursename(commentcourse)) {
                throw new IllegalArgumentException("Please enter valid coursename");
            }
            Course cs;
            // check whether the course is in the table
            if(containCourse(commentcourse[0],commentcourse[1])){
                cs = returncourse(commentcourse[0],commentcourse[1]);
            }
            else{
                List<Course> clist = new ArrayList<>();
                cs = new Course(courseList.size()+1,commentcourse[0],commentcourse[1]);
                clist.add(cs);
                courseList.add(cs);
                rdbm.addCourse(clist);
            }
            // check whether the student has submitted the coursenmae before
            if(checkrepeatedcomment(student,cs)){
                Review newreview = new Review(reviewList.size()+1,rating,student,cs,comment);
                List<Review> rlist = new ArrayList<>();
                rlist.add(newreview);
                reviewList.add(newreview);
                rdbm.AddReview(rlist);
            }
            else{
                throw new IllegalArgumentException("The student has already submitted the review");
            }
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Please enter valid coursename");
        }
    }

    protected Course addCourse(String coursename) {
        String[] commentcourse = coursename.split(" ");
        Course cs;
        List<Course> clist = new ArrayList<>();
//        System.out.println(commentcourse[1]);
        cs = new Course(courseList.size()+1, commentcourse[0],commentcourse[1]);
        clist.add(cs);
        courseList.add(cs);
        rdbm.addCourse(clist);
        return cs;
    }

    protected boolean checkReviews(String coursename) {

            // break up the coursename
        String[] commentcourse = coursename.split(" ");
        if (commentcourse.length != 2 || !checkcoursename(commentcourse)) {
            return false;
        }
        Course cs;
        // check whether the course is in the table
        if (containCourse(commentcourse[0], commentcourse[1])) {
            cs = returncourse(commentcourse[0], commentcourse[1]);
        } else {
            return false;
        }
        if (!hasComment(cs)) {
            return false;
        }
        return true;
    }

    protected void seeReviews(String coursename){
        try {
            String[] commentcourse = coursename.split(" ");
            Course cs = returncourse(commentcourse[0],commentcourse[1]);
            Map<String, Integer> reviewMessage = rdbm.returnreview(cs);
            updateReviewMessage(reviewMessage);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Failed to see the reviews");
        }
    }

    private void updateReviewMessage(Map<String, Integer> reviewMessage) {
        printreview = new ArrayList<>();
        rating = 0;

        for (Map.Entry<String, Integer> entry : reviewMessage.entrySet()){
            printreview.add(entry.getKey());
            rating += entry.getValue();
        }
        rating /= reviewMessage.size();
    }

    private boolean hasComment(Course cs){
        return rdbm.checkHasMessage(cs);
    }


    private boolean checkcoursename(String[] commentcourse){
        try{
            String str = commentcourse[0];
            for(int i=0; i < str.length(); i++){
                //if any character is not in upper case, return false
                if( !Character.isUpperCase(str.charAt(i)))
                    return false;
                }
            int num = Integer.parseInt(commentcourse[1]);
            return commentcourse.length == 2 && str.length()>= 1 && str.length()<= 4 &&  commentcourse[1].length()==4;
        }catch(IllegalStateException e){
            throw new IllegalArgumentException();
        }catch (NumberFormatException notInt){
            throw new NumberFormatException();
        }
    }
    public boolean checkcoursenamebyinputname(String coursename){
        String[] commentcourse = coursename.split(" ");
        return checkcoursename(commentcourse);
    }



    public boolean checkrepeatedcomment(Student st, Course course){
        return rdbm.checkRepeatedMessage(course,st);
    }





//    public static void main(String args[]){
//        BusinessLayer bl = new BusinessLayer();
////        Student st1 = bl.loginExistingUser("Kaich Chu","3123214");
////        System.out.println(st1.getId()+st1.getName());
//        Course course1 = new Course(7,"STS","0002");
//        bl.addCourse("STS 0002");
////        Student st2 = bl.createUser("Joanna","233");
////        System.out.println(st2.getId()+st2.getName());
////        bl.submitReview(bl.studentList.get(2),"CS 3140", "I don't like that course",2
////        System.out.println(bl.checkReviews("CS 3140"));
////        bl.seeReviews("CS 3140");
////        bl.createUser("ZT","23333");
////        bl.submitReview(st1,"STAT 3250","Not good",2);
////
////
////        System.out.println(bl.rating);
////        for(String each: bl.printreview){
////            System.out.println(each);
////        }
//
//    }
}
