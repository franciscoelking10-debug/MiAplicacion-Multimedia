package com.example.miaplicacion;

public class CourseModel {

    private String courseName;
    private float courseRating;
    private int courseImage;
    private String courseDetail;

    // Constructor
    public CourseModel(String courseName, float courseRating, int courseImage, String courseDetail) {
        this.courseName = courseName;
        this.courseRating = courseRating;
        this.courseImage = courseImage;
        this.courseDetail = courseDetail;
    }

    // Getters and Setters
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public float getCourseRating() {
        return courseRating;
    }

    public void setCourseRating(float courseRating) {
        this.courseRating = courseRating;
    }

    public int getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(int courseImage) {
        this.courseImage = courseImage;
    }

    public String getCourseDetail() {
        return courseDetail;
    }

    public void setCourseDetail(String courseDetail) {
        this.courseDetail = courseDetail;
    }
}
