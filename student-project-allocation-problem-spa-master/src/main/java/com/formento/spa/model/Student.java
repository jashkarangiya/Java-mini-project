package com.formento.spa.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Goldi on 13.10.2022.
 */
public class Student extends Person {
    /**
     * List of projects preferred by this student, in ascending order.
     */
    List<Project> preferredProjects;
    List<Project> preferredProjectsCopy;
    Project projectAssigned;
    Lecturer projectLecturer;

    public Student(String email, String name, int ageInYears) {
        super(email, name, ageInYears);
        this.preferredProjects = new ArrayList<Project>();

    }

    public List<Project> getPreferredProjects() {
        return preferredProjects;
    }

    /**
     * Used to tell if a Student doesn't have a project assigned yet
     */
    public boolean isFree() {
        return projectAssigned == null;
    }

    public int getHeaderLength(String header) {
        return toString().length();
    }

    /**
     * @param preferredProject Variable number of Student's preferred projects
     */
    public void addPreferredProject(Project preferredProject) {
        this.preferredProjects.add(preferredProject);
    }

    public void setPreferredProjectsCopy() {
        this.preferredProjectsCopy = new ArrayList<>(preferredProjects);
    }

    public Project getFirstProject() {
        assert preferredProjects.size() > 0 : "Student has no remaining acceptable projects!";
        return preferredProjects.get(0);
    }

    public Project getProjectAssigned() {
        return projectAssigned;
    }

    public void setProjectAssigned(Project projectAssigned) {
        this.projectAssigned = projectAssigned;
    }

    public void setProjectLecturer(Lecturer projectLecturer) {
        this.projectLecturer = projectLecturer;
    }

    public void removeProject(Project p) {
        for (Iterator<Project> iterator = preferredProjects.iterator(); iterator.hasNext(); ) {
            Project currentProject = iterator.next();
            if (p.equals(currentProject)) {
                iterator.remove();
            }
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Student)) {
            return false;
        }
        Student student = (Student) obj;
        return (this.name.equals(student.name));
    }

    public void printProjectAssigned() {
        if (projectAssigned != null) {
            System.out.println(this.name + " : " + projectAssigned.getProjectName());
        } else {
            System.out.println(this.name + " : No project.");
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        sb.append(" : ");
        for (Project project : preferredProjects) {
            sb.append(project.getProjectName());
            sb.append(" ");
        }
        return String.valueOf(sb);
    }
}
