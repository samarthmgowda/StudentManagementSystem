import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DBConnection {
    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL successfully!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }
    public static void insertStudent(Student s) {
        String query = "INSERT INTO Students (name, usn, dob, address, gender, section, department," +
                " current_semester, batch_year)" +
                " VALUES (?, ?, STR_TO_DATE(?, '%d-%m-%Y'), ?, ?, ?, ?, ?, ?, 4, 2024)";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, s.getName());
            ps.setString(2, s.getUsn());
            ps.setString(3, s.getDob());
            ps.setString(4, s.getAddress());
            ps.setString(7, s.getGender());
            ps.setString(8, s.getSection());
            ps.setString(9, s.getDepartment());
            ps.executeUpdate();
            System.out.println("Student inserted into MySQL successfully!");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error inserting student: " + e.getMessage());
        }
    }
    public static ArrayList<Student> loadFromDB() {
        ArrayList<Student> students = new ArrayList<>();
        String query = "SELECT * FROM Students";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                String name = rs.getString("name");
                String usn = rs.getString("usn");
                String dob = rs.getString("dob");
                double cgpa = rs.getDouble("cgpa");
                int backlogs = rs.getInt("backlogs");
                String address = rs.getString("address");
                String gender = rs.getString("gender");
                String section = rs.getString("section");
                String department = rs.getString("department");
                double fees = rs.getDouble("fees");
                students.add(new Student(name, usn, dob, cgpa, backlogs, address,gender,section,department,fees));
            }
            conn.close();
            System.out.println(students.size() + " students loaded from MySQL!");
        } catch (SQLException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
        return students;
    }
    public static void updateCGPAAndBacklogs() {
        String csvPath = "src/students.csv";
        String query = "UPDATE Students SET cgpa = ?, backlogs = ? WHERE usn = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            BufferedReader br = new BufferedReader(new FileReader(csvPath));
            br.readLine(); // skip header
            String line;
            int count = 0;
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                ps.setDouble(1, Double.parseDouble(data[3])); // cgpa
                ps.setInt(2, Integer.parseInt(data[4]));       // backlogs
                ps.setString(3, data[1]);                      // usn
                ps.executeUpdate();
                count++;
            }
            br.close();
            conn.close();
            System.out.println(count + " students updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public static void deleteStudent(String usn) {
        String query = "DELETE FROM Students WHERE usn = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, usn);
            ps.executeUpdate();
            conn.close();
            System.out.println("Student deleted from MySQL!");
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }
    public static void updateStudent(Student s) {
        String query = "UPDATE Students SET name=?, dob=?, cgpa=?, backlogs=?, address=? WHERE usn=?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, s.getName());
            ps.setString(2, s.getDob());
            ps.setDouble(3, s.getCgpa());
            ps.setInt(4, s.getBacklogs());
            ps.setString(5, s.getAddress());
            ps.setString(6, s.getGender());
            ps.setString(7, s.getSection());
            ps.setString(8, s.getDepartment());
            ps.setString(6, s.getUsn());
            ps.executeUpdate();
            conn.close();
            System.out.println("Student updated in MySQL!");
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ArrayList<Student> students = loadFromDB();
        for(Student s : students){
            System.out.println(s.getName() + " - " + s.getCgpa());
        }
    }
}
