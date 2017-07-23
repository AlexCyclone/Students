package com.gmail.babanin.aleksey.group;

import java.io.Serializable;
import java.util.Date;

import com.gmail.babanin.aleksey.human.Human;
import com.gmail.babanin.aleksey.human.Sex;

public class Student extends Human implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer recordBook;

    public Student() {
        super();
    }

    public Student(String name, String surname, Sex sex, Date birthday, Integer recordBook) {
        super(name, surname, sex, birthday);
        setRecordBook(recordBook);
    }

    public Integer getRecordBook() {
        return recordBook;
    }

    public void setRecordBook(Integer recordBook) {
        this.recordBook = validateRecordBook(recordBook);
    }
    
    private Integer validateRecordBook(Integer recordBook) {
        if (recordBook < 0) {
            throw new IllegalArgumentException();
        }
        return recordBook;
    }

    @Override
    public String toString() {
        return "Record-book: " + recordBook + ", " + super.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((recordBook == null) ? 0 : recordBook.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        if (recordBook == null) {
            if (other.recordBook != null)
                return false;
        } else if (!recordBook.equals(other.recordBook))
            return false;
        return true;
    }

}
