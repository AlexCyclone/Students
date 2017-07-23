package com.gmail.babanin.aleksey.group;

public abstract class DAOFactory {
    public static final int FILE = 1;
    // public static final int SOMETHING_ELSE = 2;

    public abstract void setParameters(String parameter);
    public abstract GroupDAO getGroupDAO();

    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
        case FILE:
            return new FileDAOFactory();
        // case SOMETHING_ELSE:
        // return new SomethingElseDAOFactory();
        default:
            return null;
        }
    }
}
