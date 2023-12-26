package org.example;
import java.io.Serializable;

public class Student implements Serializable {
    String nume;
    Double medie;
    String cursOptional;

    public Student() {}

    public Student(String nume) {
        this.nume = nume;
    }

    String getNume() {
        return nume;
    }
    void setNume(String nume) {
        this.nume = nume;
    }
}

class StudentMaster extends Student implements Serializable {
    public StudentMaster(String nume) {
        this.setNume(nume);
    }
}

class StudentLicenta extends Student implements Serializable {
    public StudentLicenta(String nume) {
        this.setNume(nume);
    }
}