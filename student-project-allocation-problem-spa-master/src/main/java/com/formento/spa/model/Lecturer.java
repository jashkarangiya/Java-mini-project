package com.formento.spa.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Goldi on 13.10.2022.
 */
public class Lecturer extends Person {
    /**
     * Maximum number of students that the lecturer can accept.
     */
    int lecturerCapacity;
    int noOfSubscribedStudents;

    /**
     * List of students preferred by this lecturer, in ascending order.
     */
    List<Student> preferredStudents;

    List<Student> subscribedStudents;

    /**
     * List of projects offered by this lecturer.
     */
    List<Project> availableProjects;

    public Lecturer(String email, String name, int ageInYears, int capacity) {
        super(email, name, ageInYears);
        this.noOfSubscribedStudents = 0;
        this.lecturerCapacity = capacity;
        this.preferredStudents = new ArrayList<Student>();
        this.subscribedStudents = new ArrayList<Student>();
        this.availableProjects = new ArrayList<Project>();
    }

    /**
     * Used to tell if a Lecturer hasn't reached his capacity yet.
     *
     * @return True if the lecturer can accept more students, otherwise False.
     */
    public boolean isFree() {
        return (lecturerCapacity - noOfSubscribedStudents) > 0;
    }

    public boolean isFull() {
        return (lecturerCapacity - noOfSubscribedStudents) == 0;
    }

    public boolean isOverSubscribed() {
        return (lecturerCapacity - noOfSubscribedStudents) < 0;
    }

    public void subscribeStudent(Student s) {
        this.subscribedStudents.add(s);
        this.noOfSubscribedStudents++;
    }

    /**
     * Method used in the Stable Matching Algorithm.
     * Removes projects that had been already assigned to a better student.
     *
     * @param s Student that has just been assigned a project.
     */
    public void deleteUnwantedStudents(Student s) {
        Iterator<Student> iterator = preferredStudents.iterator();
        for (; iterator.hasNext(); ) {
            Student currentStudent = iterator.next();
            if (currentStudent.equals(s)) {
                break;
            }
        }
        for (; iterator.hasNext(); ) {
            Student currentStudent = iterator.next();
            for (Project project : availableProjects) {
                currentStudent.removeProject(project);
            }
        }
    }

    public void unSubscribeStudent(Student s) {
        for (Iterator<Student> iterator = subscribedStudents.iterator(); iterator.hasNext(); ) {
            Student currentStudent = iterator.next();
            if (s.equals(currentStudent)) {
                iterator.remove();
            }
        }
        noOfSubscribedStudents--;
    }

    public int getHeaderLength(String header) {
        switch (header) {
            case "students":
                return toString().length();
            case "projects":
                return String.valueOf(availableProjectsToString()).length();
        }
        return 0;
    }

    public void deleteSuccessors(Student s, Project p) {
        boolean canDelete = false;
        for (Student student : preferredStudents) {
            if (canDelete) {
                student.removeProject(p);
            }
            if (student.equals(s)) {
                canDelete = true;
            }
        }
    }

    public Student getWorstStudent(Project p) {
        Student worstStudent = null;
        for (Student student : preferredStudents) {
            if (this.equals(student.projectLecturer) && p.equals(student.projectAssigned)) {
                worstStudent = student;
            }
        }
        return worstStudent;
    }

    public Student getWorstStudent() {
        Student worstStudent = null;
        for (Student student : preferredStudents) {
            if (this.equals(student.projectLecturer)) {
                worstStudent = student;
            }
        }
        return worstStudent;
    }

    /**
     * @param student Preferred studnet to be added in lecturer's list
     */
    public void addPreferredStudent(Student student) {
        this.preferredStudents.add(student);
    }

    /**
     * @param project Avaiable project to be added in lecturer's list
     */
    public void addAvailableProject(Project project) {
        this.availableProjects.add(project);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        sb.append(" : ");
        for (Student student : preferredStudents) {
            sb.append(student.getName());
            sb.append(" ");
        }
        return String.valueOf(sb);
    }

    public String availableProjectsToString() {
        StringBuilder sb = new StringBuilder(name);
        sb.append(" offers ");
        int i;
        for (i = 0; i < availableProjects.size() - 1; ++i) {
            sb.append(availableProjects.get(i).getProjectName());
            sb.append(", ");
        }
        sb.append(availableProjects.get(i).getProjectName());
        return String.valueOf(sb);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Lecturer)) {
            return false;
        }
        Lecturer lecturer = (Lecturer) obj;
        return (this.name.equals(lecturer.name));
    }
}
