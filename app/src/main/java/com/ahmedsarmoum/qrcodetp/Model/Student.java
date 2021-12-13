package com.ahmedsarmoum.qrcodetp.Model;

public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private int sexe;
    private String adress;
    private int age;
    private String created_at;

    public Student() {
    }

    public Student(int id, String firstName, String lastName, int sexe, String adress, int age, String created_at) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sexe = sexe;
        this.adress = adress;
        this.age = age;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getSexe() {
        return sexe;
    }

    public void setSexe(int sexe) {
        this.sexe = sexe;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
