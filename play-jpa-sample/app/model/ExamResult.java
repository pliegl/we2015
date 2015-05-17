package model;

import javax.persistence.*;
import java.util.Date;

/**
 * Examresult domain object
 */
@Entity
public class ExamResult extends BaseEntity {

    @Column(name = "pruefungsDatum")
    @Temporal(TemporalType.DATE)
    private Date examDate;

    private String exam;

    private int mark;

    @ManyToOne
    private Student student;


    @Transient
    private String examLocation;

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public int getMark() {
        return mark;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getExamLocation() {
        return examLocation;
    }

    public void setExamLocation(String examLocation) {
        this.examLocation = examLocation;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }
}
