package com.formento.spa.model;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Keval on 13.10.2022.
 */

/**
 * Models Students,Lecturers and projects all together.
 */
public class Problem {
    List<Student> students;
    List<Lecturer> lecturers;
    List<Project> projects;
    int[] projectsAnalysis;
    int lecturerSatisfactions[];

    public Problem() {
        initialize();
    }

    public void initialize() {
        students = new ArrayList<Student>();
        lecturers = new ArrayList<Lecturer>();
        projects = new ArrayList<Project>();
        setStudentsData();
        setLecturersData();
        setProjectsData();
        setStudentPreferences();
        setLecturerPreferences();
        setAvailableProjects();
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Lecturer> getLecturers() {
        return lecturers;
    }

    public List<Project> getProjects() {
        return projects;
    }

    Project getProjectByName(String projectName) {
        for (Project project : projects) {
            if (project.getProjectName().equals(projectName)) {
                return project;
            }
        }
        return null;
    }

    Student getStudentByName(String studentName) {
        for (Student student : students) {
            if (student.getName().equals(studentName)) {
                return student;
            }
        }
        return null;
    }

    int getStudentIndex(String studentName) {
        for (int i = 0; i < students.size(); ++i) {
            if (students.get(i).getName().equals(studentName)) {
                return i;
            }
        }
        return -1;
    }

    public boolean thereIsAFreeStudentWithNoEmptyList() {
        for (Student student : students) {
            if (student.isFree() && student.preferredProjects.size() > 0) {
                return true;
            }
        }
        return false;
    }

    public Student getFreeStudentWithNoEmptyList() {
        for (Student student : students) {
            if (student.isFree() && student.preferredProjects.size() > 0) {
                return student;
            }
        }
        return null;
    }

    public Lecturer getProjectLecturer(Project p) {
        return p.projectLecturer;
    }

    public void setStudentsData() {
        String studentName;
        String studentEmail;
        int studentAge;
        try {
            Scanner sc = new Scanner(new File("InputFiles/students.in"));
            while (sc.hasNextLine()) {
                studentName = sc.next();
                studentEmail = sc.next();
                studentAge = sc.nextInt();
                Student student = new Student(studentEmail, studentName, studentAge);
                this.students.add(student);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setProjectsData() {
        String projectName;
        int projectCapacity;
        try {
            Scanner sc = new Scanner(new File("InputFiles/projects.in"));
            while (sc.hasNextLine()) {
                projectName = sc.next();
                projectCapacity = sc.nextInt();
                Project project = new Project(projectName, projectCapacity);
                this.projects.add(project);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setLecturersData() {
        String lecturerName;
        String lecturerEmail;
        int lecturerAge;
        int lecturerCapacity;
        try {
            Scanner sc = new Scanner(new File("InputFiles/lecturers.in"));
            while (sc.hasNextLine()) {
                lecturerName = sc.next();
                lecturerEmail = sc.next();
                lecturerAge = sc.nextInt();
                lecturerCapacity = sc.nextInt();
                Lecturer lecturer = new Lecturer(lecturerEmail, lecturerName, lecturerAge, lecturerCapacity);
                this.lecturers.add(lecturer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setStudentPreferences() {
        String line;
        try {
            Scanner sc = new Scanner(new File("InputFiles/studentPreferences.in"));
            int currentStudentIndex = 0;
            Student currentStudent;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                currentStudent = students.get(currentStudentIndex);
                String preferences[] = line.split("\\s+");
                for (String projectName : preferences) {
                    Project preferredProject = getProjectByName(projectName);
                    currentStudent.addPreferredProject(preferredProject);
                }
                currentStudent.setPreferredProjectsCopy();
                currentStudentIndex++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setLecturerPreferences() {
        String line;
        int currentLecturerIndex = 0;
        Lecturer currentLecturer;
        try {
            Scanner sc = new Scanner(new File("InputFiles/lecturerPreferences.in"));
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                currentLecturer = lecturers.get(currentLecturerIndex);
                String preferences[] = line.split("\\s+");
                for (String studentName : preferences) {
                    Student preferredStudent = getStudentByName(studentName);
                    currentLecturer.addPreferredStudent(preferredStudent);
                }
                currentLecturerIndex++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setAvailableProjects() {
        String line;
        int currentLecturerIndex = 0;
        Lecturer currentLecturer;
        try {
            Scanner sc = new Scanner(new File("InputFiles/availableProjects.in"));
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                currentLecturer = lecturers.get(currentLecturerIndex);
                String availableProjects[] = line.split("\\s+");
                for (String projectName : availableProjects) {
                    Project availableProject = getProjectByName(projectName);
                    availableProject.setLecturer(currentLecturer);
                    currentLecturer.addAvailableProject(availableProject);
                }
                currentLecturerIndex++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to find the table row size, depending on the column.
     *
     * @param persons   List of lecturers or students.
     * @param maxLength Current header length.
     * @param header    Column used to find the row size.
     * @return maximum length of the header.
     */
    public int getMaxHeaderLength(List<? extends Person> persons, int maxLength, String header) {
        for (Person person : persons) {
            if (person.getHeaderLength(header) > maxLength) {
                maxLength = person.getHeaderLength(header);
            }
        }
        return maxLength;
    }

    /**
     * Adds spaces to the header.
     *
     * @param header
     * @param maxLength
     * @return the header padded with spaces.
     */
    String getPaddedHeader(String header, int maxLength) {
        int spacesRequired = maxLength - header.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spacesRequired / 2; ++i) {
            sb.append(" ");
        }
        sb.append(header);
        for (int i = spacesRequired / 2; i < spacesRequired; ++i) {
            sb.append(" ");
        }
        return String.valueOf(sb);
    }

    String getTableRow(String row, int maxLength) {
        StringBuilder paddedRow = new StringBuilder();
        if (row.length() == 0) {
            for (int i = 0; i < maxLength; ++i) {
                paddedRow.append(" ");
            }
        } else {
            paddedRow.append(row);
            for (int i = paddedRow.length(); i < maxLength; ++i) {
                paddedRow.append(" ");
            }
        }
        return String.valueOf(paddedRow);
    }

    public void printSolution(String matching) {
        System.out.println("\n" + matching + " solution\n--------------------------------------");
        for (Student student : students) {
            student.printProjectAssigned();
        }
    }

    public void printStudentSolutionAnalysis() {
        System.out.println("\nStudents solution analysis");
        projectsAnalysis = new int[students.size()];
        int satisfaction;
        for (int i = 0; i < students.size(); ++i) {
            satisfaction = 0;
            List<Project> studProjects = students.get(i).preferredProjectsCopy;
            for (int j = 0; j < studProjects.size(); ++j) {
                if (studProjects.get(j).equals(students.get(i).projectAssigned)) {
                    satisfaction = (int) ((double) (studProjects.size() - j) / studProjects.size() * 100);
                    break;
                }
            }
            projectsAnalysis[i] = satisfaction;
        }
        int sum = 0;
        for (int i = 0; i < projectsAnalysis.length; ++i) {
            String name = students.get(i).getName();
            System.out.println(name + " : " + projectsAnalysis[i] + "% pleased.");
            sum += projectsAnalysis[i];
        }
        sum /= projectsAnalysis.length;
        System.out.println("Overall : " + sum + "% pleased.");
    }

    public void printLecturerSolutionAnalysis() {
        System.out.println("\nLecturers solution analysis");
        lecturerSatisfactions = new int[students.size()];
        int[] projectsAnalysis = new int[lecturers.size()];
        int satisfaction;
        int totalSatisfaction;
        for (Lecturer lect : lecturers) {
            Iterator<Student> iterator = lect.subscribedStudents.iterator();
            for (; iterator.hasNext(); ) {
                Student currentStudent = iterator.next();
                if (currentStudent == null) {
                    iterator.remove();
                }
            }
        }


        for (int i = 0; i < lecturers.size(); ++i) {
            totalSatisfaction = 0;
            List<Student> preferredStuds = lecturers.get(i).preferredStudents;
            List<Student> subscribedStuds = lecturers.get(i).subscribedStudents;
            int foundBefore = 0;
            for (int k = 0; k < preferredStuds.size(); ++k) {
                satisfaction = 0;
                for (int j = 0; j < subscribedStuds.size(); ++j) {
                    if (preferredStuds.get(k).equals(subscribedStuds.get(j))) {
                        satisfaction = (int) ((double) (preferredStuds.size() - k) / (preferredStuds.size() - foundBefore) * 100);
                        foundBefore++;
                        break;
                    }
                }
                totalSatisfaction += satisfaction;
                int index = getStudentIndex(preferredStuds.get(k).name);
                if (lecturerSatisfactions[index] == 0) {
                    lecturerSatisfactions[index] = satisfaction;
                }
            }
            if (subscribedStuds.size() > 0) {
                totalSatisfaction /= subscribedStuds.size();
            }
            projectsAnalysis[i] = totalSatisfaction;
        }
        int sum = 0;
        for (int i = 0; i < projectsAnalysis.length; ++i) {
            String name = lecturers.get(i).getName();
            System.out.println(name + " : " + projectsAnalysis[i] + "% pleased.");
            sum += projectsAnalysis[i];
        }
        sum /= projectsAnalysis.length;
        System.out.println("Overall : " + sum + "% pleased.");
    }

    /**
     * Average between student satisfaction and lecturer satisfaction for that student.
     */
    public void printMatchingSolutionAnalysis() {
        System.out.println("\nMatching Solution Analysis");
        int count = 0;
        int sum = 0;
        for (int i = 0; i < students.size(); ++i) {
            if (projectsAnalysis[i] > 0) {
                System.out.println(students.get(i).getName() + " - " + students.get(i).projectLecturer.getName() + " : "
                        + (projectsAnalysis[i] + lecturerSatisfactions[i]) / 2 + "% pleased.");
                sum += ((projectsAnalysis[i] + lecturerSatisfactions[i]) / 2);
                count++;
            }
        }
        System.out.println("Overall : " + sum / count + "% pleased.");
    }

    public String toString() {
        String studentsHeader = " Student preferences ";
        int maxLengthOfStudentsHeader = studentsHeader.length();
        maxLengthOfStudentsHeader = getMaxHeaderLength(students, maxLengthOfStudentsHeader, "students");
        String lecturersHeader = " Lecturer preferences ";
        int maxLengthOfLecturerHeader = lecturersHeader.length();
        maxLengthOfLecturerHeader = getMaxHeaderLength(lecturers, maxLengthOfLecturerHeader, "students");
        String projectsHeader = " Available projects ";
        int maxLengthOfProjectsHeader = projectsHeader.length();
        maxLengthOfProjectsHeader = getMaxHeaderLength(lecturers, maxLengthOfProjectsHeader, "projects");

        int totalHeaderLength = maxLengthOfStudentsHeader + maxLengthOfLecturerHeader + maxLengthOfProjectsHeader;


        StringBuilder lineSb = new StringBuilder();
        for (int i = 0; i < totalHeaderLength + 4; ++i) {
            lineSb.append("-");
        }
        String line = String.valueOf(lineSb);

        StringBuilder table = new StringBuilder(line);
        table.append("\n");
        table.append("|");
        table.append(getPaddedHeader(studentsHeader, maxLengthOfStudentsHeader));
        table.append("|");
        table.append(getPaddedHeader(lecturersHeader, maxLengthOfLecturerHeader));
        table.append("|");
        table.append(getPaddedHeader(projectsHeader, maxLengthOfProjectsHeader));
        table.append("|\n");
        table.append(line);
        table.append("\n");

        int currentStudent = 0;
        int currentLecturer = 0;

        while (currentStudent < students.size() || currentLecturer < lecturers.size()) {
            table.append("|");
            String currentRow = currentStudent < students.size() ? students.get(currentStudent).toString() : "";
            table.append(getTableRow(currentRow, maxLengthOfStudentsHeader));
            table.append("|");
            currentRow = currentLecturer < lecturers.size() ? lecturers.get(currentLecturer).toString() : "";
            table.append(getTableRow(currentRow, maxLengthOfLecturerHeader));
            table.append("|");
            currentRow = currentLecturer < lecturers.size() ? lecturers.get(currentLecturer).availableProjectsToString() : "";
            table.append(getTableRow(currentRow, maxLengthOfProjectsHeader));
            table.append("|\n");
            table.append(line);
            table.append("\n");

            currentStudent++;
            currentLecturer++;
        }

        return String.valueOf(table);
    }
}
