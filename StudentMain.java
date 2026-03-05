import java.util.ArrayList;
import java.util.Scanner;

class Student{
    private String name;
    private String usn;
    private String dob;
    private double cgpa;
    private int backlogs;
    private String address;

    Student(String name,
            String usn,
            String dob,
            double cgpa,
            int backlogs,
            String address
    ){
        this.name=name;
        this.usn=usn;
        this.dob=dob;
        this.address=address;
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
}
class StudentManager{
    private ArrayList<Student> students;
    public StudentManager(){
        students = new ArrayList<>();
    }
    public boolean addStudent(Student s){
        for(Student st: students){
            if(st.getUsn().equals(s.getUsn())){
                return false;
            }
        }
        students.add(s);
        return true;
    }
    public Student searchStudent(String Usn){
        for(Student s : students){
            if(s.getUsn().equals(Usn)){
                return s;
            }
        }
        return null;
    }
    public void displayAllStudents(){
        if(students.isEmpty()){
            System.out.println("No Students Available");
            return;
        }
        for(Student st : students){
            System.out.println(st);
        }
    }
}
public class StudentMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();
        sampledata(manager);
        while (true) {
            System.out.println("------- Student Database Menu --------" +
                    "\n1. Add a Student " +
                    "\n2. Search a Student " +
                    "\n3. Display Students " +
                    "\n4. Exit" +
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
                    Student s = new Student(name, usn, dob, cgpa, backlogs, address);
                    if (manager.addStudent(s)) {
                        System.out.println("Student added succesfully");
                    } else
                        System.out.println("Student with Usn : " + usn + " already exist");
                    break;
                case 2:
                    System.out.print("Enter the Usn to search : ");
                    String searchusn = sc.nextLine();
                    Student found = manager.searchStudent(searchusn);
                    if (found != null) {
                        System.out.println("Student found : " + found);
                    } else
                        System.out.println("Student not found....!!!");
                    break;
                case 3:
                    manager.displayAllStudents();
                    break;
                case 4:
                    System.out.println("Exiting Proogram.....");
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Choice....!!!");

            }

        }
    }
    // SAMPLE DATA
    private static void sampledata(StudentManager manager) {
        manager.addStudent(new Student("Samartha M", "4MH24IS089", "03-12-2006", 9.65, 0, "Mysore"));
        manager.addStudent(new Student("Tushaar Singavi", "4MH24IS108", "21-03-2006", 9.4, 2, "Mumbai"));
        manager.addStudent(new Student("Yashwanth Gowda", "4MH24IS115", "13-03-2006", 9.2, 4, "Delhi"));
    }
}