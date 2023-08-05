package com.midhun.task;

import java.io.Serializable;

public class FavModal implements Serializable {
    private String fname;
    private String ffullname;

    public FavModal() {

    }

    public FavModal(String fname, String ffullname) {
        this.fname = fname;
        this.ffullname = ffullname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFfullname() {
        return ffullname;
    }

    public void setFfullname(String ffullname) {
        this.ffullname = ffullname;
    }
}
