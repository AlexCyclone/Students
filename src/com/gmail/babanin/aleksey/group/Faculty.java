package com.gmail.babanin.aleksey.group;

public enum Faculty {
    JAVA("Java"),
    ANDROID("Android"),
    CSHARP("C#"),
    FRONTEND("Front-End"),
    WEBDESIGN("Web-Design"),
    QA("QA");

    private String faculty;

    private Faculty(String faculty) {
        this.faculty = faculty;
    }

    public String getFaculty() {
        return this.faculty;
    }

}
