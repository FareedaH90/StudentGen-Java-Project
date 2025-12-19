# StudentGen Management System 🎓

A Student Management System built with Java, implementing Object-Oriented Programming principles with both console and GUI interfaces.

## 📖 Project Overview

StudentGen is a student management system developed as part of a Java OOP assignment. The system manages students, courses, enrollments, and grades through both a command-line interface and a graphical user interface (GUI).

---

## ✨ Features

### Core Functionality
- **Student Registration** with comprehensive validation
- **Student Search** by ID
- **Course Enrollment** for students
- **Automatic Grading** (random grades 40-100)
- **Student Summary** with enrolled courses and grades
- **Course Summary** showing all available courses
- **Course Average Grade** calculation
- **Passed Courses Filter** (grades ≥ 50)

### GUI Interface
- 8-button menu with intuitive navigation
- Modal dialogs for data entry
- Real-time output console
- User-friendly error messages

![studentgen.jpg](../../../Documents/bakars/studentgen.jpg)

---

## 🛠️ Technologies Used

- **Language:** Java 13+
- **GUI Framework:** Java AWT
- **IDE:** IntelliJ IDEA
- **Version Control:** Git & GitHub
- **Testing:** JUnit

---

## 💻 How to Use

### Running the Console Version
```bash
java com.generation.Main
```

### Running the GUI Version
```bash
java com.generation.StudentGenGUI
```

### Available Courses
- `INTRO-CS-1` - Introduction to Computer Science
- `INTRO-CS-2` - Introduction to Algorithms
- `INTRO-CS-3` - Algorithm Design and Problem Solving
- `INTRO-CS-4` - Introduction to Programming
- `INTRO-CS-5` - Intermediate Programming
- `INTRO-CS-6` - Advanced Programming
- `INTRO-CS-7` - Introduction to Web Development

---

## 📸 GUI Screenshots

### Main Interface
![Main Interface](screenshots/main-interface.png)
*StudentGen main window with 8-button menu and output console*

### Student Registration
![Student Registration](screenshots/register-student.png)
*Registration form with validation*

### Grading System
![Grading](screenshots/grade-student.png)
*Auto-grading with pass/fail status*

### Students Summary
![Students Summary](screenshots/students-summary.png)
*Complete student listing with enrolled courses and grades*

---

## 🔧 Key Implementations & Learnings

### 1. Date Validation with Exception Handling ✅

**Challenge:** Validate user input dates in DD/MM/YYYY format and handle invalid entries gracefully.

**Implementation:**
```java
Date birthDate = new Date();
Boolean dateValidated = false;
SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

do {
    try {
        birthDate = formatter.parse(scanner.next());
        dateValidated = true;
    } catch (Exception exception) {
        System.out.println("Date is not valid! Please use DD/MM/YYYY format.");
    }
} while (!dateValidated);
```

**What I Learned:**
- Using `try-catch` blocks for robust exception handling
- Implementing `SimpleDateFormat` for date parsing
- Creating validation loops that continue until correct input is received
- Providing clear, user-friendly error messages
- Understanding Java's exception hierarchy

**Testing Results:**
- ✅ Valid dates accepted: `16/01/2018`, `29/02/2020` (leap year)
- ✅ Invalid formats rejected: `32/01/2018`, `16-01-2018`, `abc`, `fatima`
- ✅ Loop continues prompting until valid date is entered
- ✅ No crashes from invalid input

**Key Takeaway:** Exception handling is essential for creating robust applications that don't crash on invalid user input.

---

### 2. Fixing the Grade Compounding Bug 🐛

**Problem:** When grading the same course multiple times, credits kept adding up (9 → 18 → 27 → 36) instead of replacing the previous grade.

**Original Buggy Code:**
```java
public void setGrade(double grade) {
    this.grade += grade;  // ❌ This keeps adding!
}
```

**Test Case That Failed:**
```
1. Grade student 002 in INTRO-CS-1 → Total: 9.0
2. Grade student 002 in INTRO-CS-2 → Total: 18.0
3. Grade student 002 in INTRO-CS-2 again → Total: 27.0 ❌ (Should be 18.0!)
```

**Solution:** Used a HashMap to store individual grades per course:
```java
// In Student.java
private Map<String, Double> courseGrades = new HashMap<>();

public void gradeInCourse(String courseCode, double grade) {
    courseGrades.put(courseCode, grade);  // ✅ Replaces if key exists
}

public Double getGradeForCourse(String courseCode) {
    return courseGrades.get(courseCode);
}

public double getTotalGrade() {
    double total = 0;
    for (Double grade : courseGrades.values()) {
        total += grade;
    }
    return total;
}
```

**What I Learned:**
- Understanding the difference between `=` (assignment) and `+=` (accumulation)
- Using `HashMap<String, Double>` for key-value pair storage
- The `put()` method automatically replaces existing values
- Iterating through HashMap values to calculate aggregates
- Why data structures matter for preventing bugs

**Result After Fix:**
```
1. Grade student 002 in INTRO-CS-1 → Total: 9.0
2. Grade student 002 in INTRO-CS-2 → Total: 18.0
3. Grade student 002 in INTRO-CS-2 again → Total: 18.0 ✅ (Correct!)
```

**Key Takeaway:** Choosing the right data structure is crucial. A HashMap prevents duplicate keys and automatically replaces values, solving the compounding issue elegantly.

---

### 3. Implementing Passed/Failed Course Filter 📊

**Challenge:** Identify which courses a student passed (grade ≥ 50) to determine academic progress.

**Implementation:**
```java
// In Student.java
private static final double PASSING_GRADE = 50.0;

public List<Course> findPassedCourses(Course course) {
    List<Course> passedCourses = new ArrayList<>();
    
    // Loop through all graded courses
    for (String courseCode : courseGrades.keySet()) {
        double grade = courseGrades.get(courseCode);
        
        // Check if grade meets passing threshold
        if (grade >= PASSING_GRADE && approvedCourses.containsKey(courseCode)) {
            passedCourses.add(approvedCourses.get(courseCode));
        }
    }
    
    return passedCourses;
}
```

**What I Learned:**
- Filtering collections based on conditional logic
- Working with multiple data structures simultaneously (HashMap and List)
- Using constants (`PASSING_GRADE`) for maintainable thresholds
- Iterating through HashMap with `keySet()`
- Building new collections from existing data

**Example Usage:**
```java
Student student = studentService.findStudent("002");
List<Course> passed = student.findPassedCourses(null);

// Output:
// ✓ INTRO-CS-1: 75.50 - PASSED
// ✓ INTRO-CS-2: 82.30 - PASSED
// ✗ INTRO-CS-3: 45.20 - FAILED (not in list)
```

**Key Takeaway:** Breaking down complex requirements into simple conditional checks makes code easier to understand and maintain.

---

### 4. Course Average Grade Calculation 📈

**Challenge:** Calculate the average grade of all students enrolled in a specific course for performance tracking.

**Implementation:**
```java
// In StudentService.java
public double getCourseAverageGrade(String courseCode) {
    double totalGrade = 0;
    int studentCount = 0;
    
    // Loop through all students in the system
    for (Student student : students.values()) {
        // Get grade for this specific course
        Double grade = student.getGradeForCourse(courseCode);
        
        if (grade != null) {  // Only count students who have been graded
            totalGrade += grade;
            studentCount++;
        }
    }
    
    // Return average or -1 if no students have been graded
    return studentCount > 0 ? totalGrade / studentCount : -1;
}
```

**What I Learned:**
- Aggregating data across multiple objects (students)
- Handling null values gracefully when data is missing
- Using ternary operators (`? :`) for concise conditional returns
- Calculating statistical measures (average)
- Returning sentinel values (-1) to indicate "no data"

**Example Output:**
```
Course: Introduction to Computer Science (INTRO-CS-1)
Students graded: 3
Grades: 70.00, 85.00, 65.00
Average Grade: 73.33/100
```

**Edge Cases Handled:**
- ✅ No students enrolled → Returns -1
- ✅ Students enrolled but not graded → Returns -1
- ✅ Some students graded, others not → Only counts graded students
- ✅ All students graded → Correct average

**Key Takeaway:** Always consider edge cases like missing data and handle them gracefully to prevent crashes.

---

### 5. Enhanced GUI with Complete Validation ✨

**Challenge:** Create a user-friendly GUI that prevents invalid data entry before it reaches the database.

**Email Validation:**
```java
private boolean isValidEmail(String email) {
    String emailRegex = "^[a-zA-Z0-9_+&*-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,7}$";
    return email.matches(emailRegex);
}

// Usage in registration
if (!isValidEmail(email)) {
    outputArea.append("✗ ERROR: Invalid email format!\n");
    outputArea.append("Email must be in format: example@domain.com\n\n");
    return;
}
```

**Name Validation:**
```java
// Only letters, spaces, and hyphens allowed (2-50 characters)
if (!name.matches("^[a-zA-Z\\s\\-]{2,50}$")) {
    outputArea.append("✗ ERROR: Invalid name!\n");
    outputArea.append("Name must contain only letters (2-50 characters).\n\n");
    return;
}
```

**Student ID Validation:**
```java
// Alphanumeric, 3-10 characters, must be unique
if (!id.matches("^[a-zA-Z0-9]{3,10}$")) {
    outputArea.append("✗ ERROR: Invalid Student ID!\n");
    outputArea.append("ID must be 3-10 alphanumeric characters.\n\n");
    return;
}

if (studentService.findStudent(id) != null) {
    outputArea.append("✗ ERROR: Student ID '" + id + "' already exists!\n\n");
    return;
}
```

**Date Validation (GUI Enhanced Version):**
```java
private Date parseDateFromString(String dateStr) {
    try {
        // Parse DD/MM/YYYY format
        String[] parts = dateStr.split("/");
        if (parts.length != 3) return null;
        
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        
        // Validate ranges
        if (month < 1 || month > 12) return null;
        if (day < 1 || day > 31) return null;
        if (year < 1900 || year > 2024) return null;
        
        // Validate days per month (handles leap years)
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (isLeapYear(year)) daysInMonth[1] = 29;
        if (day > daysInMonth[month - 1]) return null;
        
        // Create and return date
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, 0, 0, 0);
        return cal.getTime();
        
    } catch (Exception e) {
        return null;
    }
}

private boolean isLeapYear(int year) {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
}
```

**What I Learned:**
- Regular expressions for powerful pattern matching
- Input sanitization and validation at the entry point
- Creating reusable validation methods
- Building informative error messages that guide users
- Preventing bad data from entering the system

**Validation Examples:**

| Input | Valid? | Reason |
|-------|--------|--------|
| `fareeda@gmail.com` | ✅ | Correct email format |
| `as` | ❌ | Not an email format |
| `John Doe` | ✅ | Valid name |
| `John123` | ❌ | Contains numbers |
| `004` | ✅ | Valid student ID |
| `01` | ❌ | Too short (< 3 chars) |
| `16/01/2018` | ✅ | Valid date |
| `32/01/2018` | ❌ | Day 32 doesn't exist |
| `29/02/2020` | ✅ | Leap year |
| `29/02/2019` | ❌ | Not a leap year |

**Key Takeaway:** Validate early, validate thoroughly. Preventing bad data at the entry point saves countless hours of debugging later.

---

## 📚 Overall Learnings

### Object-Oriented Programming Concepts
- ✅ **Inheritance:** `Student extends Person`
- ✅ **Interfaces:** Implementing `Evaluation` interface
- ✅ **Encapsulation:** Private fields with public getter/setter methods
- ✅ **Polymorphism:** Method overriding in child classes

### Data Structures Mastery
- ✅ `HashMap<String, Student>` - Fast student lookup by ID
- ✅ `HashMap<String, Double>` - Store grades per course without duplication
- ✅ `ArrayList<Course>` - Maintain ordered list of enrolled courses
- ✅ Understanding when to use each data structure

### Problem-Solving Skills
- ✅ Debugging the compounding credits issue systematically
- ✅ Implementing robust validation with exception handling
- ✅ Replacing values instead of accumulating them
- ✅ Calculating aggregates across multiple objects
- ✅ Handling edge cases gracefully

### Java Programming Skills
- ✅ Exception handling with `try-catch` blocks
- ✅ Date parsing and validation with `SimpleDateFormat` and `Calendar`
- ✅ Regular expressions for input validation
- ✅ Building GUI applications with Java AWT
- ✅ Event handling with `ActionListener`
- ✅ Working with collections framework

### Software Development Practices
- ✅ Using Git for version control
- ✅ Writing clean, readable code with meaningful variable names
- ✅ Creating comprehensive documentation
- ✅ Testing with various input cases
- ✅ Handling edge cases and error conditions
- ✅ Providing helpful error messages to users

---

## 🚀 Future Enhancements

### Planned Features
- [ ] **Database Integration** - Replace in-memory storage with MySQL/PostgreSQL
- [ ] **JavaFX Migration** - Upgrade from AWT to modern JavaFX UI
- [ ] **Manual Grading** - Allow instructors to enter grades manually
- [ ] **Multiple Assessments** - Support midterms, finals, assignments with weights
- [ ] **GPA Calculation** - Calculate cumulative GPA for students
- [ ] **PDF Reports** - Export student transcripts and grade reports
- [ ] **User Authentication** - Login system for admins, teachers, and students
- [ ] **Attendance Tracking** - Monitor student attendance per course
- [ ] **Email Notifications** - Send grade reports to students via email
- [ ] **Advanced Search** - Filter students by name, course, grade range
- [ ] **Data Import/Export** - Bulk import students from CSV/Excel files

### Technical Improvements
- [ ] Increase unit test coverage to 90%+
- [ ] Add integration tests
- [ ] Implement logging framework
- [ ] Add JavaDoc documentation
- [ ] Refactor to MVC architecture
- [ ] Performance optimization for large datasets

---

##  Testing

### Unit Tests Implemented

**StudentServiceTest.java:**
```java
@Test
void testIsSubscribed_ValidStudent() {
    // Verifies registered students are found in the system
}

@Test
void testIsSubscribed_InvalidStudent() {
    // Verifies unregistered students return false
}

@Test
void testGetCourse_ValidCode() {
    // Tests retrieving courses by their code
}

@Test
void testEnrollStudent_Success() {
    // Tests successful student enrollment in courses
}
```

### Manual Testing Scenarios

**Date Validation:**
- Valid dates: `16/01/2018`, `29/02/2020`, `31/12/2000`
- Invalid dates: `32/01/2018`, `30/02/2020`, `16-01-2018`, `abc`

**Email Validation:**
-  Valid: `student@email.com`, `john.doe@university.edu`
-  Invalid: `as`, `@gmail.com`, `student@`, `student`

**Grade System:**
-  Grades replace instead of compound
-  Average calculation is accurate
-  Passed courses filter works (≥50)
-  Failed courses excluded from passed list

**GUI Testing:**
-  All 8 menu buttons functional
-  Dialogs open and close properly
-  Error messages display correctly
-  Output console updates in real-time

---

## 📝 Assignment Requirements Completion

 **Part 1:** Understand the project structure  
 **Part 2:** Implement Student and StudentService methods  
 **Part 3:** Implement gradeStudent() method in Main.java  
 **Part 4:** Handle date format exceptions with try-catch  
 **Part 5:** Write unit tests (2+ tests for each service)  
 **Challenge 1:** Store grades per course + implement findPassedCourses()  
 **Challenge 2:** Calculate average grade for courses  
 **Extra Credit:** Built comprehensive GUI with full validation

---

## 👨‍💻 Author

**Fareeda**

- Email: fareedahab@gmail.com

---

## 🙏 Acknowledgments

- **Instructor:** Martin Leong - For assignment guidance and support
- **Java Documentation** - For comprehensive API references
- **Stack Overflow Community** - For troubleshooting assistance
- **IntelliJ IDEA** - For excellent development tools

---

## 📄 License

This project was created for educational purposes as part of a Java OOP assignment.

---

**Last Updated:** December 2025  
**Version:** 1.0.0  
**Status:**  Assignment Complete |  Open for Enhancements

---

*Built with ☕ Java and 💪 determination*