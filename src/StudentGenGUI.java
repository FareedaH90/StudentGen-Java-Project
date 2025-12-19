package com.generation;

import com.generation.model.Course;
import com.generation.model.Student;
import com.generation.service.CourseService;
import com.generation.service.StudentService;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class StudentGenGUI extends Frame {
    private StudentService studentService;
    private CourseService courseService;

    // UI Components
    private Panel mainPanel;
    private TextArea outputArea;
    private TextField studentIdField;
    private TextField courseCodeField;
    private TextField nameField;
    private TextField emailField;
    private TextField birthDateField;

    public StudentGenGUI() {
        // Initialize services
        studentService = new StudentService();
        courseService = new CourseService();

        // Setup Frame
        setTitle("StudentGen Management System");
        setSize(900, 700);
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Create components
        createMenuPanel();
        createOutputArea();

        // Window closing event
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void createMenuPanel() {
        Panel menuPanel = new Panel();
        menuPanel.setLayout(new GridLayout(8, 1, 5, 5));
        menuPanel.setBackground(new Color(240, 240, 240));

        // Create buttons
        Button registerBtn = new Button("1. Register Student");
        Button findBtn = new Button("2. Find Student");
        Button gradeBtn = new Button("3. Grade Student");
        Button enrollBtn = new Button("4. Enroll Student to Course");
        Button studentSummaryBtn = new Button("5. Show Students Summary");
        Button courseSummaryBtn = new Button("6. Show Courses Summary");
        Button courseAverageBtn = new Button("7. Show Course Average");
        Button exitBtn = new Button("8. Exit");

        // Style buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        Color buttonColor = new Color(70, 130, 180);

        Button[] buttons = {registerBtn, findBtn, gradeBtn, enrollBtn,
                studentSummaryBtn, courseSummaryBtn, courseAverageBtn, exitBtn};

        for (Button btn : buttons) {
            btn.setFont(buttonFont);
            btn.setBackground(buttonColor);
            btn.setForeground(Color.WHITE);
            menuPanel.add(btn);
        }

        // Add action listeners
        registerBtn.addActionListener(e -> registerStudent());
        findBtn.addActionListener(e -> findStudent());
        gradeBtn.addActionListener(e -> gradeStudent());
        enrollBtn.addActionListener(e -> enrollStudent());
        studentSummaryBtn.addActionListener(e -> showStudentsSummary());
        courseSummaryBtn.addActionListener(e -> showCoursesSummary());
        courseAverageBtn.addActionListener(e -> showCourseAverage());
        exitBtn.addActionListener(e -> System.exit(0));

        add(menuPanel, BorderLayout.WEST);
    }

    private void createOutputArea() {
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBackground(Color.WHITE);
        outputArea.setText("Welcome to StudentGen Management System\n" +
                "Select an option from the menu on the left.\n\n");

        add(outputArea, BorderLayout.CENTER);
    }

    // NEW: Helper method to parse and validate dates
    private java.util.Date parseDateFromString(String dateStr) {
        try {
            // Expected format: DD/MM/YYYY
            String[] parts = dateStr.split("/");

            if (parts.length != 3) {
                return null;
            }

            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            // Basic validation
            if (month < 1 || month > 12) {
                return null;
            }

            if (day < 1 || day > 31) {
                return null;
            }

            if (year < 1900 || year > 2024) {
                return null;
            }

            // Validate days in month
            int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

            // Check for leap year
            if (isLeapYear(year)) {
                daysInMonth[1] = 29;
            }

            if (day > daysInMonth[month - 1]) {
                return null;
            }

            // Create date using Calendar (month is 0-indexed in Calendar)
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(year, month - 1, day, 0, 0, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);

            return cal.getTime();

        } catch (NumberFormatException e) {
            return null;
        }
    }

    // NEW: Helper method to check leap year
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    // 1. Register Student (UPDATED with complete validation)
    private void registerStudent() {
        Dialog dialog = new Dialog(this, "Register Student", true);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));
        dialog.setSize(400, 300);

        Label idLabel = new Label("Student ID:");
        TextField idField = new TextField();

        Label nameLabel = new Label("Name:");
        nameField = new TextField();

        Label emailLabel = new Label("Email:");
        emailField = new TextField();

        Label birthLabel = new Label("Birth Date (DD/MM/YYYY):");
        birthDateField = new TextField();

        Button submitBtn = new Button("Register");
        Button cancelBtn = new Button("Cancel");

        dialog.add(idLabel);
        dialog.add(idField);
        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(emailLabel);
        dialog.add(emailField);
        dialog.add(birthLabel);
        dialog.add(birthDateField);
        dialog.add(submitBtn);
        dialog.add(cancelBtn);

        submitBtn.addActionListener(e -> {
            try {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String birthDateStr = birthDateField.getText().trim();

                // 1. Check if all fields are filled
                if (id.isEmpty() || name.isEmpty() || email.isEmpty() || birthDateStr.isEmpty()) {
                    outputArea.append("✗ ERROR: All fields are required!\n\n");
                    dialog.dispose();
                    return;
                }

                // 2. Validate Student ID (must be alphanumeric, 3-10 characters)
                if (!id.matches("^[a-zA-Z0-9]{3,10}$")) {
                    outputArea.append("✗ ERROR: Invalid Student ID!\n");
                    outputArea.append("Student ID must be 3-10 alphanumeric characters.\n\n");
                    dialog.dispose();
                    return;
                }

                // 3. Check if student ID already exists
                if (studentService.findStudent(id) != null) {
                    outputArea.append("✗ ERROR: Student ID '" + id + "' already exists!\n\n");
                    dialog.dispose();
                    return;
                }

                // 4. Validate Name (letters, spaces, hyphens only, 2-50 characters)
                if (!name.matches("^[a-zA-Z\\s\\-]{2,50}$")) {
                    outputArea.append("✗ ERROR: Invalid name!\n");
                    outputArea.append("Name must contain only letters, spaces, or hyphens (2-50 characters).\n\n");
                    dialog.dispose();
                    return;
                }

                // 5. Validate Email format
                if (!isValidEmail(email)) {
                    outputArea.append("✗ ERROR: Invalid email format!\n");
                    outputArea.append("Email must be in format: example@domain.com\n\n");
                    dialog.dispose();
                    return;
                }

                // 6. Parse and validate date
                java.util.Date birthDate = parseDateFromString(birthDateStr);
                if (birthDate == null) {
                    outputArea.append("✗ ERROR: Invalid date format!\n");
                    outputArea.append("Please use DD/MM/YYYY format (e.g., 16/01/2018)\n\n");
                    dialog.dispose();
                    return;
                }

                // 7. Check if date is not in the future
                if (birthDate.after(new java.util.Date())) {
                    outputArea.append("✗ ERROR: Birth date cannot be in the future!\n\n");
                    dialog.dispose();
                    return;
                }

                // All validations passed - create student
                Student student = new Student(id, name, email, birthDate);
                studentService.subscribeStudent(student);

                outputArea.append("✓ Student registered successfully!\n");
                outputArea.append("ID: " + id + ", Name: " + name + "\n");
                outputArea.append("Email: " + email + "\n");
                outputArea.append("Birth Date: " + birthDateStr + "\n\n");
                dialog.dispose();

            } catch (Exception ex) {
                outputArea.append("✗ Error: " + ex.getMessage() + "\n\n");
                dialog.dispose();
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // NEW: Email validation method
    private boolean isValidEmail(String email) {
        // Basic email validation: has @ symbol, has domain, has extension
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    // 2. Find Student
    private void findStudent() {
        Dialog dialog = new Dialog(this, "Find Student", true);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));
        dialog.setSize(350, 150);

        Label idLabel = new Label("Student ID:");
        studentIdField = new TextField();

        Button findBtn = new Button("Find");
        Button cancelBtn = new Button("Cancel");

        dialog.add(idLabel);
        dialog.add(studentIdField);
        dialog.add(findBtn);
        dialog.add(cancelBtn);

        findBtn.addActionListener(e -> {
            String studentId = studentIdField.getText();
            Student student = studentService.findStudent(studentId);

            if (student != null) {
                outputArea.append("✓ Student Found:\n");
                outputArea.append(student.toString() + "\n\n");
            } else {
                outputArea.append("✗ Student with ID " + studentId + " not found.\n\n");
            }
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // 3. Grade Student
    private void gradeStudent() {
        Dialog dialog = new Dialog(this, "Grade Student", true);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));
        dialog.setSize(400, 200);

        Label idLabel = new Label("Student ID:");
        studentIdField = new TextField();

        Label courseLabel = new Label("Course Code:");
        courseCodeField = new TextField();

        Button gradeBtn = new Button("Grade");
        Button cancelBtn = new Button("Cancel");

        dialog.add(idLabel);
        dialog.add(studentIdField);
        dialog.add(courseLabel);
        dialog.add(courseCodeField);
        dialog.add(gradeBtn);
        dialog.add(cancelBtn);

        gradeBtn.addActionListener(e -> {
            String studentId = studentIdField.getText();
            String courseCode = courseCodeField.getText();

            Student student = studentService.findStudent(studentId);

            if (student == null) {
                outputArea.append("✗ Student not found.\n\n");
                dialog.dispose();
                return;
            }

            if (!student.isCourseApproved(courseCode)) {
                outputArea.append("✗ Student not enrolled in course: " + courseCode + "\n\n");
                dialog.dispose();
                return;
            }

            Course course = null;
            for (Course c : student.getApprovedCourses()) {
                if (c.getCode().equals(courseCode)) {
                    course = c;
                    break;
                }
            }

            if (course != null) {
                studentService.gradeStudent(studentId, course);

                Double grade = student.getGradeForCourse(courseCode);
                outputArea.append("✓ Student " + studentId + " graded in " + course.getName() + "\n");
                outputArea.append("Grade: " + String.format("%.2f", grade) + "/100\n");
                outputArea.append(grade >= 50 ? "Status: ✓ PASSED\n\n" : "Status: ✗ FAILED\n\n");
            }

            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // 4. Enroll Student to Course
    private void enrollStudent() {
        Dialog dialog = new Dialog(this, "Enroll Student to Course", true);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));
        dialog.setSize(400, 200);

        Label idLabel = new Label("Student ID:");
        studentIdField = new TextField();

        Label courseLabel = new Label("Course Code:");
        courseCodeField = new TextField();

        Button enrollBtn = new Button("Enroll");
        Button cancelBtn = new Button("Cancel");

        dialog.add(idLabel);
        dialog.add(studentIdField);
        dialog.add(courseLabel);
        dialog.add(courseCodeField);
        dialog.add(enrollBtn);
        dialog.add(cancelBtn);

        enrollBtn.addActionListener(e -> {
            String studentId = studentIdField.getText();
            String courseCode = courseCodeField.getText();

            Student student = studentService.findStudent(studentId);
            Course course = courseService.getCourse(courseCode);

            if (student == null) {
                outputArea.append("✗ Invalid Student ID\n\n");
            } else if (course == null) {
                outputArea.append("✗ Invalid Course Code\n\n");
            } else {
                courseService.enrollStudent(courseCode, student);
                studentService.enrollToCourse(studentId, course);
                outputArea.append("✓ Student " + studentId + " enrolled in " + course.getName() + "\n\n");
            }

            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // 5. Show Students Summary
    private void showStudentsSummary() {
        outputArea.append("═══════════════════════════════════════\n");
        outputArea.append("          STUDENTS SUMMARY\n");
        outputArea.append("═══════════════════════════════════════\n\n");

        // Use a StringBuilder to capture the output
        StringBuilder summary = new StringBuilder();

        for (String key : studentService.getStudents().keySet()) {
            Student student = studentService.findStudent(key);
            summary.append(student.toString()).append("\n");

            List<Course> enrolledCourses = student.getApprovedCourses();

            if (enrolledCourses.size() > 0) {
                summary.append("  Enrolled Courses:\n");
                for (Course course : enrolledCourses) {
                    summary.append("  - ").append(course.toString()).append("\n");
                    Double grade = student.getGradeForCourse(course.getCode());
                    if (grade != null) {
                        summary.append("    Grade: ").append(String.format("%.2f", grade)).append("/100\n");
                    }
                }
            } else {
                summary.append("  No courses enrolled.\n");
            }
            summary.append("\n");
        }

        outputArea.append(summary.toString());
        outputArea.append("═══════════════════════════════════════\n\n");
    }

    // 6. Show Courses Summary
    private void showCoursesSummary() {
        outputArea.append("═══════════════════════════════════════\n");
        outputArea.append("          COURSES SUMMARY\n");
        outputArea.append("═══════════════════════════════════════\n\n");

        courseService.showSummary();

        // Since CourseService prints to console, let's manually add to outputArea
        outputArea.append("Course details displayed in console.\n");
        outputArea.append("Available courses: INTRO-CS-1, INTRO-CS-2, INTRO-CS-3, etc.\n\n");
        outputArea.append("═══════════════════════════════════════\n\n");
    }

    // 7. Show Course Average
    private void showCourseAverage() {
        Dialog dialog = new Dialog(this, "Show Course Average", true);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));
        dialog.setSize(350, 150);

        Label courseLabel = new Label("Course Code:");
        courseCodeField = new TextField();

        Button showBtn = new Button("Show Average");
        Button cancelBtn = new Button("Cancel");

        dialog.add(courseLabel);
        dialog.add(courseCodeField);
        dialog.add(showBtn);
        dialog.add(cancelBtn);

        showBtn.addActionListener(e -> {
            String courseCode = courseCodeField.getText();
            Course course = courseService.getCourse(courseCode);

            if (course == null) {
                outputArea.append("✗ Course not found\n\n");
            } else {
                double average = studentService.getCourseAverageGrade(courseCode);

                if (average == -1) {
                    outputArea.append("✗ No students have been graded in " + courseCode + "\n\n");
                } else {
                    outputArea.append("═══════════════════════════════════════\n");
                    outputArea.append("Course: " + course.getName() + " (" + courseCode + ")\n");
                    outputArea.append("Average Grade: " + String.format("%.2f", average) + "/100\n");
                    outputArea.append("═══════════════════════════════════════\n\n");
                }
            }

            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new StudentGenGUI();
    }
}