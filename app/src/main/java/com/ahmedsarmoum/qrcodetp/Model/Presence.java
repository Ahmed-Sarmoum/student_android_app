package com.ahmedsarmoum.qrcodetp.Model;

public class Presence {
    private int id;
    private int student_id;
    private String created_at;

    public Presence() {
    }

    public Presence(int id, int student_id, String created_at) {
        this.id = id;
        this.student_id = student_id;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
