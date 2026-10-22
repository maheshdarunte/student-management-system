import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==========================================");
            System.out.println("      STUDENT MANAGEMENT SYSTEM (CRUD)    ");
            System.out.println("==========================================");
            System.out.println("1. Add New Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student Record");
            System.out.println("4. Delete Student Record");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 5.");
                continue;
            }

            switch (choice) {
                case 1:
                    // CREATE OPERATION
                    System.out.print("Enter Student Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Student Email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter Enrolled Course: ");
                    String course = sc.nextLine();

                    System.out.print("Enter Course Fee: ");
                    double fee;
                    try {
                        fee = Double.parseDouble(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid fee format. Operation cancelled.");
                        break;
                    }

                    Student newStudent = new Student(name, email, course, fee);
                    if (dao.addStudent(newStudent)) {
                        System.out.println("✓ Student registered successfully!");
                    } else {
                        System.out.println("✗ Failed to add student. Please check database connection.");
                    }
                    break;

                case 2:
                    // READ OPERATION
                    List<Student> students = dao.getAllStudents();
                    System.out.println("\n--------------- CURRENT STUDENT RECORDS ---------------");
                    if (students.isEmpty()) {
                        System.out.println("No student records found in the database.");
                    } else {
                        for (Student s : students) {
                            System.out.println(s);
                        }
                    }
                    System.out.println("-------------------------------------------------------");
                    break;

                case 3:
                    // UPDATE OPERATION
                    System.out.print("Enter the ID of the student to update: ");
                    int updateId;
                    try {
                        updateId = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID format.");
                        break;
                    }

                    System.out.print("Enter New Course Name: ");
                    String newCourse = sc.nextLine();

                    System.out.print("Enter New Fee: ");
                    double newFee;
                    try {
                        newFee = Double.parseDouble(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid fee format.");
                        break;
                    }

                    if (dao.updateStudent(updateId, newCourse, newFee)) {
                        System.out.println("✓ Student record updated successfully!");
                    } else {
                        System.out.println("✗ Update failed. Verify that ID " + updateId + " exists.");
                    }
                    break;

                case 4:
                    // DELETE OPERATION
                    System.out.print("Enter the ID of the student to delete: ");
                    int deleteId;
                    try {
                        deleteId = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID format.");
                        break;
                    }

                    if (dao.deleteStudent(deleteId)) {
                        System.out.println("✓ Student record deleted successfully!");
                    } else {
                        System.out.println("✗ Delete failed. Verify that ID " + deleteId + " exists.");
                    }
                    break;

                case 5:
                    System.out.println("Exiting the application. Goodbye!");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice! Please select an option from 1 to 5.");
            }
        }
    }
}