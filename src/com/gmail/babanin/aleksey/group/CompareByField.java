package com.gmail.babanin.aleksey.group;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public interface CompareByField {

    static int compare(Student a, Student b, StudentField field) throws FieldException {
        if (field == null) {
            return 0;
        }

        Method method = field.getMethod();

        if (field.getRetClass() == String.class) {
            return compareString(a, b, method);
        }
        if (field.getRetClass() == Date.class) {
            return compareDate(a, b, method);
        }
        if (field.getRetClass() == Integer.class) {
            return compareInteger(a, b, method);
        }
        if (field.getRetClass().isEnum()) {
            return compareEnum(a, b, method);
        }
        throw new FieldException();
    }

    static int compareString(Student a, Student b, Method method) {
        try {

            String aValue = (String) method.invoke(a);
            String bValue = (String) method.invoke(b);
            return aValue.compareTo(bValue);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new FieldException();
        }
    }

    static int compareDate(Student a, Student b, Method method) {
        try {
            Date aValue = (Date) method.invoke(a);
            Date bValue = (Date) method.invoke(b);
            return aValue.compareTo(bValue);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new FieldException();
        }
    }

    static int compareInteger(Student a, Student b, Method method) {
        try {
            Integer aValue = (Integer) method.invoke(a);
            Integer bValue = (Integer) method.invoke(b);
            return aValue.compareTo(bValue);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new FieldException();
        }
    }

    static int compareEnum(Student a, Student b, Method method) {
        try {
            if (method.invoke(a).hashCode() > method.invoke(b).hashCode()) {
                return -1;
            }
            if (method.invoke(a).hashCode() < method.invoke(b).hashCode()) {
                return 1;
            }
            return 0;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new FieldException();
        }
    }

}