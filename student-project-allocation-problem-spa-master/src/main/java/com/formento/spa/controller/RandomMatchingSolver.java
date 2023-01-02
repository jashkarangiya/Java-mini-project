package com.formento.spa.controller;

import com.formento.spa.model.Lecturer;
import com.formento.spa.model.Problem;
import com.formento.spa.model.Project;
import com.formento.spa.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jash on 13.10.2022
 */

/**
 * Random Solver ALgorithm
 */
public class RandomMatchingSolver extends Solver {

    List<Student> students = new ArrayList<Student>(problem.getStudents());
    List<Lecturer> lecturers = new ArrayList<Lecturer>(problem.getLecturers());
    List<Project> projects = new ArrayList<Project>(problem.getProjects());

    public RandomMatchingSolver(Problem problem) {
        super(problem);
    }

    boolean thereIsAFreeLecturer() {
        for (Lecturer lecturer : lecturers) {
            if (lecturer.isFree()) {
                return true;
            }
        }
        return false;
    }

    Lecturer getFreeLecturer() {
        for (Lecturer lecturer : lecturers) {
            if (lecturer.isFree()) {
                return lecturer;
            }
        }
        return null;
    }

    boolean thereIsAFreeProject() {
        for (Project project : projects) {
            if (!project.isFull() && !project.isOverSubscribed()) {
                return true;
            }
        }
        return false;
    }

    Project getFreeProject(Lecturer lecturer) {
        for (Project project : projects) {
            if (!project.isFull() && !project.isOverSubscribed() && project.getProjectLecturer().equals(lecturer)) {
                return project;
            }
        }
        return null;
    }

    List<Student> getProjectStudents(Project project) {
        List<Student> projectStudents = new ArrayList<Student>();
        for (Student student : students) {
            if (student.getProjectAssigned() == null) {
                List<Project> studentProjects = student.getPreferredProjects();
                for (Project p : studentProjects) {
                    if (project.equals(p)) {
                        projectStudents.add(student);
                    }
                }
            }

        }
        return projectStudents;
    }

    /**
     * Finds a random matching solution.
     * While there is a free lecturer, a free project, and a student that wants that project,
     * assign that project to the student.
     */
    public void execute() {
        while (thereIsAFreeLecturer() && thereIsAFreeProject()) {
            Lecturer freeLecturer = getFreeLecturer();
            Project freeProject = getFreeProject(freeLecturer);
            List<Student> projectStudents = getProjectStudents(freeProject);
            if (projectStudents.size() > 0) {
                Random rand = new Random();
                int randomStudentIndex = rand.nextInt(projectStudents.size());
                Student randomStudent = projectStudents.get(randomStudentIndex);
                if (randomStudent.getProjectAssigned() == null) {
                    randomStudent.setProjectAssigned(freeProject);
                    randomStudent.setProjectLecturer(freeLecturer);
                    freeProject.subscribeStudent(randomStudent);
                    freeLecturer.subscribeStudent(randomStudent);
                }
            } else {
                freeLecturer.subscribeStudent(null);
                freeProject.subscribeStudent(null);
            }
        }
        problem.printSolution("Random Matching");
        problem.printStudentSolutionAnalysis();
        problem.printLecturerSolutionAnalysis();
        problem.printMatchingSolutionAnalysis();
    }
}
