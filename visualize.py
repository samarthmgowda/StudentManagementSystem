import pandas as pd 
import matplotlib.pyplot as plt 
from reportlab.lib.pagesizes import A4
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, Image
from reportlab.lib.styles import getSampleStyleSheet
import datetime
import sys
plt.style.use('ggplot')

def load_data():
    return pd.read_csv(r"C:\Users\Samarth\Documents\StudentManagementSystem\src\students.csv")

def show_charts(df):
    # CGPA Distribution histogram
    plt.figure(figsize=(6,4))
    plt.hist(df["CGPA"], bins=5, color="skyblue", edgecolor="black")
    plt.title("CGPA Distribution")
    plt.xlabel("CGPA Range")
    plt.ylabel("Number of Students")
    plt.tight_layout()

    # Top 10 students chart
    top_students = df.sort_values(by="CGPA", ascending=False).head(10)
    plt.figure(figsize=(8,5))
    plt.bar(top_students["Name"], top_students["CGPA"], color="gold")
    plt.legend(["CGPA"], loc="upper right")
    plt.xticks(rotation=45)
    plt.title("Top 10 Students by CGPA")
    plt.xlabel("Student")
    plt.ylabel("CGPA")
    plt.ylim(0,10)
    plt.tight_layout()

    # Pie chart
    no_backlogs = len(df[df["Backlogs"] == 0])
    with_backlogs = len(df[df["Backlogs"] > 0])
    plt.figure(figsize=(5,5))
    explode = (0.1, 0)
    plt.pie([no_backlogs, with_backlogs],
            labels=["No Backlogs", "Has Backlogs"],
            colors=["lightgreen", "salmon"],
            autopct="%1.1f%%",shadow=True,explode=explode)
    plt.title("Students with/without Backlogs")
    plt.legend()
    
    plt.show()

def generate_report(df):
    # Statistics
    total = len(df)
    avg_cgpa = df["CGPA"].mean()
    highest_cgpa = df["CGPA"].max()
    lowest_cgpa = df["CGPA"].min()
    total_backlogs = df["Backlogs"].sum()
    top = df[df["CGPA"] == highest_cgpa].iloc[0]
    bottom = df[df["CGPA"] == lowest_cgpa].iloc[0]

    # Save charts as images (for PDF)
    plt.figure(figsize=(6,4))
    plt.hist(df["CGPA"], bins=5, color="skyblue", edgecolor="black")
    plt.title("CGPA Distribution")
    plt.tight_layout()
    plt.savefig("cgpa_chart.png",dpi=150,bbox_inches='tight')
    plt.close()

    no_backlogs = len(df[df["Backlogs"] == 0])
    with_backlogs = len(df[df["Backlogs"] > 0])
    plt.figure(figsize=(5,5))
    plt.pie([no_backlogs, with_backlogs],
            labels=["No Backlogs", "Has Backlogs"],
            colors=["green", "red"],
            autopct="%1.1f%%")
    plt.title("Students with/without Backlogs")
    plt.savefig("pie_chart.png")
    plt.close()

    top_students = df.sort_values(by="CGPA", ascending=False).head(10)
    plt.figure(figsize=(8,5))
    plt.bar(top_students["Name"], top_students["CGPA"], color="gold")
    plt.grid(axis='y', alpha=0.7)
    plt.title("Top 10 Students by CGPA")
    plt.tight_layout()
    plt.savefig("top_students.png")
    plt.close()

    # Build PDF
    pdf_path = r"C:\Users\Samarth\Documents\StudentManagementSystem\student_report.pdf"
    doc = SimpleDocTemplate(pdf_path, pagesize=A4)
    styles = getSampleStyleSheet()
    story = []

    story.append(Paragraph("Student Management System - Analytics Report", styles["Title"]))
    story.append(Paragraph(f"Generated on: {datetime.date.today()}", styles["Normal"]))
    story.append(Spacer(1, 20))
    story.append(Paragraph("Statistics", styles["Heading1"]))
    story.append(Paragraph(f"Total Students: {total}", styles["Normal"]))
    story.append(Paragraph(f"Average CGPA: {avg_cgpa:.2f}", styles["Normal"]))
    story.append(Paragraph(f"Highest CGPA: {highest_cgpa}", styles["Normal"]))
    story.append(Paragraph(f"Lowest CGPA: {lowest_cgpa}", styles["Normal"]))
    story.append(Paragraph(f"Total Backlogs: {int(total_backlogs)}", styles["Normal"]))
    story.append(Spacer(1, 20))
    story.append(Paragraph("Top Performer", styles["Heading1"]))
    story.append(Paragraph(f"Name: {top['Name']} | USN: {top['USN']} | CGPA: {top['CGPA']}", styles["Normal"]))
    story.append(Spacer(1, 10))
    story.append(Paragraph("Bottom Performer", styles["Heading1"]))
    story.append(Paragraph(f"Name: {bottom['Name']} | USN: {bottom['USN']} | CGPA: {bottom['CGPA']}", styles["Normal"]))
    story.append(Spacer(1, 20))
    story.append(Paragraph("Charts", styles["Heading1"]))
    story.append(Image("cgpa_chart.png", width=400, height=250))
    story.append(Spacer(1, 20))
    story.append(Image("pie_chart.png", width=300, height=300))
    story.append(Spacer(1, 20))
    story.append(Image("top_students.png", width=400, height=250))

    doc.build(story)
    print("Report generated: student_report.pdf")

try:
    df = load_data()
    print("Arguments:", sys.argv)
    if len(sys.argv) > 1 and sys.argv[1] == "report":
        generate_report(df)
    else:
        show_charts(df)
except Exception as e:
    print("Error:", e)