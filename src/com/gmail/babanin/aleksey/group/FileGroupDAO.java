package com.gmail.babanin.aleksey.group;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileGroupDAO implements GroupDAO {

    private File path;

    protected void setPath(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        } else {
            if (!f.isDirectory()) {
                throw new IllegalArgumentException("Directory expected file found");
            }
        }

        this.path = f;
    }

    @Override
    public Group loadGroup(String id) {
        Group group = null;

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(path.getAbsolutePath() + File.separatorChar + id + ".stdg"))) {
            group = (Group) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return group;
    }

    @Override
    public String[] listAvalibleID() {
        File[] found = path
                .listFiles((pathname) -> pathname.getName().lastIndexOf(".stdg") == pathname.getName().length() - 5
                        ? true : false);
        String[] list = new String[found.length];

        for (int i = 0; i < list.length; i++) {
            list[i] = found[i].getName().substring(0, found[i].getName().lastIndexOf(".stdg"));
        }
        return list;
    }

    @Override
    public boolean saveGroup(Group group) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(path.getAbsolutePath() + File.separatorChar + group.getID() + ".stdg"))) {
            oos.writeObject(group);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteGroup(String id) {
        File file = new File(path.getAbsolutePath() + File.separatorChar + id + ".stdg");
        return file.delete();
    }

}
