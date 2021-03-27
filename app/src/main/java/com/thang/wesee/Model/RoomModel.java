package com.thang.wesee.Model;

import java.io.Serializable;

public class RoomModel  implements Serializable {
    private int ID;
    private String name;
    private String date;

    public RoomModel(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public RoomModel() {
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public int getID() {
        return ID;
    }


}
