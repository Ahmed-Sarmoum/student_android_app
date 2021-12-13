package com.ahmedsarmoum.qrcodetp.Model;

public class Note {
    private int id;
    private int student_id;
    private double note_control;
    private double note_examen;
    private String observation;
    private  String created_at;

    public Note() {
    }

    public Note(int id, int student_id, double note_control, double note_examen, String observation, String created_at) {
        this.id = id;
        this.student_id = student_id;
        this.note_control = note_control;
        this.note_examen = note_examen;
        this.observation = observation;
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

    public double getNote_control() {
        return note_control;
    }

    public void setNote_control(double note_control) {
        this.note_control = note_control;
    }

    public double getNote_examen() {
        return note_examen;
    }

    public void setNote_examen(double note_examen) {
        this.note_examen = note_examen;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
