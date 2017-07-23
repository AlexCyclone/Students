package com.gmail.babanin.aleksey.ui;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.gmail.babanin.aleksey.group.DAOFactory;
import com.gmail.babanin.aleksey.group.Faculty;
import com.gmail.babanin.aleksey.group.FieldException;
import com.gmail.babanin.aleksey.group.Group;
import com.gmail.babanin.aleksey.group.GroupDAO;
import com.gmail.babanin.aleksey.group.LimitGroupException;
import com.gmail.babanin.aleksey.group.Student;
import com.gmail.babanin.aleksey.group.StudentField;
import com.gmail.babanin.aleksey.human.Sex;

public class GroupUI {

    private Scanner sc = new Scanner(System.in);
    private DAOFactory fileFactory;
    private GroupDAO groupDAO;
    private File dbLocation = new File("");
    private String[] groupList;
    private Menu groupMenu;
    private Menu mainMenu;
    private Group group = null;

    public GroupUI() {
        super();
        generateDefaultParameters();
        showMainMenu();
    }

    private void generateDefaultParameters() {
        fileFactory = DAOFactory.getDAOFactory(DAOFactory.FILE);
        fileFactory.setParameters(dbLocation.getAbsolutePath());
        groupDAO = fileFactory.getGroupDAO();
        groupList = groupDAO.listAvalibleID();
    }

    // Implements Main menu methods

    private void showMainMenu() {
        mainMenu = new Menu();
        this.fillMainMenu();
        String parameter = "";

        while (!parameter.equalsIgnoreCase("q")) {
            System.out.println(mainMenu + System.lineSeparator());
            System.out.print("Select an action: ");
            parameter = sc.nextLine();
            System.out.println();
            switchMainSelector(parameter);
        }
    }

    private void fillMainMenu() {
        this.mainMenu.setHead("Group editor" + System.lineSeparator() + "Current database location: \""
                + dbLocation.getAbsolutePath() + "\"");
        mainMenu.addElement("New group");
        mainMenu.addElement("Load group");
        mainMenu.addElement("List avalible");
        mainMenu.addElement("Remove group");
        mainMenu.addElement("Change database location");
    }

    private void switchMainSelector(String parameter) {
        switch (parameter) {
        case "q":
            break;
        case "1":
            newGroup();
            break;
        case "2":
            loadGroup();
            break;
        case "3":
            listGroups();
            break;
        case "4":
            removeGroup();
            break;
        case "5":
            changeDB();
            break;
        default:
            invalidInput();
            break;
        }
        System.out.println(System.lineSeparator());
    }

    private void newGroup() {
        group = new Group();

        try {
            System.out.print("Input group number: ");
            group.setNumber(toInteger(sc.nextLine()));
            group.setFaculty(facultyRequest());
            System.out.print("Input course: ");
            group.setCourse(toInteger(sc.nextLine()));
            System.out.print("Input date start study (format \"dd.MM.yyyy\"): ");
            group.setStartStudy(toDate(sc.nextLine()));
        } catch (IllegalArgumentException e) {
            group = null;
            invalidInput();
            return;
        }

        if (duplicateGroup(group)) {
            group = null;
            System.out.println("A group with the specified parameters already exists");
            return;
        }

        System.out.println();
        showGroupMenu();
    }

    private boolean duplicateGroup(Group group) {
        String id = group.getID();
        for (String list : groupList) {
            if (id.equals(list)) {
                return true;
            }
        }
        return false;
    }

    private void loadGroup() {
        System.out.print("Input a GroupID to load: ");
        String id = sc.nextLine();
        group = groupDAO.loadGroup(id);
        if (group == null) {
            System.out.println("Load failed");
        } else {
            System.out.println("Load complete");

            System.out.println();
            showGroupMenu();
        }
    }

    private void listGroups() {
        groupList = groupDAO.listAvalibleID();
        if (groupList.length == 0) {
            System.out.println("There are no groups in the current location");
            return;
        }
        System.out.println("Found group with newt ID:");
        for (String groupID : groupList) {
            System.out.println(groupID);
        }
    }

    private void removeGroup() {
        System.out.print("Input a GroupID to delete: ");
        String id = sc.nextLine();
        System.out.println("Will delete group with ID: \"" + id + "\"");
        if (confirm()) {
            if (groupDAO.deleteGroup(id)) {
                System.out.println("Group delete");
            } else {
                System.out.println("Error deleting");
            }

        } else {
            System.out.println("Cancelled");
        }

    }

    private void changeDB() {
        System.out.println("Input new location of database:");
        File location = new File(sc.nextLine());
        if (!location.exists()) {
            if (!location.mkdirs()) {
                System.out.println("Invalid location.");
                return;
            }
        } else {
            if (!location.isDirectory()) {
                System.out.println("Invalid location. File found directory expected.");
                return;
            }
        }
        dbLocation = location;
        fileFactory.setParameters(dbLocation.getAbsolutePath());
        groupDAO = fileFactory.getGroupDAO();
        groupList = groupDAO.listAvalibleID();

        System.out.println("Location accepted");
        this.mainMenu.setHead("Group editor " + System.lineSeparator() + "Current database location: \""
                + dbLocation.getAbsolutePath() + "\"");
    }

    // Implements Group menu methods

    private void showGroupMenu() {
        groupMenu = new Menu();
        this.fillGroupMenu();
        String parameter = "";

        while (!parameter.equalsIgnoreCase("q")) {
            System.out.println(groupMenu + System.lineSeparator());
            System.out.print("Select an action: ");
            parameter = sc.nextLine();
            System.out.println();
            switchGroupSelector(parameter);
        }
    }

    private void fillGroupMenu() {
        this.groupMenu.setHead(
                "Group editor (group edit mode)" + System.lineSeparator() + "Current group: \"" + group + "\"");
        groupMenu.addElement("Add student");
        groupMenu.addElement("Remove student");
        groupMenu.addElement("List students");
        groupMenu.addElement("Order students");
        groupMenu.addElement("Find by parameter");
        groupMenu.addElement("Save");
    }

    private void switchGroupSelector(String parameter) {

        switch (parameter) {
        case "q":
            break;
        case "1":
            addStudent();
            break;
        case "2":
            removeStudent();
            break;
        case "3":
            listStudents();
            break;
        case "4":
            orederGroup();
            break;
        case "5":
            findStudents();
            break;
        case "6":
            saveGroup();
            break;
        default:
            invalidInput();
        }
        System.out.println(System.lineSeparator());
    }

    private void addStudent() {
        Student student;
        student = createStudent();
        if (student == null) {
            return;
        }
        System.out.println();

        if (confirm(student, "add")) {
            try {
                group.addStudent(student);
            } catch (LimitGroupException | IllegalArgumentException e) {
                System.out.println("New student wasn't added");
                System.out.println("Group have maximum size");
                return;
            }
        }
        System.out.println("Complete");
    }

    private Student createStudent() {
        Student student = new Student();
        try {
            System.out.print("Input Record-book number: ");
            student.setRecordBook(toInteger(sc.nextLine()));
            System.out.print("Input name: ");
            student.setName(sc.nextLine());
            System.out.print("Input surname: ");
            student.setSurname(sc.nextLine());
            System.out.print("Input sex (M/F): ");
            student.setSex(toSex(sc.nextLine()));
            System.out.print("Input birthday date (format \"dd.MM.yyyy\"): ");
            student.setBirthday(toDate(sc.nextLine()));

        } catch (IllegalArgumentException e) {
            invalidInput();
            return null;
        }
        return student;
    }

    private void removeStudent() {
        System.out.print("Input number of student record-book for remove: ");
        Integer recordBook;
        Student[] found;
        try {
            recordBook = toInteger(sc.nextLine());
            found = group.findByField(StudentField.RECORD_BOOK, (Object) recordBook);

            if (found.length != 1) {
                System.out.println("Not found");
                return;
            }
        } catch (IllegalArgumentException | FieldException e) {
            invalidInput();
            return;
        }

        if (confirm(found[0], "remove")) {
            if (group.removeStudent(found[0]) > 0) {
                System.out.println("Complete");
            } else {
                System.out.println("Failed");
            }
        }
    }

    private void listStudents() {
        System.out.println(group.list());
        System.out.println("Press Enter to continue");
        sc.nextLine();
    }

    private void orederGroup() {
        Menu subMenu = new Menu("Field to order:");
        StudentField[] fields = StudentField.values();

        for (StudentField field : fields) {
            subMenu.addElement(field.toString());
        }
        System.out.println(subMenu);

        try {
            System.out.print("Input field number: ");
            int f = Integer.parseInt(sc.nextLine());
            group.sortByField(fields[f - 1]);
        } catch (NumberFormatException | FieldException | ArrayIndexOutOfBoundsException e) {
            invalidInput();
            return;
        }
        listStudents();
    }

    private void findStudents() {
        Student[] found = new Student[0];
        try {
            StudentField field = studentsFieldRequest();
            Object value = valueRequest(field);
            found = group.findByField(field, value);
        } catch (FieldException e) {
            invalidInput();
            return;
        }
        printStudentsArray(found);
        System.out.println("Press Enter to continue");
        sc.nextLine();
    }

    private void saveGroup() {
        if (groupDAO.saveGroup(group)) {
            System.out.println("Save");
        } else {
            System.out.println("Don't save");
        }
    }

    // Auxiliary methods

    private void invalidInput() {
        System.out.println("Invalid input. Try again.");
    }

    private boolean confirm() {
        System.out.println("Confirm (Y/N)");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            return true;
        }
        return false;
    }

    private boolean confirm(Student student, String action) {
        System.out.println("Will " + action + " student:");
        System.out.println(student);
        System.out.print("Confirm action (Y/N): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            return true;
        }
        return false;
    }

    private void printStudentsArray(Student[] students) {
        if (students == null || students.length == 0) {
            System.out.println("Empty");
        }

        for (Student student : students) {
            System.out.println(student);
        }
    }

    // Transform methods

    private Integer toInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
    }

    private Date toDate(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return sdf.parse(s);
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
    }

    private Faculty facultyRequest() {
        Menu subMenu = new Menu("Choise Faculty");
        Faculty[] faculties = Faculty.values();

        for (Faculty faculty : faculties) {
            subMenu.addElement(faculty.getFaculty());
        }

        System.out.println(subMenu);
        System.out.print("Input number faculty: ");
        int f = toInteger(sc.nextLine());
        return faculties[f - 1];
    }

    private Sex toSex(String s) throws IllegalArgumentException {
        if (s.equalsIgnoreCase("m")) {
            return Sex.M;
        } else if (s.equalsIgnoreCase("f")) {
            return Sex.F;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private StudentField studentsFieldRequest() {
        Menu subMenu = new Menu("Choise parameter");
        StudentField[] fields = StudentField.values();

        for (StudentField field : fields) {
            subMenu.addElement(field.toString());
        }

        System.out.println(subMenu);
        System.out.print("Input number of field: ");
        try {
            int f = toInteger(sc.nextLine());
            return fields[f - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new FieldException();
        }
    }
    
    private Object valueRequest(StudentField field) throws FieldException {
        Object value = null;
        System.out.print("Input " + field.toString() + ": ");
        if (field.getRetClass() == Integer.class) {
            value = toInteger(sc.nextLine());
        }
        if (field.getRetClass() == String.class) {
            value = sc.nextLine();
        }
        if (field.getRetClass() == Date.class) {
            value = toDate(sc.nextLine());
        }
        if (field.getRetClass() == Sex.class) {
            value = toSex(sc.nextLine());
        }
        if (value == null) {
            throw new FieldException();
        }
        return value;
    }
}
