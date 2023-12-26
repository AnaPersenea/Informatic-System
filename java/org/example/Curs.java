package org.example;

import java.util.ArrayList;
import java.util.List;

public class Curs<T extends Student> {
    private final String denumireCurs;
    private final int capacitateMaxima;
    List<Student> studentiInrolati;
    public Curs(String denumireCurs, int capacitateMaxima) {
        this.denumireCurs = denumireCurs;
        this.capacitateMaxima = capacitateMaxima;
        this.studentiInrolati = new ArrayList<>();
    }
    String getDenumireCurs() {
        return denumireCurs;
    }
    int getCapacitateMaxima() {
        return capacitateMaxima;
    }
    List<Student> getStudentiInrolati() {
        return studentiInrolati;
    }
}
