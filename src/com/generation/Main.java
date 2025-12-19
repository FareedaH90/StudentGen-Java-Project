package com.generation;

import com.generation.model.Course;
import com.generation.model.Student;
import com.generation.service.CourseService;
import com.generation.service.StudentService;
import com.generation.utils.PrinterHelper;

import java.text.ParseException;
import java.util.Scanner;

public class Main
{

    public static void main( String[] args )
            throws ParseException
    {
        StudentService studentService = new StudentService();
        CourseService courseService = new CourseService();
        Scanner scanner = new Scanner( System.in );
        int option = 0;
        do
        {
            PrinterHelper.showMainMenu();
            option = scanner.nextInt();
            switch ( option )
            {
                case 1:
                    registerStudent( studentService, scanner );
                    break;
                case 2:
                    findStudent( studentService, scanner );
                    break;
                case 3:
                    gradeStudent( studentService, scanner );
                    break;
                case 4:
                    enrollStudentToCourse( studentService, courseService, scanner );
                    break;
                case 5:
                    showStudentsSummary( studentService, scanner );
                    break;
                case 6:
                    showCoursesSummary( courseService, scanner );
                    break;
                case 7:
                    showCourseAverageGrade( studentService, courseService, scanner );
                    break;
            }
        }
        while ( option != 8 );
    }

    private static void enrollStudentToCourse( StudentService studentService, CourseService courseService,
                                               Scanner scanner )
    {
        System.out.println( "Insert student ID" );
        String studentId = scanner.next();
        Student student = studentService.findStudent( studentId );
        if ( student == null )
        {
            System.out.println( "Invalid Student ID" );
            return;
        }
        System.out.println( student );
        System.out.println( "Insert course ID" );
        String courseId = scanner.next();
        Course course = courseService.getCourse( courseId );
        if ( course == null )
        {
            System.out.println( "Invalid Course ID" );
            return;
        }
        System.out.println( course );
        courseService.enrollStudent( courseId, student );
        studentService.enrollToCourse( studentId, course );
        System.out.println( "Student with ID: " + studentId + " enrolled successfully to " + courseId );

    }

    private static void showCoursesSummary( CourseService courseService, Scanner scanner )
    {
        courseService.showSummary();
    }

    private static void showStudentsSummary( StudentService studentService, Scanner scanner )
    {
        studentService.showSummary();
    }

    private static void gradeStudent(StudentService studentService, Scanner scanner)
    {
        System.out.println("Enter student ID: ");
        String studentId = scanner.next();
        Student student = studentService.findStudent(studentId);

        if (student != null)
        {
            System.out.println("Enter course Code: ");
            String courseCode = scanner.next();
            boolean courseFound = student.isCourseApproved(courseCode);

            if (courseFound)
            {
                Course courseToGrade = null;

                for (Course c : student.getApprovedCourses())
                {
                    if (c.getCode().equals(courseCode))
                    {
                        courseToGrade = c;
                        break;
                    }
                }

                // Auto-grade (no user input needed - random grade generated)
                studentService.gradeStudent(studentId, courseToGrade);
            }
            else
            {
                System.out.println("Course with Code = " + courseCode + " not found");
            }
        }
        else
        {
            System.out.println("Student with Id = " + studentId + " not found");
        }
    }

    private static void findStudent( StudentService studentService, Scanner scanner )
    {
        System.out.println( "Enter student ID: " );
        String studentId = scanner.next();
        Student student = studentService.findStudent( studentId );
        if ( student != null )
        {
            System.out.println( "Student Found: " );
            System.out.println( student );
        }
        else
        {
            System.out.println( "Student with Id = " + studentId + " not found" );
        }
    }

    private static void registerStudent( StudentService studentService, Scanner scanner )
            throws ParseException
    {
        Student student = PrinterHelper.createStudentMenu( scanner );
        studentService.subscribeStudent( student );
    }

    // CHALLENGE: Show average grade for a course
    private static void showCourseAverageGrade(StudentService studentService, CourseService courseService, Scanner scanner)
    {
        System.out.println("Enter course Code: ");
        String courseCode = scanner.next();

        Course course = courseService.getCourse(courseCode);
        if (course == null)
        {
            System.out.println("Course not found");
            return;
        }

        double average = studentService.getCourseAverageGrade(courseCode);

        if (average == -1)
        {
            System.out.println("No students have been graded in " + courseCode);
        }
        else
        {
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("Course: " + course.getName() + " (" + courseCode + ")");
            System.out.println("Average Grade: " + String.format("%.2f", average) + "/100");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        }
    }
}