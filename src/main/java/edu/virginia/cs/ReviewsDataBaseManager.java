package edu.virginia.cs;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewsDataBaseManager {
    private final String DataBase_Name = "Reviews.sqlite3";
    private Connection connection;
    public List<Student> studentList;
    public List<Course> courseList;
    public List<Review> reviewList;
    private boolean createTable = false;

    public ReviewsDataBaseManager(){
        try {
            File file = new File(DataBase_Name);
            boolean dataBaseExist = file.exists();
            if (!dataBaseExist) {
                File dbFile = new File(DataBase_Name);
                dbFile.createNewFile();
                connect();
                if (!createTable) {
                    createTables();
                }
                initializeTable();
//                disconnect();
            }
            else {
                connect();
            }
//            connect();
            disconnect();
        }catch (IOException e) {
            throw new IllegalStateException("Failed to create the file!");
        }
    }

    private void connect(){
        try {
            if (connection == null|| connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                this.connection = DriverManager.getConnection("jdbc:sqlite:"+DataBase_Name);
            } else {
                throw new IllegalStateException("the manager is already connected");
            }
        }catch(SQLException e){
            throw new RuntimeException();
        }catch(ClassNotFoundException e){
            throw new RuntimeException();
        }
//        System.out.println("Connected!");
    }
    private void createTables(){
        createTable = true;
        try {
            if (connection == null) {
                throw new IllegalStateException("The Manager has not connected yet!");
            }

            Statement statement1 = connection.createStatement();
            String StudentTableQuery = "CREATE TABLE IF NOT EXISTS Students(id INTEGER PRIMARY KEY AUTOINCREMENT , name VARCHAR(255) UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL)";
            statement1.executeUpdate(StudentTableQuery);

            Statement statement2 = connection.createStatement();
            String CourseTableQuery ="CREATE TABLE IF NOT EXISTS Courses(id INTEGER PRIMARY KEY AUTOINCREMENT, department TEXT NOT NULL, catalog_number VARCHAR(255) NOT NULL) ";
            statement2.executeUpdate(CourseTableQuery);

            // Cannot figure out why the tables are unresolved
            Statement statement3 = connection.createStatement();
            String ReviewsTableQuery ="CREATE TABLE Reviews (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "student_id INTEGER NOT NULL,"+
                    "course_id INTEGER NOT NULL, review TEXT NOT NULL,"+
                    "rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),"+
                    "FOREIGN KEY (student_id) REFERENCES Students(id) ON DELETE CASCADE,"+
                    "FOREIGN KEY (course_id) REFERENCES Courses(id)  ON DELETE CASCADE)";
            statement3.executeUpdate(ReviewsTableQuery);
        }catch (SQLException e){
            throw new RuntimeException("The tables are created!");
        }
//        System.out.println("Tables are created!");
    }
    protected void initializeTable(){
        if(connection == null){
            throw new IllegalStateException("The connection is closed!");
        }
        Student student1 = new Student("Kaich Chu",1,"3123214");
        Student student2 = new Student("Josh Ma",2,"djeqw21");
        Student student3 = new Student("Jin Zhao",3,"cjda23123");
        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);

        Course course1 = new Course(1,"CS","3140");
        Course course2 = new Course(2,"STAT","3250");
        Course course3 = new Course(3,"APMA","2130");
        Course course4 = new Course(4,"CHEM","0001");
        ArrayList<Course> courseList = new ArrayList<>();
        courseList.add(course1);
        courseList.add(course2);
        courseList.add(course3);
        courseList.add(course4);

        Review review1 = new Review(1,5,student1,course1,"VeryGood!");
        Review review2 = new Review(2,3,student2,course2,"I like this class!");
        Review review3 = new Review(3,4,student3,course4,"The material is interesting!");
        Review review4 = new Review(4,2,student1,course3,"Good class!");
        ArrayList<Review> reviewList = new ArrayList<>();
        reviewList.add(review1);
        reviewList.add(review2);
        reviewList.add(review3);
        reviewList.add(review4);

        try {
            String sql1 = "INSERT into Students(id,name,password) Values(?,?,?)";
            String sql2 = "INSERT into Courses(id, department, catalog_number) VALUES (?,?,?)";
            String sql3 = "INSERT into Reviews(id,student_id,course_id,review,rating) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            for(Student st: studentList){
                preparedStatement1.setInt(1,st.getId());
                preparedStatement1.setString(2,st.getName());
                preparedStatement1.setString(3,st.getPassword());
                preparedStatement1.executeUpdate();
            }
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            for(Course course: courseList){
                preparedStatement2.setInt(1,course.getId());
                preparedStatement2.setString(2,course.getDepartment());
                preparedStatement2.setString(3,course.getCatlogNumber());
                preparedStatement2.executeUpdate();
            }
            PreparedStatement preparedStatement3 = connection.prepareStatement(sql3);
            for (Review review:reviewList){
                preparedStatement3.setInt(1,review.getId());
                preparedStatement3.setInt(2,review.getStudentId());
                preparedStatement3.setInt(3,review.getCourseId());
                preparedStatement3.setString(4,review.getReviewMessage());
                if (review.getRating()<=5 & review.getRating()>=1){
                    preparedStatement3.setInt(5,review.getRating());
                }else{
                    throw new IllegalArgumentException("The rating should be a number between 1 to 5");
                }
                preparedStatement3.executeUpdate();
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
//        System.out.println("Program initialized!");
    }

    protected void initializeTable(List<Student> studentList, List<Course> courseList, List<Review> reviewList){
        if(connection == null){
            throw new IllegalStateException("The connection is closed!");
        }

        try {
            String sql1 = "INSERT into Students(id,name,password) Values(?,?,?)";
            String sql2 = "INSERT into Courses(id, department, catalog_number) VALUES (?,?,?)";
            String sql3 = "INSERT into Reviews(id,student_id,course_id,review,rating) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            for(Student st: studentList){
                String checkSql = "SELECT COUNT(*) FROM Students WHERE id = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setInt(1, st.getId());
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);
                if (count > 0) {
                    return;
                    //throw new IllegalArgumentException("Student with ID " + st.getId() + " already exists in the Students table");
                }else {
                    preparedStatement1.setInt(1, st.getId());
                    preparedStatement1.setString(2, st.getName());
                    preparedStatement1.setString(3, st.getPassword());
                    preparedStatement1.executeUpdate();
                }
            }
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            for(Course course: courseList){
                String checkSql = "SELECT COUNT(*) FROM Courses WHERE id = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setInt(1, course.getId());
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);
                if (count > 0) {
                    return;
                    //throw new IllegalArgumentException("Course with ID " + course.getId() + " already exists in the Courses table");
                }else{
                    preparedStatement2.setInt(1,course.getId());
                    preparedStatement2.setString(2,course.getDepartment());
                    preparedStatement2.setString(3,course.getCatlogNumber());
                    preparedStatement2.executeUpdate();
                }
            }
            PreparedStatement preparedStatement3 = connection.prepareStatement(sql3);
            for (Review review:reviewList){
                String checkSql = "SELECT COUNT(*) FROM Reviews WHERE id = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setInt(1, review.getId());
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);
                if (count > 0) {
                    return;
                    //throw new IllegalArgumentException("Review with ID " + review.getId() + " already exists in the Reviews table");
                }else {
                    preparedStatement3.setInt(1, review.getId());
                    preparedStatement3.setInt(2, review.getStudentId());
                    preparedStatement3.setInt(3, review.getCourseId());
                    preparedStatement3.setString(4, review.getReviewMessage());
                }
                if (review.getRating()<=5 & review.getRating()>=1){
                    preparedStatement3.setInt(5,review.getRating());
                }else{
                    throw new IllegalArgumentException("The rating should be a number between 1 to 5");
                }
                preparedStatement3.executeUpdate();
            }

        }catch(SQLException e){
            throw new IllegalStateException("The table does not exist!");
        }
//        System.out.println("Program initialized!");
    }

    public void disconnect(){
        try {
            if (connection == null || connection.isClosed()) {
                throw new IllegalStateException("The the Manager hasn't connected yet");
            }
            //connection.commit();

            connection.close();

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void addStudent(List<Student> studentList) {
        try {
            if(connection.isClosed()){
                connect();
            }
            for(Student st : studentList){

                String sql= String.format("INSERT INTO Students(id,name,password) VALUES" +
                        "(%d, \"%s\", \"%s\")", st.getId(),st.getName(),st.getPassword());
                Statement stm = connection.createStatement();
                stm.executeUpdate(sql);
                disconnect();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void addCourse(List<Course> courseList){
        try {
            if(connection.isClosed()){
                connect();
            }
            for(Course cour : courseList){

                String sql= String.format("INSERT INTO Courses(id, department, catalog_number) VALUES" +
                        "(%d, \"%s\", \"%s\")", cour.getId(),cour.getDepartment(),cour.getCatlogNumber());
                Statement stm = connection.createStatement();
                stm.executeUpdate(sql);
                disconnect();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void AddReview(List<Review> reviewList){
        try{
            if(connection.isClosed()){
                connect();
            }
            for(Review rw : reviewList){
                String sql = String.format("INSERT INTO Reviews(id,student_id,course_id,review,rating) VALUES" +
                        "(%d, %d, %d, \"%s\", %d)", rw.getId(),rw.getStudentId(),rw.getCourseId(),rw.getReviewMessage(),rw.getRating());
                Statement stm = connection.createStatement();
                stm.executeUpdate(sql);
                disconnect();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean checkRepeatedMessage(Course course, Student student){
        try {
            if(connection.isClosed()){
                connect();
            }
            String checkSql = String.format("SELECT COUNT(*) FROM Reviews WHERE " +
                    "student_id = %d AND course_id = %d", student.getId(), course.getId());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(checkSql);
            if (rs.getInt(1) == 0){
                disconnect();
                return true;
            }
            disconnect();
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    protected boolean checkHasMessage(Course course){
        try {
            if(connection.isClosed()){
                connect();
            }
            String checkSql = String.format("SELECT COUNT(*) FROM Reviews WHERE " +
                    "course_id = %d", course.getId());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(checkSql);
            if (rs.getInt(1) == 0){
                disconnect();
                return false;
            }
            disconnect();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected Map<String, Integer> returnreview(Course course){
        try {
            if(connection.isClosed()){
                connect();
            }
            Map<String, Integer> returnreview = new HashMap<>();
            String checkSql = String.format("SELECT * FROM Reviews WHERE " +
                    "course_id = %d", course.getId());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(checkSql);
            while(rs.next()){
                String revi = rs.getString("review");
                int rat = rs.getInt("rating");
                returnreview.put(revi,rat);
            }
            disconnect();
            return returnreview;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


        public List<Student> getStudentList(){
        List<Student> studentList = new ArrayList<>();
        try {
            if(connection.isClosed()){
                connect();
            }
            if (connection == null) {
                throw new IllegalStateException("The connection is closed!");
            }
            Statement statement = connection.createStatement();
            String sql = "SELECT * from Students";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                Student student = new Student(name,id,password);
                studentList.add(student);
            }
            disconnect();
        }catch (SQLException e){
            throw new IllegalStateException("The Stops Query do not exist!");
        }
        return studentList;
    }

    public List<Course> getCourseList(){
        List<Course> courseList = new ArrayList<>();
        try {
            if(connection.isClosed()){
                connect();
            }
            if (connection == null) {
                throw new IllegalStateException("The connection is closed!");
            }
            Statement statement = connection.createStatement();
            String sql = "SELECT * from Courses";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String department = rs.getString("department");
                String catlog_num = rs.getString("catalog_number") ;
                Course course = new Course(id,department,catlog_num);
                courseList.add(course);
            }
            disconnect();
        }catch (SQLException e){
            throw new IllegalStateException("The Stops Query do not exist!");
        }
        return courseList;
    }

    public List<Review> getReviewList(){
        List<Review> reviewList = new ArrayList<>();
        try {
            if(connection.isClosed()){
                connect();
            }
            if (connection == null) {
                throw new IllegalStateException("The connection is closed!");
            }
            Statement statement = connection.createStatement();
            String sql = "SELECT * from Reviews";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                int student_id = rs.getInt("student_id");
                int course_id = rs.getInt("course_id") ;
                String review = rs.getString("review");
                int rating = rs.getInt("rating");
                Statement statement2 = connection.createStatement();
                String sql2 = "SELECT * from Students WHERE id ="+student_id;
                ResultSet rs2 = statement2.executeQuery(sql2);
                Student stu = null;
                while(rs2.next()){
                    int stu_id = rs2.getInt("id");
                    String stu_name = rs2.getString("name");
                    String stu_pw =rs2.getString("password");
                    stu = new Student(stu_name,stu_id,stu_pw);
                }
                Course cour = null;
                Statement statement3 = connection.createStatement();
                String sql3 = "SELECT * from Courses WHERE id ="+course_id;
                ResultSet rs3 = statement3.executeQuery(sql3);
                while(rs3.next()){
                    int cou_id = rs3.getInt("id");
                    String cou_depar = rs3.getString("department");
                    String cou_cata =rs3.getString("catalog_number");
                    cour = new Course(cou_id,cou_depar, cou_cata);
                }
                Review rw = new Review(id,rating,stu,cour,review);
                reviewList.add(rw);

            }
        }catch (SQLException e){
            throw new IllegalStateException("ERROR! leave for ckc to write");
        }
        disconnect();
        return reviewList;
    }
    public boolean tableExists(String tableName) throws SQLException {
        Connection connection = null;
        ResultSet resultSet = null;
        connection = DriverManager.getConnection("jdbc:sqlite:"+DataBase_Name);
        DatabaseMetaData metaData = connection.getMetaData();
        resultSet = metaData.getTables(null, null, tableName, null);
        return resultSet.next(); // true if the table exists, false otherwise
    }

    /*
    public static void main(String args[]) throws IOException {
        ReviewsDataBaseManager reviewsDataBaseManager = new ReviewsDataBaseManager();
        Course course1 = new Course(7,"STS","0002");
//        Course course2 = new Course(2,"STAT",3250);
//        List<Student> stdlist = new ArrayList<>();
//        stdlist.add(st);
        List<Course> courlist = new ArrayList<>();
        courlist.add(course1);

//        reviewsDataBaseManager.addStudent(stdlist);
        reviewsDataBaseManager.addCourse(courlist);
//        stdlist = reviewsDataBaseManager.getStudentList();
//        courlist = reviewsDataBaseManager.getCourseList();
//        System.out.println(reviewsDataBaseManager.checkRepeatedMessage(course1,st));
//        System.out.println(reviewsDataBaseManager.checkRepeatedMessage(course2,st));
//        reviewsDataBaseManager.disconnect();
//        reviewsDataBaseManager.connect();

    }

     */
}