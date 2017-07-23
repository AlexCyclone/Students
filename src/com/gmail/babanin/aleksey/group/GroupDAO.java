package com.gmail.babanin.aleksey.group;

public interface GroupDAO {

    public Group loadGroup(String id);
    public String[] listAvalibleID();
    public boolean saveGroup(Group group);
    public boolean deleteGroup(String id);

}