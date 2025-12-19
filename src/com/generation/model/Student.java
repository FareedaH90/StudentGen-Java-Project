package com.generation.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student
        extends Person
        implements Evaluation
{
    private double average;

    private final List<Course> courses = new ArrayList<>();

    private final Map<String, Course> approvedCourses = new HashMap<>();

    // Map to store grades per course
    private final Map<String, Double> courseGrades = new HashMap<>();

    // Passing grade constant
    private static final double PASSING_GRADE = 50.0;

    public Student( String id, String name, String email, Date birthDate )
    {
        super( id, name, email, birthDate );
    }

    public void enrollToCourse( Course course )
    {
        // only if courses is not found in courses
        if(!courses.contains(course)) {
            // add the course to courses ArrayList
            courses.add(course);
            // put the course as an approved course is in ApprovedCourses
            registerApprovedCourse(course);
        }
    }

    public void registerApprovedCourse( Course course )
    {
        approvedCourses.put( course.getCode(), course );
    }

    public boolean isCourseApproved( String courseCode )
    {
        // approvedCourses as a HashMap is stored in key value pairs
        // return true or false when course approved
        return approvedCourses.containsKey(courseCode);
    }

    // CHALLENGE: Returns all courses where student passed (grade >= 50)
    public List<Course> findPassedCourses( Course course )
    {
        List<Course> passedCourses = new ArrayList<>();

        // Loop through all graded courses
        for (String courseCode : courseGrades.keySet()) {
            double grade = courseGrades.get(courseCode);

            // If grade is passing (>= 50), add the course to passedCourses
            if (grade >= PASSING_GRADE && approvedCourses.containsKey(courseCode)) {
                passedCourses.add(approvedCourses.get(courseCode));
            }
        }

        return passedCourses;
    }

    public boolean isAttendingCourse(String courseCode) {
        // If we use courses to check, what will the logic be?
        for (Course course : courses) {
            if (course.getCode().equals(courseCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getAverage()
    {
        return average;
    }

    // Grade a specific course (replaces old grade if course is graded again)
    public void gradeInCourse(String courseCode, double grade) {
        courseGrades.put(courseCode, grade);
    }

    // Get grade for a specific course
    public Double getGradeForCourse(String courseCode) {
        return courseGrades.get(courseCode);
    }

    // Calculate total grade from all unique courses
    public double getTotalGrade() {
        double total = 0;
        for (Double grade : courseGrades.values()) {
            total += grade;
        }
        return total;
    }

    // Calculate average grade across all courses
    public double getAverageGrade() {
        if (courseGrades.isEmpty()) {
            return 0.0;
        }
        return getTotalGrade() / courseGrades.size();
    }

    @Override
    public List<Course> getApprovedCourses()
    {
        // return the courses ArrayList
        return courses;
    }

    @Override
    public String toString()
    {
        return "Student {" + super.toString() + "}";
    }
}