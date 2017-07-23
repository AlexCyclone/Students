package com.gmail.babanin.aleksey.human;

public enum Sex {
    M("Male"),
    F("Female");

    private String name;

    private Sex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
