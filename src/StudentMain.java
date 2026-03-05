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
    public void setName(String name){
        this.name=name;
    }
    public void setDob(String dob){
        this.dob=dob;
    }
    public void setAddress(String add){
        this.address=add;
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
    public boolean deleteStudent(String usn) {
        for (Student s : students) {
            if (s.getUsn().equals(usn)) {
                students.remove(s);
                return true;
            }
        }
        return false;
    }
    public Student searchStudent(String Usn){
        for(Student s : students){
            if(s.getUsn().equals(Usn)){
                return s;
            }
        }
        return null;
    }
    public void UpdateStudent(String Usn,Scanner sc){
        Student s = searchStudent(Usn);
        if(s==null){
            System.out.println("Student not found...!!!");
        }
        boolean updating = true;
        while(updating){
            System.out.println("\n1. Name" +
                    "\n2. Dob" +
                    "\n3. address" +
                    "\n4. Cgpa" +
                    "\n5. Backlogs" +
                    "\n6. Complete updation" +
                    "\nEnter your choice");
            int chs=sc.nextInt();
            sc.nextLine();
            switch(chs){
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
                    break;
                case 5:
                    System.out.print("Enter new Backlogs : ");
                    s.setBacklogs(sc.nextInt());
                    break;
                case 6:
                    System.out.println("Updation Succesfull...!!!");
                    updating=false;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
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
                    "\n3. Display Students "+
                    "\n4. Delete Students "+
                    "\n5. Update" +
                    "\n6. Exit"+
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
                    System.out.println("Enter Usn to delete : ");
                    String deleteusn=sc.nextLine();
                    if(manager.deleteStudent(deleteusn)){
                        System.out.println("Student Deleted Succesfully...!!!");
                    }
                    else
                        System.out.println("Student with Usn : "+deleteusn+" not found...!!!");
                    break;
                case 5:
                    System.out.println("Enter Usn of Student to update : ");
                    String dusn=sc.nextLine();
                    manager.UpdateStudent(dusn,sc);
                    break;
                case 6:
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