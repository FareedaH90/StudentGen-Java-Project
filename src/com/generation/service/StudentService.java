package com.generation.service;

import com.generation.model.Course;
import com.generation.model.Student;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService {
    private final Map<String, Student> students = new HashMap<>();

    // Generate dummy students
    public StudentService() {
        subscribeStudent(new Student("001", "John Doe", "johndoe@gmail.com", new Date("01/01/2000")));
        subscribeStudent(new Student("002", "May Fair", "mayfair@gmail.com", new Date("02/02/2010")));
        subscribeStudent(new Student("003", "Steve Smith", "stevesmith@gmail.com", new Date("03/03/2015")));
    }

    public void subscribeStudent(Student student) {
        students.put(student.getId(), student);
    }

    public Student findStudent(String studentId) {
        if (students.containsKey(studentId)) {
            return students.get(studentId);
        }
        return null;
    }

    public boolean isSubscribed(String studentId) {
        return findStudent(studentId) != null;
    }

    public void showSummary() {
        // Show the student details and the enrolled courses
        System.out.println("Enrolled Students");

        for (String key : students.keySet()) {
            // first outer for loop prints out the student's info.
            Student student = students.get(key);
            System.out.println(student);

            List<Course> enrolledCourses = student.getApprovedCourses();

            if (enrolledCourses.size() > 0) {
                System.out.println("\tEnrolled Courses");

                for (Course course : enrolledCourses) {
                    System.out.println("\t" + course);
                    // Show grade if student has been graded
                    Double grade = student.getGradeForCourse(course.getCode());
                    if (grade != null) {
                        System.out.println("\t\tGrade: " + String.format("%.2f", grade));
                    }
                }
            } else {
                System.out.println("\tNo course found.");
            }
        }
    }

    public void enrollToCourse(String studentId, Course course) {
        if (students.containsKey(studentId)) {
            students.get(studentId).enrollToCourse(course);
        }
    }

    // AUTO-GRADE: Generate random grade between 40-100
    public void gradeStudent(String studentId, Course course) {
        Student student = students.get(studentId);

        // Auto-generate a realistic grade (40-100 range)
        // 40-100 ensures most students pass (>= 50) but some fail
        double gradeScore = 40 + (Math.random() * 60);

        // Store the grade
        student.gradeInCourse(course.getCode(), gradeScore);

        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("Student " + studentId + " graded in " + course.getName());
        System.out.println("Grade: " + String.format("%.2f", gradeScore) + "/100");

        if (gradeScore >= 50.0) {
            System.out.println("Status: ✓ PASSED");
        } else {
            System.out.println("Status: ✗ FAILED");
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    // CHALLENGE: Calculate average grade of all students in a course
    public double getCourseAverageGrade(String courseCode) {
        double totalGrade = 0;
        int studentCount = 0;

        // Loop through all students
        for (Student student : students.values()) {
            // Check if student has a grade for this course
            Double grade = student.getGradeForCourse(courseCode);
            if (grade != null) {
                totalGrade += grade;
                studentCount++;
            }
        }

        // Return average or -1 if no students graded
        return studentCount > 0 ? totalGrade / studentCount : -1;
    }

    // method for StudentService.java
    public Map<String, Student> getStudents() {
        return students;
    }
}