import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.InputMismatchException;

class User {
    private String username;
    private String password;
    private String role;
    private String linkedUsn;

    public User(String username, String password, String role, String linkedUsn) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.linkedUsn = linkedUsn;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }
    public String getLinkedUsn() {
        return linkedUsn;
    }
}

class Student{
    private String name;
    private String usn;
    private String dob;
    private double cgpa;
    private int backlogs;
    private String address;
    private String gender;
    private String section;
    private String department;
    private double fees;
    private double paidAmount;

    Student(String name,
            String usn,
            String dob,
            double cgpa,
            int backlogs,
            String address,
            String gender,
            String section,
            String department,
            double fees,
            double paidAmount
    ){
        this.name=name;
        this.usn=usn;
        this.dob=dob;
        this.address=address;
        this.gender = gender;
        this.section = section;
        this.department=department;
        this.fees=fees;
        this.paidAmount = paidAmount;
        setCgpa(cgpa);
        setBacklogs(backlogs);

    }
    public String getName(){
        return name;
    }
    public String getUsn(){
        return usn;
    }
    public String getDob(){
        return dob;
    }
    public double getCgpa(){
        return cgpa;
    }
    public int getBacklogs(){
        return backlogs;
    }
    public String getAddress(){
        return address;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setDob(String dob){
        this.dob=dob;
    }
    public void setAddress(String add){
        this.address=add;
    }
    public String getGender(){
        return gender;
    }
    public String getSection(){
        return section;
    }
    public double getFees(){
        return fees;
    }
    public String getDepartment(){
        return department;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public void setSection(String section){
        this.section = section;
    }
    public void setDepartment(String department){
        this.department = department;
    }
    public double getPaidAmount(){
        return paidAmount;
    }
    public void setPaidAmount(double paidAmount){
        this.paidAmount = paidAmount;
    }
    public double getBalance(){
        return fees - paidAmount;
    }
    public void setCgpa(double cgpa){
        if(cgpa<0||cgpa>10){
            System.out.println("CGPA should be in the range 0-10..!!!");
            return;
        }
        this.cgpa=cgpa;
    }
    public void setBacklogs(int backlogs){
        if(backlogs<0){
            System.out.println("Backlogs can't be Negative..!!!");
            return;
        }
        this.backlogs=backlogs;
    }
    public String toString(){
        return "\nName: " + name +
                "\nUSN: " + usn +
                "\nDOB: " + dob +
                "\nCgpa: " + cgpa +
                "\nBacklogs: " + backlogs +
                "\nAddress: " + address;
    }
    public String toCSV(){
        return name+","+usn+","+dob+","+cgpa+","+backlogs+","+address+","+gender+","+section+","+department;
    }
}
class StudentManager {
    private ArrayList<Student> students;

    public StudentManager() {
        students = new ArrayList<>();
    }

    public boolean addStudent(Student s) {
        for (Student st : students) {
            if (st.getUsn().equals(s.getUsn())) {
                return false;
            }
        }
        students.add(s);
        DBConnection.insertStudent(s);
        return true;
    }

    public boolean deleteStudent(String usn) {
        for (Student s : students) {
            if (s.getUsn().equals(usn)) {
                students.remove(s);
                DBConnection.deleteStudent(usn);
                return true;
            }
        }
        return false;
    }

    public Student searchStudent(String Usn) {
        for (Student s : students) {
            if (s.getUsn().equals(Usn)) {
                return s;
            }
        }
        return null;
    }

    public void searchByName(Scanner sc) {
        System.out.print("Enter Name to search : ");
        String name = sc.nextLine();
        System.out.println("==== Search Results ====");
        System.out.printf("%-25s %-15s %-10s %-10s%n", "Name", "USN", "CGPA", "Backlogs");
        System.out.println("------------------------");
        boolean found = false;
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(name.toLowerCase())) {
                found = true;
                System.out.printf("%-25s %-15s %-10s %-10s%n", s.getName(), s.getUsn(), s.getCgpa(), s.getBacklogs());
            }
        }
        if (!found)
            System.out.println("No Student found with Name: " + name);
        System.out.println("------------------------");
    }

    public void UpdateStudent(String Usn, Scanner sc) {
        Student s = searchStudent(Usn);
        if (s == null) {
            System.out.println("Student not found...!!!");
            return;
        }
        boolean updating = true;
        while (updating) {
            System.out.println("\n1. Name" +
                    "\n2. Dob" +
                    "\n3. address" +
                    "\n4. Cgpa" +
                    "\n5. Backlogs" +
                    "\n6. Complete updation" +
                    "\nEnter your choice");
            int chs = sc.nextInt();
            sc.nextLine();
            switch (chs) {
                case 1:
                    System.out.print("Enter new Name : ");
                    s.setName(sc.nextLine());
                    break;
                case 2:
                    System.out.print("Enter new Dob : ");
                    s.setDob(sc.nextLine());
                    break;
                case 3:
                    System.out.print("Enter new Address : ");
                    s.setAddress(sc.nextLine());
                    break;
                case 4:
                    System.out.print("Enter new Cgpa : ");
                    s.setCgpa(sc.nextDouble());
                    sc.nextLine();
                    break;
                case 5:
                    System.out.print("Enter new Backlogs : ");
                    s.setBacklogs(sc.nextInt());
                    break;
                case 6:
                    System.out.println("Updation Succesfull...!!!");
                    updating = false;
                    DBConnection.updateStudent(s);
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No Students Available");
            return;
        }
        for (Student st : students) {
            System.out.println(st);
        }
    }

    public void saveToFile() {
        try {
            FileWriter fw = new FileWriter("src/students.csv");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name,USN,DOB,CGPA,Backlogs,Address");
            bw.newLine();
            for (Student s : students) {
                bw.write(s.toCSV());
                bw.newLine();
            }
            bw.close();
            System.out.println("data saved succesfully!");
        } catch (IOException e) {
            System.out.println("Error occured while writing data onto file: " + e);
        }
    }

    public void sortStudents(Scanner sc) {
        System.out.println("1. Sort by Cgpa\n2. Sort by Name\nEnter your Choice");
        int chs = sc.nextInt();
        System.out.println("1. Ascending\n2. Descending\nEnter your Choice");
        int chs2 = sc.nextInt();
        switch (chs) {
            case 1:
                if (chs2 == 1) {
                    Collections.sort(students, (s1, s2) -> Double.compare(s1.getCgpa(), s2.getCgpa()));
                    displayAllStudents();
                } else {
                    Collections.sort(students, (s1, s2) -> Double.compare(s2.getCgpa(), s1.getCgpa()));
                    displayAllStudents();
                }
                break;
            case 2:
                if (chs2 == 1) {
                    Collections.sort(students, (s1, s2) -> s1.getName().compareTo(s2.getName()));
                    displayAllStudents();
                } else {
                    Collections.sort(students, (s1, s2) -> s2.getName().compareTo(s1.getName()));
                    displayAllStudents();
                }
                break;
            default:
                System.out.println("Entered invalid choice...!!!");
        }
    }

    public void topbottomPerformers() {
        ArrayList<Student> temp = new ArrayList<>(students);
        Collections.sort(temp, (s1, s2) -> Double.compare(s2.getCgpa(), s1.getCgpa()));
        if (temp.isEmpty()) {
            System.out.println("No students available to analyze.");
            return;
        }
        System.out.println("Top Performer -> " + temp.get(0));
        System.out.println("Bottom Performer -> " + temp.get(temp.size() - 1));
    }

    public void viewStatistics() {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "visualize.py");
            pb.directory(new File("C:\\Users\\Samarth\\Documents\\StudentManagementSystem"));
            Process p = pb.start();
            p.waitFor();
        } catch (IOException e) {
            System.out.println("Unable to Show Statistics");
        } catch (InterruptedException e) {
            System.out.println("Process interrupted!");
        }
    }

    public void generatereport() {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "visualize.py", "report");
            pb.directory(new File("C:\\Users\\Samarth\\Documents\\StudentManagementSystem"));
            Process p = pb.start();
            p.waitFor();
        } catch (IOException e) {
            System.out.println("Unable to Generate Report");
        } catch (InterruptedException e) {
            System.out.println("Process interrupted...!!!");
        }
    }

    public void departmentStatistics() {
        if (students.isEmpty()) {
            System.out.println("No Students Found...!!!");
            return;
        }
        Student hcgpa = students.get(0), lcgpa = students.get(0);
        int studwbck = 0;
        double totalcpa = 0;
        for (Student s : students) {
            totalcpa += s.getCgpa();
            if (s.getCgpa() > hcgpa.getCgpa())
                hcgpa = s;
            if (s.getCgpa() < lcgpa.getCgpa())
                lcgpa = s;
            if (s.getBacklogs() > 0)
                studwbck++;
        }
        System.out.println("===== Department Statistics =====" +
                "\nTotal Students           : " + students.size() +
                "\nAverage CGPA             : " + String.format("%.2f", totalcpa / students.size()) +
                "\nHighest CGPA             : " + hcgpa.getCgpa() + " " + hcgpa.getName() + " (" + hcgpa.getUsn() + ")" +
                "\nLowest CGPA              : " + lcgpa.getCgpa() + " " + lcgpa.getName() + " (" + lcgpa.getUsn() + ")" +
                "\nStudents with Backlog    : " + studwbck +
                "\nStudents without Backlog : " + (students.size() - studwbck) +
                "\n===================================");
    }

    public void studentReportCard(Scanner sc) {
        System.out.print("Enter the USN : ");
        String usn = sc.nextLine();
        Student s = searchStudent(usn);
        if (s == null) {
            System.out.println("Student not found...!!!");
            return;
        }
        System.out.println("======== Student Report Card ========" +
                "\nName          : " + s.getName() +
                "\nUSN           : " + s.getUsn() +
                "\nDate of Birth : " + s.getDob() +
                "\nCGPA          : " + s.getCgpa() +
                "\nBacklogs      : " + s.getBacklogs() +
                "\nAddress       : " + s.getAddress() +
                "\n====================================");
    }

    public void backlogStudentsList() {
        int bckstud = 0;
        if (students.isEmpty()) {
            System.out.println("No Students found...!!!");
            return;
        }
        System.out.println("======== Students with Backlogs ========");
        System.out.printf("%-25s %-15s %-10s%n", "Name", "USN", "Backlogs");
        System.out.println("---------------------------------------------");
        for (Student s : students) {
            if (s.getBacklogs() > 0) {
                bckstud++;
                System.out.printf("%-25s %-15s %-10s%n", s.getName(), s.getUsn(), s.getBacklogs());
            }
        }
        System.out.println("---------------------------------------------");
        System.out.println("Total number of Students with Backlogs : " + bckstud);
        System.out.println("========================================");
    }

    public void filterByCGPA(double mincgpa, double maxcgpa, Scanner sc) {
        System.out.println("==== Students in CGPA range " + mincgpa + " - " + maxcgpa + " ====");
        System.out.printf("%-25s %-15s %-10.2s %-10s%n", "Name", "USN", "CGPA", "Backlogs");
        System.out.println("----------------------------------------------");
        boolean found = false;
        ArrayList<Student> filteredList = new ArrayList<>();
        for (Student s : students) {
            if (s.getCgpa() <= maxcgpa && s.getCgpa() >= mincgpa) {
                found = true;
                filteredList.add(s);
                System.out.printf("%-25s %-15s %-12.2fa %-10s%n", s.getName(), s.getUsn(), s.getCgpa(), s.getBacklogs());
            }
        }
        if (!found)
            System.out.println("No Students found in that range...!!!");
        else {
            System.out.println("Export results to CSV ?  (YES/NO)");
            String ch = sc.nextLine();
            if (ch.equalsIgnoreCase("YES")) {
                System.out.print("Enter the file name: ");
                String fname = sc.nextLine();
                exportToCSV(filteredList, fname);
            }
        }
        System.out.println("----------------------------------------------");
    }

    public void exportToCSV(ArrayList<Student> filteredList, String filename) {
        try {
            FileWriter fw = new FileWriter(filename + ".csv");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Name,USN,DOB,CGPA,Backlogs,Address");
            bw.newLine();
            for (Student s : filteredList) {
                bw.write(s.toCSV());
                bw.newLine();
            }
            bw.close();
            System.out.println("data saved succesfully!");
        } catch (IOException e) {
            System.out.println("Error occured while writing data onto file: " + e);
        }
    }
    public void loadFromDB() {
        students = DBConnection.loadFromDB();
    }
    public void feeDefaulterList(){
        if(students.isEmpty()){
            System.out.println("No students found!");
            return;
        }
        int defaulters=0;
        double balance=0;
        System.out.println("===== Fee Defaulter List =====");
        System.out.printf("%-25s %-15s %-10s %-10s %-10s%n", "Name", "USN", "Total Fess","Fees paid","Balance");
        System.out.println("------------------------------");
        for(Student s:students){
            if(s.getBalance()>0){
                defaulters++;
                balance+=s.getBalance();
                System.out.printf("%-25s %-15s %-10s %-10s %-10s%n",
                        s.getName(),s.getUsn(),s.getFees(),s.getPaidAmount(),s.getBalance());
            }
        }
        System.out.println("------------------------------");
        System.out.println("Total Defaulters: "+defaulters+" students");
        System.out.println("Total pending amount: Rs. " +String.format("%,.0f", balance));
    }
    public void placementEligibility(Scanner sc) {
        System.out.println("===== Placement Eligibility =====");
        System.out.println("1. Check eligibility for a student");
        System.out.println("2. Show eligible students for a company");
        System.out.println("3. Export eligible students to CSV");
        System.out.println("4. Back");
        System.out.print("Enter choice: ");
        int ch = sc.nextInt();
        sc.nextLine();
        switch (ch) {
            case 1:
                checkStudentEligibility(sc);
                break;
            case 2: showEligibleStudents(sc); break;
            case 3: exportEligibleStudents(sc); break;
            case 4:
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }
    public void checkStudentEligibility(Scanner sc) {
        System.out.print("Enter the USN: ");
        String usn = sc.nextLine();
        Student s = searchStudent(usn);
        if (s == null) {
            System.out.println("Student not found...!!!");
            return;
        }
        ArrayList<String[]> companies = DBConnection.getCompanies();
        int eligible = 0;
        System.out.println("===== Placement Eligibility for " + s.getName() + " =====");
        System.out.printf("%-25s %-10s %-10s%n", "Company", "Min CGPA", "Status");
        System.out.println("------------------------------------------------");
        for (String[] company : companies) {
            double minCgpa = Double.parseDouble(company[2]);
            int maxBacklogs = Integer.parseInt(company[3]);
            String status;
            if (s.getCgpa() >= minCgpa && s.getBacklogs() <= maxBacklogs) {
                status = "Eligible ✓";
                eligible++;
            } else {
                status = "Not Eligible ✗";
            }
            System.out.printf("%-25s %-10s %-10s%n", company[1], company[2], status);
        }
        System.out.println("------------------------------------------------");
        System.out.println("Eligible for " + eligible + " out of " + companies.size() + " companies!");
    }

    public void showEligibleStudents(Scanner sc){
        ArrayList<String[]> companies = DBConnection.getCompanies();
        System.out.println("Available Companies:");
        System.out.printf("%-5s %-20s %-10s %-10s%n", "No.", "Company", "Min CGPA", "Max Backlogs");
        System.out.println("--------------------------------------------------");
        for(String[] company : companies){
            System.out.printf("%-5s %-20s %-10s %-10s%n",
                    company[0], company[1], company[2], company[3]);
        }
        System.out.println("--------------------------------------------------");
        System.out.print("Enter company name: ");
        String companyName = sc.nextLine();

        String[] selectedCompany = null;
        for(String[] c : companies){
            if(c[1].equalsIgnoreCase(companyName)){
                selectedCompany = c;
                break;
            }
        }
        if(selectedCompany == null){
            System.out.println("Company not found...!!!");
            return;
        }

        double minCgpa = Double.parseDouble(selectedCompany[2]);
        int maxBacklogs = Integer.parseInt(selectedCompany[3]);
        int eligible = 0;
        System.out.println("===== Students eligible for " + selectedCompany[1] + " =====");
        System.out.printf("%-25s %-15s %-10s%n", "Name", "USN", "CGPA");
        System.out.println("--------------------------------------------");
        for(Student s : students){
            if(s.getCgpa() >= minCgpa && s.getBacklogs() <= maxBacklogs){
                System.out.printf("%-25s %-15s %-10s%n", s.getName(), s.getUsn(), s.getCgpa());
                eligible++;
            }
        }
        System.out.println("--------------------------------------------");
        System.out.println("Total eligible: " + eligible + " out of " + students.size() + " students!");
    }
    public void exportEligibleStudents(Scanner sc){
        ArrayList<String[]> companies = DBConnection.getCompanies();
        System.out.println("Available Companies:");
        System.out.printf("%-5s %-20s %-10s %-10s%n", "No.", "Company", "Min CGPA", "Max Backlogs");
        System.out.println("--------------------------------------------------");
        for(String[] company : companies){
            System.out.printf("%-5s %-20s %-10s %-10s%n",
                    company[0], company[1], company[2], company[3]);
        }
        System.out.println("--------------------------------------------------");
        System.out.print("Enter company name: ");
        String companyName = sc.nextLine();
        String[] selectedCompany = null;
        for(String[] c : companies){
            if(c[1].equalsIgnoreCase(companyName)){
                selectedCompany = c;
                break;
            }
        }
        if(selectedCompany == null){
            System.out.println("Company not found...!!!");
            return;
        }
        double minCgpa = Double.parseDouble(selectedCompany[2]);
        int maxBacklogs = Integer.parseInt(selectedCompany[3]);
        ArrayList<Student> filteredList = new ArrayList<>();
        for(Student s : students){
            if(s.getCgpa() >= minCgpa && s.getBacklogs() <= maxBacklogs){
                filteredList.add(s);
            }
        }
        if(filteredList.isEmpty()){
            System.out.println("No eligible students found for " + companyName);
            return;
        }
        exportToCSV(filteredList, selectedCompany[1] + "_eligible");
        System.out.println(filteredList.size() + " eligible students exported to "
                + selectedCompany[1] + "_eligible.csv!");
    }

    public void checkStudentEligibility(String usn){
        Student s = searchStudent(usn);
        if(s == null){
            System.out.println("Student not found...!!!");
            return;
        }
        ArrayList<String[]> companies = DBConnection.getCompanies();
        int eligible = 0;
        System.out.println("===== Placement Eligibility for " + s.getName() + " =====");
        System.out.printf("%-25s %-10s %-10s%n", "Company", "Min CGPA", "Status");
        System.out.println("------------------------------------------------");
        for(String[] company : companies){
            double minCgpa = Double.parseDouble(company[2]);
            int maxBacklogs = Integer.parseInt(company[3]);
            String status;
            if(s.getCgpa() >= minCgpa && s.getBacklogs() <= maxBacklogs){
                status = "Eligible ✓";
                eligible++;
            } else {
                status = "Not Eligible ✗";
            }
            System.out.printf("%-25s %-10s %-10s%n", company[1], company[2], status);
        }
        System.out.println("------------------------------------------------");
        System.out.println("Eligible for " + eligible + " out of " + companies.size() + " companies!");
    }

}
public class StudentMain {

    public static void showAdminMenu(StudentManager manager, Scanner sc){
        while (true) {
            try {
                System.out.println("------- Student Database Menu --------" +
                        "\n1. Add a Student " +
                        "\n2. Search a Student " +
                        "\n3. Display Students " +
                        "\n4. Delete Students " +
                        "\n5. Update" +
                        "\n6. Exit" +
                        "\n7. Sorting" +
                        "\n8. Top & Bottom performers" +
                        "\n9. Show Statistics" +
                        "\n10. Generate Report" +
                        "\n11. Department Statistics" +
                        "\n12. Student Report Card" +
                        "\n13. Backlog Students List" +
                        "\n14. Filter by CGPA" +
                        "\n15. Placement Eligibility" +
                        "\n16. Fees Defaulters list" +
                        "\nEnter your Choice : ");
                System.out.flush();
                int ch = sc.nextInt();
                sc.nextLine();
                switch (ch) {
                    case 1:
                        System.out.println("Enter student Details :-");
                        System.out.print("Name : ");
                        String name = sc.nextLine();
                        System.out.print("Dob : ");
                        String dob = sc.nextLine();
                        System.out.print("Usn : ");
                        String usn = sc.nextLine();
                        System.out.print("Address : ");
                        String address = sc.nextLine();
                        System.out.print("Cgpa : ");
                        double cgpa = sc.nextDouble();
                        System.out.print("Backlogs : ");
                        int backlogs = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Gender(M/F): ");
                        String gender = sc.nextLine();
                        System.out.println("section(C/D): ");
                        String section = sc.nextLine();
                        System.out.println("Department: ");
                        String department = sc.nextLine();
                        System.out.println("Fees: ");
                        double fees = sc.nextDouble();
                        System.out.println("PaidAmount: ");
                        double paidamount = sc.nextDouble();
                        Student s = new Student(name, usn, dob, cgpa, backlogs,
                                address, gender, section, department, fees, paidamount);
                        if (manager.addStudent(s)) {
                            System.out.println("Student added succesfully");
                        } else
                            System.out.println("Student with Usn : " + usn + " already exist");
                        break;
                    case 2:
                        System.out.println("Search by\n1. USN\n2. Name");
                        int srch = sc.nextInt();
                        sc.nextLine();
                        switch (srch) {
                            case 1:
                                System.out.print("Enter the Usn to search : ");
                                String searchusn = sc.nextLine();
                                Student found = manager.searchStudent(searchusn);
                                if (found != null) {
                                    System.out.println("Student found : " + found);
                                } else
                                    System.out.println("Student not found....!!!");
                                break;
                            case 2:
                                manager.searchByName(sc);
                                break;
                            default:
                                System.out.println("Invalid Choice...!!!");
                        }
                        break;
                    case 3:
                        manager.displayAllStudents();
                        break;
                    case 4:
                        System.out.println("Enter Usn to delete : ");
                        String deleteusn = sc.nextLine();
                        if (manager.deleteStudent(deleteusn)) {
                            System.out.println("Student Deleted Succesfully...!!!");
                        } else
                            System.out.println("Student with Usn : " + deleteusn + " not found...!!!");
                        break;
                    case 5:
                        System.out.println("Enter Usn of Student to update : ");
                        String dusn = sc.nextLine();
                        manager.UpdateStudent(dusn, sc);
                        break;
                    case 6:
                        System.out.println("Exiting Program.....");
                        sc.close();
                        System.exit(0);
                        break;
                    case 7:
                        manager.sortStudents(sc);
                        break;
                    case 8:
                        manager.topbottomPerformers();
                        break;
                    case 9:
                        manager.saveToFile();
                        System.out.println("Generating visualizations...");
                        manager.viewStatistics();
                        break;
                    case 10:
                        manager.saveToFile();
                        manager.generatereport();
                        System.out.println("Generated Report Succesfully");
                        break;
                    case 11:
                        manager.departmentStatistics();
                        break;
                    case 12:
                        manager.studentReportCard(sc);
                        break;
                    case 13:
                        manager.backlogStudentsList();
                        break;
                    case 14:
                        System.out.print("Enter the Minimum CGPA : ");
                        double mincgpa = sc.nextDouble();
                        sc.nextLine();
                        System.out.print("Enter the Maximum CGPA : ");
                        double maxcgpa = sc.nextDouble();
                        sc.nextLine();
                        manager.filterByCGPA(mincgpa, maxcgpa, sc);
                        break;
                    case 15:
                        manager.placementEligibility(sc);
                        break;
                    case 16:
                        manager.feeDefaulterList();
                        break;
                    default:
                        System.out.println("Invalid Choice....!!!");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid input...!!!");
                sc.nextLine();
            }
        }
    }

    public static void showStudentMenu(StudentManager manager, Scanner sc, User currentUser){
        while(true){
            try{
                System.out.println("===== Student Menu ====="+
                        "\n1. View My Report Card"+
                        "\n2. Check My Placement Eligibility"+
                        "\n3. Logout");
                int ch = sc.nextInt();
                sc.nextLine();
                switch(ch){
                    case 1:
                        Student s = manager.searchStudent(currentUser.getLinkedUsn());
                        if(s != null){
                            System.out.println("======== Student Report Card ========");
                            System.out.printf("%-15s : %s%n", "Name", s.getName());
                            System.out.printf("%-15s : %s%n", "USN", s.getUsn());
                            System.out.printf("%-15s : %s%n", "Date of Birth", s.getDob());
                            System.out.printf("%-15s : %s%n", "CGPA", s.getCgpa());
                            System.out.printf("%-15s : %s%n", "Backlogs", s.getBacklogs());
                            System.out.printf("%-15s : %s%n", "Address", s.getAddress());
                            System.out.println("=====================================");
                        }
                        break;
                    case 2: manager.checkStudentEligibility(currentUser.getLinkedUsn()); break;
                    case 3: System.out.println("Logged out!"); return;
                    default: System.out.println("Invalid choice!");
                }
            } catch(InputMismatchException e){
                System.out.println("Invalid input!");
                sc.nextLine();
            }
        }
    }

    public static void showTeacherMenu(StudentManager manager, Scanner sc){
        while(true){
            try{
                System.out.println("===== Teacher Menu ====="+
                        "\n1. Search Student"+
                        "\n2. Display Students"+
                        "\n3. Sort Students"+
                        "\n4. Top & Bottom Performers"+
                        "\n5. Department Statistics"+
                        "\n6. Show Statistics"+
                        "\n7. Generate Report"+
                        "\n8. Backlog Students List"+
                        "\n9. Filter by CGPA"+
                        "\n10. Placement Eligibility"+
                        "\n11. Fee Defaulters"+
                        "\n12. Logout");
                int ch = sc.nextInt();
                sc.nextLine();
                switch(ch){
                    case 1:
                        System.out.println("Search by\n1. USN\n2. Name");
                        int srch = sc.nextInt(); sc.nextLine();
                        if(srch == 1){
                            System.out.print("Enter USN: ");
                            Student found = manager.searchStudent(sc.nextLine());
                            if(found != null) System.out.println(found);
                            else System.out.println("Not found!");
                        } else manager.searchByName(sc);
                        break;
                    case 2: manager.displayAllStudents(); break;
                    case 3: manager.sortStudents(sc); break;
                    case 4: manager.topbottomPerformers(); break;
                    case 5: manager.departmentStatistics(); break;
                    case 6: manager.saveToFile(); manager.viewStatistics(); break;
                    case 7: manager.saveToFile(); manager.generatereport(); break;
                    case 8: manager.backlogStudentsList(); break;
                    case 9:
                        System.out.print("Min CGPA: "); double min = sc.nextDouble(); sc.nextLine();
                        System.out.print("Max CGPA: "); double max = sc.nextDouble(); sc.nextLine();
                        manager.filterByCGPA(min, max, sc); break;
                    case 10: manager.placementEligibility(sc); break;
                    case 11: manager.feeDefaulterList(); break;
                    case 12: System.out.println("Logged out!"); return;
                    default: System.out.println("Invalid choice!");
                }
            } catch(InputMismatchException e){
                System.out.println("Invalid input!");
                sc.nextLine();
            }
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();
        manager.loadFromDB();
        System.out.println("Student Management System - v1.0");
        while(true) {
            System.out.println("===== LOGIN =====");
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();
            User currentUser = DBConnection.login(username, password);
            if (currentUser == null) {
                System.out.println("Invalid credentials!");
                return;
            } else
                System.out.println("Login Succesfull...!!!");

            if (currentUser.getRole().equals("ADMIN")) {
                showAdminMenu(manager, sc);
            } else if (currentUser.getRole().equals("TEACHER")) {
                showTeacherMenu(manager, sc);
            } else if (currentUser.getRole().equals("STUDENT")) {
                showStudentMenu(manager, sc, currentUser);
            }
        }
    }
}