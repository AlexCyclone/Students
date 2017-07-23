package com.gmail.babanin.aleksey.ui;

import java.util.Arrays;

public class Menu {
    private String head;
    private String[] elements = new String[0];

    public Menu() {
        super();
    }

    public Menu(String head) {
        super();
        this.head = head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public boolean addElement(String element) {
        if (element == null || element.trim().equals("")) {
            return false;
        }
        elements = Arrays.copyOf(elements, elements.length + 1);
        elements[elements.length - 1] = element;
        return true;
    }

    public int getLength() {
        return elements.length;
    }

    @Override
    public String toString() {
        StringBuilder menu = new StringBuilder();
        menu.append(head).append(System.lineSeparator()).append(System.lineSeparator());
        int i = 1;
        for (String element : elements) {
            menu.append(i).append(". ").append(element).append(System.lineSeparator());
            i += 1;
        }
        menu.append("q").append(". ").append("Quit");
        return menu.toString();
    }

}
