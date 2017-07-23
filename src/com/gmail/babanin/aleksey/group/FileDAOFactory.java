package com.gmail.babanin.aleksey.group;

import java.io.*;

public class FileDAOFactory extends DAOFactory {
    private String path = null;

    public FileDAOFactory() {
        super();
    }

    @Override
    public void setParameters(String parameter) {
        File path = new File(parameter);
        if (path.exists() && !path.isDirectory()) {
            throw new IllegalArgumentException("Directory expected file found");
        }
        this.path = path.getAbsolutePath();
    }

    @Override
    public GroupDAO getGroupDAO() {
        if (path == null) {
            path = new File("").getAbsolutePath();
        }
        FileGroupDAO dao = new FileGroupDAO();
        dao.setPath(path);
        return dao;
    }

}
