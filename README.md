# 🎓 Student Analytics System

A data-driven academic intelligence system built in Java with Python analytics — managing real student data, tracking attendance, analyzing IA marks, detecting at-risk students and generating professional PDF reports.

---

## 🚀 Project Evolution

| Version | What was built | Why |
|---------|---------------|-----|
| v1.0 | Java console app with CSV storage | Learn OOP and file handling |
| v2.0 | Python charts + PDF report via ProcessBuilder | Data needs visualization |
| v3.0 | MySQL database with JDBC | CSV can't handle relationships or complex queries |
| v3.1 | Real data + diagnostic analytics | Real data makes analytics actionable |

---

## ✨ Features

### Java Core
- Add, Search (by USN/Name), Update, Delete students
- Sort by CGPA and Name (ascending/descending)
- Top/Bottom performer analysis
- Department Statistics (avg CGPA, highest, lowest, backlogs)
- Student Report Card
- Backlog Students List
- Filter by CGPA range with CSV export
- Launch Python analytics directly from Java menu
- Input validation — no crashes on invalid input

### Python Analytics
- CGPA distribution histogram
- Top 10 students bar chart
- Backlog distribution pie chart
- Attendance % per subject chart
- IA marks average per subject chart
- At-risk student detection (attendance < 75%)
- Auto-generated PDF analytics report

### Database
- MySQL with 7 normalized tables
- 113 real students with gender, section, fees
- 16 real faculty members
- 16 subjects (C and D sections)
- 30,500+ attendance records
- 1,800+ IA marks records

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Core Application | Java (OOP, Collections, File I/O) |
| Database | MySQL + JDBC |
| Analytics | Python, Pandas, Matplotlib |
| Report Generation | ReportLab |
| Security | Environment Variables |
| Version Control | Git & GitHub |

---

## 📂 Project Structure
```
StudentManagementSystem/
│
├── src/
│   ├── StudentMain.java      # Main application + all classes
│   └── DBConnection.java     # MySQL JDBC connection and queries
│
├── visualize.py              # Python analytics, charts and PDF report
├── visualize.ipynb           # Jupyter notebook version
└── README.md
```

---

## ⚙️ Setup and Run

### Prerequisites
- Java JDK 17+
- MySQL 8.0+
- Python 3.x with libraries:
```
pip install pandas matplotlib reportlab sqlalchemy mysql-connector-python
```
- MySQL JDBC Connector JAR added to IntelliJ project

### Environment Variables (Required)
```
setx DB_PASSWORD "your_mysql_password"
setx DB_USER "root"
setx DB_URL "jdbc:mysql://localhost:3306/college_analytics"
```

### Run Java Application
```bash
cd src
javac StudentMain.java
java StudentMain
```

### Menu Options
```
1.  Add Student          8.  Top & Bottom Performers
2.  Search Student       9.  Show Statistics (Charts)
3.  Display Students     10. Generate PDF Report
4.  Delete Student       11. Department Statistics
5.  Update Student       12. Student Report Card
6.  Exit                 13. Backlog Students List
7.  Sort Students        14. Filter by CGPA
```

### Run Analytics Manually
```bash
python visualize.py          # show charts
python visualize.py report   # generate PDF report
```

---

## 📊 Database Schema

7 normalized tables:
`Students` → `Attendance` → `Subjects` → `Faculty`
`Students` → `IA_Marks` → `Subjects`
`Students` → `CGPA_History`
`Students` → `Assignments`

---

## 🔮 Roadmap

- [ ] v3.2 — HikariCP connection pooling
- [ ] v4.0 — ML prediction model (scikit-learn)
- [ ] v5.0 — Flask REST API + web dashboard
- [ ] v6.0 — Placement Analytics System
- [ ] v7.0 — Cloud deployment

---

## 👨‍💻 Author

**Samarth M**
BE Information Science Engineering
Maharaja Institute of Technology Mysore

[![GitHub](https://img.shields.io/badge/GitHub-samarthmgowda-black?logo=github)](https://github.com/samarthmgowda)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Samarth%20M-blue?logo=linkedin)](https://www.linkedin.com/in/samarth-m-27a6b630a)
