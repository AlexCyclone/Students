package com.gmail.babanin.aleksey.group;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.gmail.babanin.aleksey.human.Sex;

public class Group implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int maxGroupSize = 10;
    private Student[] students = new Student[maxGroupSize];
    private Integer groupSize = 0;
    private final int maxCourse = 5;

    private Integer number;
    private Faculty faculty;
    private Integer course;
    private Date startStudy;

    public Group() {
        super();
    }

    public Group(Integer number, Faculty faculty, Integer course, Date startStudy) {
        super();
        setNumber(number);
        this.faculty = faculty;
        setCourse(course);
        this.startStudy = startStudy;
    }

    public Integer getNumber() {
        return number;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public Integer getCourse() {
        return course;
    }

    public Date getStartStudy() {
        return startStudy;
    }

    public String getID() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return "" + getNumber() + "-" + getFaculty() + "-" + getCourse() + "-" + sdf.format(getStartStudy());
    }

    public void setNumber(Integer number) {
        this.number = validateNumber(number);
    }

    private final Integer validateNumber(Integer number) throws IllegalArgumentException {
        if (number < 0) {
            throw new IllegalArgumentException();
        }
        return number;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public void setCourse(Integer course) {
        this.course = validateCourse(course);
    }

    private final Integer validateCourse(Integer course) throws IllegalArgumentException {
        if ((course < 1) || (course > this.maxCourse)) {
            throw new IllegalArgumentException();
        }
        return course;
    }

    public void setStartStudy(Date startStudy) {
        this.startStudy = startStudy;
    }

    public void addStudent(Student student) throws LimitGroupException, IllegalArgumentException {
        if (groupSize == 10) {
            throw new LimitGroupException();
        }

        if (checkDuplicate(student) >= 0) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < this.students.length; i++) {
            if (this.students[i] == null) {
                this.students[i] = student;
                groupSize += 1;
                return;
            }
        }
    }

    private int checkDuplicate(Student student) {
        if (groupSize == 0) {
            return -1;
        }

        for (int i = 0; i < this.students.length; i++) {
            if (this.students[i] != null && (this.students[i].getRecordBook().equals(student.getRecordBook()))) {
                return i;
            }
        }
        return -1;
    }

    public int removeStudent(Student student) {
        for (int i = 0; i < this.students.length; i++) {
            if (this.students[i] != null && this.students[i].equals(student)) {
                this.students[i] = null;
                groupSize -= 1;
                return i;
            }
        }
        return -1;
    }

    public Student[] findByField(StudentField field, Object value) throws FieldException {
        Class<?> valueClass = value.getClass();
        if (field.getRetClass() == Sex.class) {
            return findBySex((Sex) value);
        }
        if (field.getRetClass() == Integer.class) {
            return findByRecordBook((Integer) value);
        }
        if (valueClass != field.getRetClass()) {
            throw new FieldException("Invalid field or value");
        }
        return findBySpecifiedField(field, value);
    }

    public Student[] findBySpecifiedField(StudentField field, Object value) throws FieldException {
        Student[] found = new Student[0];
        try {
            Method method = field.getMethod();
            Object fieldValue = field.getRetClass().newInstance();

            for (int i = 0; i < students.length; i++) {
                if (students[i] == null) {
                    continue;
                }
                Object[] args = new Object[0];
                fieldValue = method.invoke(students[i], args);
                if (fieldValue.equals(value)) {
                    found = Arrays.copyOf(found, found.length + 1);
                    found[found.length - 1] = students[i];
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new FieldException();
        }
        return found;
    }

    public Student[] findByRecordBook(Integer recourdBook) {
        Student[] found = new Student[0];

        for (int i = 0; i < students.length; i++) {
            if (students[i] == null) {
                continue;
            }

            if (students[i].getRecordBook().equals(recourdBook)) {
                found = Arrays.copyOf(found, found.length + 1);
                found[found.length - 1] = students[i];
            }
        }
        return found;
    }

    private Student[] findBySex(Sex sex) {
        Student[] found = new Student[0];

        for (int i = 0; i < students.length; i++) {
            if (students[i] == null) {
                continue;
            }

            if (students[i].getSex() == sex) {
                found = Arrays.copyOf(found, found.length + 1);
                found[found.length - 1] = students[i];
            }
        }
        return found;
    }

    public void sortByField(StudentField field) throws FieldException {
        if (groupSize == 0) {
            return;
        }

        Arrays.sort(students, (a, b) -> CompareNull.compare(a, b) != CompareNull.NO_NULL ? -CompareNull.compare(a, b)
                : CompareByField.compare(a, b, field));
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("Group: ").append(number);
        sb.append(", faculty: ").append(faculty.getFaculty());
        sb.append(", course: ").append(course);
        sb.append(", start study: ").append(sdf.format(startStudy));
        return sb.toString();
    }

    public String list() {
        if (groupSize == 0) {
            return "";
        }

        sortByField(null);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.students.length; i++) {
            if (students[i] == null) {
                break;
            }
            sb.append(i + 1).append(") ").append(students[i]);
            sb.append(System.lineSeparator());
        }

        sb.delete(sb.lastIndexOf(System.lineSeparator()), sb.length());
        return sb.toString();
    }

}