package com.midhun.task;

import java.io.Serializable;

public class CryptoModal  implements Serializable {
    private String name;
    private String fullname;

    public CryptoModal() {

    }

    public CryptoModal(String name, String fullname) {
        this.name = name;
        this.fullname = fullname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
