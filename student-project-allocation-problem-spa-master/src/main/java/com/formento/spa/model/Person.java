package com.formento.spa.model;

/**
 * Created by Jash on 27.02.2016.
 */

/**
 * Abstract class used to extend classes Student and Lecturer.
 */
public abstract class Person {
    protected String email;
    protected String name;
    protected int ageInYears;

    public Person(String email, String name, int ageInYears) {
        this.email = email;
        this.name = name;
        this.ageInYears = ageInYears;
    }

    public abstract boolean isFree();

    public abstract int getHeaderLength(String header);

    public String getName() {
        return name;
    }
}
