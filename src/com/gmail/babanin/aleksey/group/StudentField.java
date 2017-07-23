package com.gmail.babanin.aleksey.group;

import java.lang.reflect.Method;

public enum StudentField {
    NAME("Name", "getName"),
    SURNAME("Surname", "getSurname"),
    SEX("Sex", "getSex"),
    BIRTHDAT("Birthday", "getBirthday"),
    RECORD_BOOK("Record-book", "getRecordBook");

    private String field;
    private String method;

    private StudentField(String field, String method) {
        this.field = field;
        this.method = method;
    }

    public String toString() {
        return this.field;
    }

    public Method getMethod() throws FieldException {
        Method m = null;
        try {
            m = Student.class.getMethod(this.method);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new FieldException();
        }
        return m;
    }

    public Class<?> getRetClass() {
        try {
            return getMethod().getReturnType();
        } catch (FieldException e) {
            throw new FieldException();
        }
    }

}
