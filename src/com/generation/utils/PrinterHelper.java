package com.generation.utils;

import com.generation.model.Student;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class PrinterHelper
{

    public static void showMainMenu()
    {
        System.out.println( "|-------------------------------|" );
        System.out.println( "| Welcome to StudentGen         |" );
        System.out.println( "|-------------------------------|" );
        System.out.println( "| Select 1 option:              |" );
        System.out.println( "| . 1 Register Student          |" );
        System.out.println( "| . 2 Find Student              |" );
        System.out.println( "| . 3 Grade Student             |" );
        System.out.println( "| . 4 Enroll Student to Course  |" );
        System.out.println( "| . 5 Show Students Summary     |" );
        System.out.println( "| . 6 Show Courses Summary      |" );
        System.out.println( "| . 7 Show Course Average       |" );
        System.out.println( "| . 8 Exit                      |" );
        System.out.println( "|-------------------------------|" );
    }

    public static Student createStudentMenu( Scanner scanner )
        throws ParseException
    {
        System.out.println( "|-------------------------------------|" );
        System.out.println( "| . 1 Register Student                |" );
        System.out.println( "|-------------------------------------|" );
        System.out.println( "| Enter student name:                 |" );
        String name = scanner.next();
        System.out.println( "| Enter student ID:                   |" );
        String id = scanner.next();
        System.out.println( "| Enter student email:                |" );
        String email = scanner.next();
        System.out.println( "| Enter student birth date(MM/dd/yyyy)|" ); //Display prompt to user asking for birth date in MM/dd/yyyy format
        // Create a date formatter that will parse dates in MM/dd/yyyy format
        // MM = month (01-12), dd = day (01-31), yyyy = year (e.g., 1999)
        // Example: 05/15/2000 means May 15, 2000
        DateFormat formatter = new SimpleDateFormat( "MM/dd/yyyy");

        // Set formatter to STRICT mode - rejects invalid dates like:
        // - 13/01/2020 (month 13 doesn't exist)
        // - 02/30/2020 (February doesn't have 30 days)
        // - 22/22/22 (invalid month and only 2-digit year)
        formatter.setLenient(false);

        //DONE DATE VALIDATION AND TESTED ON A FEW CASES AND ITS ABLE TO CAPTURE

        Date birthDate = new Date();
        Boolean dateValidated = false;

        // DONE CHALLENGE IMPLEMENTED
        do {
            try {
                birthDate = formatter.parse(scanner.next());
                dateValidated = true;
            } catch (Exception exception) {
                System.out.println("Date is not valid!");
            }
        } while (!dateValidated);


        System.out.println( "|-------------------------------------|" );
        Student student = new Student( id, name, email, birthDate );
        System.out.println( "Student Successfully Registered! " );
        System.out.println(student);
        return student;
    }

}
