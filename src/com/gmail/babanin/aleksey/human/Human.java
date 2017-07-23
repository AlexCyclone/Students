package com.gmail.babanin.aleksey.human;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Human implements Serializable {
    private static final long serialVersionUID = 1L;
    private String surname;
    private String name;
    private Sex sex;
    private Date birthday;

    public Human() {
        super();
    }

    public Human(String name, String surname, Sex sex, Date birthday) {
        super();
        this.surname = validateString(surname);
        this.name = validateString(name);
        this.sex = sex;
        this.birthday = birthday;
    }

    private final String validateString(String s) {
        if ((s == null) || (s.trim().isEmpty())) {
            throw new IllegalArgumentException("Invalid input");
        }

        s = s.trim();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((Character.isAlphabetic(c) != true) && (c != '-') && (c != '\'') && (c != ' ') && (c != '.')) {
                throw new IllegalArgumentException("Invalid input");
            }
        }
        return s;
    }

    public void setSurname(String surname) {
        this.surname = validateString(surname);
    }

    public void setName(String name) {
        this.name = validateString(name);
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public Sex getSex() {
        return sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return name + " " + surname + ", sex: " + sex.getName() +  ", birthday: " + sdf.format(birthday);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((birthday == null) ? 0 : birthday.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((sex == null) ? 0 : sex.hashCode());
        result = prime * result + ((surname == null) ? 0 : surname.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Human other = (Human) obj;
        if (birthday == null) {
            if (other.birthday != null)
                return false;
        } else if (!birthday.equals(other.birthday))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (sex != other.sex)
            return false;
        if (surname == null) {
            if (other.surname != null)
                return false;
        } else if (!surname.equals(other.surname))
            return false;
        return true;
    }

}
