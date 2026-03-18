import pandas as pd
import matplotlib.pyplot as plt
import mysql.connector
from reportlab.lib.pagesizes import A4
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, Image
from reportlab.lib.styles import getSampleStyleSheet
from sqlalchemy import create_engine
import datetime
import sys

plt.style.use('ggplot')

DB_URL = "mysql+mysqlconnector://root:3610@localhost/college_analytics"

def load_data():
    engine = create_engine(DB_URL)
    df = pd.read_sql("SELECT * FROM Students", engine)
    return df

def get_attendance_data():
    engine = create_engine(DB_URL)
    query = """
        SELECT sub.subject_name,
               ROUND(COUNT(CASE WHEN a.status='Present' THEN 1 END) * 100.0 / COUNT(*), 2) as attendance_pct
        FROM Attendance a
        JOIN Subjects sub ON a.subject_id = sub.subject_id
        GROUP BY sub.subject_name
        ORDER BY attendance_pct ASC
    """
    return pd.read_sql(query, engine)

def get_at_risk_data():
    engine = create_engine(DB_URL)
    query = """
        SELECT s.name, s.usn,
               ROUND(COUNT(CASE WHEN a.status='Present' THEN 1 END) * 100.0 / COUNT(*), 2) as attendance_pct
        FROM Students s
        JOIN Attendance a ON s.student_id = a.student_id
        GROUP BY s.student_id
        HAVING attendance_pct < 75
        ORDER BY attendance_pct ASC
    """
    return pd.read_sql(query, engine)

def get_ia_data():
    engine = create_engine(DB_URL)
    query = """
        SELECT sub.subject_name,
               ROUND(AVG(ia.marks_obtained), 2) as avg_marks
        FROM IA_Marks ia
        JOIN Subjects sub ON ia.subject_id = sub.subject_id
        GROUP BY sub.subject_name
        ORDER BY avg_marks ASC
    """
    return pd.read_sql(query, engine)

def show_charts(df):
    # Chart 1 - CGPA Distribution histogram
    plt.figure(figsize=(6,4))
    plt.hist(df["cgpa"], bins=5, color="skyblue", edgecolor="black")
    plt.title("CGPA Distribution")
    plt.xlabel("CGPA Range")
    plt.ylabel("Number of Students")
    plt.tight_layout()

    # Chart 2 - Top 10 students
    top_students = df.sort_values(by="cgpa", ascending=False).head(10)
    plt.figure(figsize=(8,5))
    plt.bar(top_students["name"], top_students["cgpa"], color="gold")
    plt.legend(["CGPA"], loc="upper right")
    plt.xticks(rotation=45, ha='right')
    plt.title("Top 10 Students by CGPA")
    plt.xlabel("Student")
    plt.ylabel("CGPA")
    plt.ylim(0, 10)
    plt.tight_layout()

    # Chart 3 - Pie chart
    no_backlogs = len(df[df["backlogs"] == 0])
    with_backlogs = len(df[df["backlogs"] > 0])
    plt.figure(figsize=(5,5))
    explode = (0.1, 0)
    plt.pie([no_backlogs, with_backlogs],
            labels=["No Backlogs", "Has Backlogs"],
            colors=["lightgreen", "salmon"],
            autopct="%1.1f%%", shadow=True, explode=explode)
    plt.title("Students with/without Backlogs")
    plt.legend()

    # Chart 4 - Attendance per subject
    att_df = get_attendance_data()
    plt.figure(figsize=(12,6))
    plt.bar(att_df["subject_name"], att_df["attendance_pct"], color="steelblue")
    plt.axhline(y=75, color='red', linestyle='--', label='75% threshold')
    plt.xticks(rotation=45, ha='right')
    plt.title("Average Attendance % per Subject")
    plt.ylabel("Attendance %")
    plt.ylim(0, 100)
    plt.legend()
    plt.tight_layout()

    # Chart 5 - IA marks per subject
    ia_df = get_ia_data()
    plt.figure(figsize=(12,6))
    plt.bar(ia_df["subject_name"], ia_df["avg_marks"], color="mediumpurple")
    plt.xticks(rotation=45, ha='right')
    plt.title("Average IA Marks per Subject")
    plt.ylabel("Average Marks (out of 30)")
    plt.ylim(0, 30)
    plt.tight_layout()

    plt.show()

def generate_report(df):
    # Statistics
    total = len(df)
    avg_cgpa = df["cgpa"].mean()
    highest_cgpa = df["cgpa"].max()
    lowest_cgpa = df["cgpa"].min()
    total_backlogs = df["backlogs"].sum()
    top = df[df["cgpa"] == highest_cgpa].iloc[0]
    bottom = df[df["cgpa"] == lowest_cgpa].iloc[0]

    # Chart 1 - CGPA histogram
    plt.figure(figsize=(6,4))
    plt.hist(df["cgpa"], bins=5, color="skyblue", edgecolor="black")
    plt.title("CGPA Distribution")
    plt.tight_layout()
    plt.savefig("cgpa_chart.png", dpi=150, bbox_inches='tight')
    plt.close()

    # Chart 2 - Pie chart
    no_backlogs = len(df[df["backlogs"] == 0])
    with_backlogs = len(df[df["backlogs"] > 0])
    plt.figure(figsize=(5,5))
    plt.pie([no_backlogs, with_backlogs],
            labels=["No Backlogs", "Has Backlogs"],
            colors=["green", "red"],
            autopct="%1.1f%%")
    plt.title("Students with/without Backlogs")
    plt.savefig("pie_chart.png", dpi=150, bbox_inches='tight')
    plt.close()

    # Chart 3 - Top 10 students
    top_students = df.sort_values(by="cgpa", ascending=False).head(10)
    plt.figure(figsize=(8,5))
    plt.bar(top_students["name"], top_students["cgpa"], color="gold")
    plt.grid(axis='y', alpha=0.7)
    plt.xticks(rotation=45, ha='right')
    plt.title("Top 10 Students by CGPA")
    plt.tight_layout()
    plt.savefig("top_students.png", dpi=150, bbox_inches='tight')
    plt.close()

    # Chart 4 - Attendance per subject
    att_df = get_attendance_data()
    plt.figure(figsize=(12,6))
    plt.bar(att_df["subject_name"], att_df["attendance_pct"], color="steelblue")
    plt.axhline(y=75, color='red', linestyle='--', label='75% threshold')
    plt.xticks(rotation=45, ha='right')
    plt.title("Average Attendance % per Subject")
    plt.ylabel("Attendance %")
    plt.ylim(0, 100)
    plt.legend()
    plt.tight_layout()
    plt.savefig("attendance_chart.png", dpi=150, bbox_inches='tight')
    plt.close()

    # Chart 5 - IA marks per subject
    ia_df = get_ia_data()
    plt.figure(figsize=(12,6))
    plt.bar(ia_df["subject_name"], ia_df["avg_marks"], color="mediumpurple")
    plt.xticks(rotation=45, ha='right')
    plt.title("Average IA Marks per Subject")
    plt.ylabel("Average Marks (out of 30)")
    plt.ylim(0, 30)
    plt.tight_layout()
    plt.savefig("ia_chart.png", dpi=150, bbox_inches='tight')
    plt.close()

    # Build PDF
    pdf_path = r"C:\Users\Samarth\Documents\StudentManagementSystem\student_report.pdf"
    doc = SimpleDocTemplate(pdf_path, pagesize=A4)
    styles = getSampleStyleSheet()
    story = []

    # Title and date
    story.append(Paragraph("Student Management System - Analytics Report", styles["Title"]))
    story.append(Paragraph(f"Generated on: {datetime.date.today()}", styles["Normal"]))
    story.append(Spacer(1, 20))

    # Statistics
    story.append(Paragraph("Statistics", styles["Heading1"]))
    story.append(Paragraph(f"Total Students: {total}", styles["Normal"]))
    story.append(Paragraph(f"Average CGPA: {avg_cgpa:.2f}", styles["Normal"]))
    story.append(Paragraph(f"Highest CGPA: {highest_cgpa}", styles["Normal"]))
    story.append(Paragraph(f"Lowest CGPA: {lowest_cgpa}", styles["Normal"]))
    story.append(Paragraph(f"Total Backlogs: {int(total_backlogs)}", styles["Normal"]))
    story.append(Spacer(1, 20))

    # Top and bottom performer
    story.append(Paragraph("Top Performer", styles["Heading1"]))
    story.append(Paragraph(f"Name: {top['name']} | USN: {top['usn']} | CGPA: {top['cgpa']}", styles["Normal"]))
    story.append(Spacer(1, 10))
    story.append(Paragraph("Bottom Performer", styles["Heading1"]))
    story.append(Paragraph(f"Name: {bottom['name']} | USN: {bottom['usn']} | CGPA: {bottom['cgpa']}", styles["Normal"]))
    story.append(Spacer(1, 20))

    # Charts
    story.append(Paragraph("Charts", styles["Heading1"]))
    story.append(Image("cgpa_chart.png", width=400, height=250))
    story.append(Spacer(1, 20))
    story.append(Image("pie_chart.png", width=300, height=300))
    story.append(Spacer(1, 20))
    story.append(Image("top_students.png", width=400, height=250))
    story.append(Spacer(1, 20))

    # Attendance Analytics
    story.append(Paragraph("Attendance Analytics", styles["Heading1"]))
    story.append(Image("attendance_chart.png", width=450, height=270))
    story.append(Spacer(1, 20))

    # IA Marks Analytics
    story.append(Paragraph("IA Marks Analytics", styles["Heading1"]))
    story.append(Image("ia_chart.png", width=450, height=270))
    story.append(Spacer(1, 20))

    # At Risk Students
    at_risk_df = get_at_risk_data()
    story.append(Paragraph("Students at Risk (Attendance < 75%)", styles["Heading1"]))
    story.append(Paragraph(f"Total at risk students: {len(at_risk_df)}", styles["Normal"]))
    story.append(Spacer(1, 10))
    for _, row in at_risk_df.iterrows():
        story.append(Paragraph(f"{row['name']} ({row['usn']}) - {row['attendance_pct']}%", styles["Normal"]))
    story.append(Spacer(1, 20))

    doc.build(story)
    print("Report generated: student_report.pdf")

try:
    df = load_data()
    if len(sys.argv) > 1 and sys.argv[1] == "report":
        generate_report(df)
    else:
        show_charts(df)
except Exception as e:
    print("Error:", e)
