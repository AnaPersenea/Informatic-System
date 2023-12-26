package org.example;

import java.io.*;
import java.util.*;

public class Secretariat implements Serializable {
    private final List<Student> students = new ArrayList<>();
    private final List<Curs<?>> courses = new ArrayList<>();
    private final HashMap<String, List<String>> preferences = new HashMap<>();

    public Secretariat() {}

    void saveStudentInfo(Student student) {
        students.add(student);
    }

    void addStudent(String command, FileWriter out) {
        String studentCycle = command.split(" - ")[1];
        String studentName = command.split(" - ")[2];

        if (findStudent(studentName) != null) {
            try (BufferedWriter bw = new BufferedWriter(out)) {
                bw.write("***");
                bw.newLine();
                bw.write("Student duplicat: " + studentName);
                bw.newLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return;
        }

        if (studentCycle.equals("licenta")) {
            StudentLicenta bachelorStudent = new StudentLicenta(studentName);
            saveStudentInfo(bachelorStudent);
        } else if (studentCycle.equals("master")) {
            StudentMaster masterStudent = new StudentMaster(studentName);
            saveStudentInfo(masterStudent);
        }
    }

    void addGradeToStudent(String studentName, String studentGrade) {
        for (Student student : students) {
            if (student.getNume().equals(studentName)) {
                student.medie = Double.parseDouble(studentGrade);
            }
        }
    }

    void readNotes(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files == null) {
            System.out.println("No files to read");
            return;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().startsWith("note")) {
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String studentDetails = scanner.nextLine();
                        String studentName = studentDetails.split(" - ")[0];
                        String studentGrade = studentDetails.split(" - ")[1];
                        addGradeToStudent(studentName, studentGrade);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    void modifyGrade(String command) {
        String studentName = command.split(" - ")[1];
        String studentNewGrade = command.split(" - ")[2];

        Student student = findStudent(studentName);
        student.medie = Double.parseDouble(studentNewGrade);
    }

    void postNotes(FileWriter out) {
        sortStudentsByGrade();
        try (BufferedWriter bw = new BufferedWriter(out)) {
            bw.write("***");
            bw.newLine();
            for (Student student : students) {
                bw.write(student.getNume() + " - " + student.medie);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    void saveCourseInfo(Curs<?> curs) {
        courses.add(curs);
    }

    void addCourse(String command) {
        String courseCycle = command.split(" - ")[1];
        String courseName = command.split(" - ")[2];
        String courseCapacity = command.split(" - ")[3];
        int capacity = Integer.parseInt(courseCapacity);

        if (courseCycle.equals("licenta")) {
            Curs<StudentLicenta> cursLicenta = new Curs<>(courseName, capacity);
            saveCourseInfo(cursLicenta);
        } else if (courseCycle.equals("master")) {
            Curs<StudentMaster> cursMaster = new Curs<>(courseName, capacity);
            saveCourseInfo(cursMaster);
        }
    }

    void savePreferenceInfo(String studentName, List<String> studentPrefers) {
        preferences.put(studentName, studentPrefers);
    }

    void addPreferences(String command) {
        String[] elementeComanda = command.split(" - ");
        String studentName = elementeComanda[1];

        List<String> studentPrefers = new ArrayList<>();
        for (int i = 2; i < elementeComanda.length; i++) {
            studentPrefers.add(elementeComanda[i]);
        }
        savePreferenceInfo(studentName, studentPrefers);
    }

    Double smallestNote(List<Student> students) {
        Double small = 10.0;
        for (Student student : students) {
            if (student.medie < small) {
                small = student.medie;
            }
        }
        return small;
    }

    void allocateCourse() {
        sortStudentsByGrade();
        int isPreferenceSet;
        for (Student student : students) {
            isPreferenceSet = 0;
            List<String> studentPreferredCourses = preferences.get(student.getNume());
            for (String preferName : studentPreferredCourses) {
                if (isPreferenceSet == 0) {
                    Curs<?> course = findCourse(preferName);
                    if (course.getStudentiInrolati().size() < course.getCapacitateMaxima()) {
                        course.getStudentiInrolati().add(student);
                        student.cursOptional = course.getDenumireCurs();
                        isPreferenceSet = 1;
                    } else {
                        if (course.getStudentiInrolati().size() >= course.getCapacitateMaxima()) {
                            Double smallestNote =  smallestNote(course.studentiInrolati);
                            if (Double.compare(smallestNote, student.medie) == 0) {
                                course.getStudentiInrolati().add(student);
                                student.cursOptional = course.getDenumireCurs();
                                isPreferenceSet = 1;
                            }
                        }
                    }
                }
            }
        }
    }

    void sortStudentsByName(List<Student> students) {
        students.sort(Comparator.comparing(Student::getNume));
    }

    void sortStudentsByGrade() {
        students.sort((student1, student2) -> {
            int compareGrade = Double.compare(student2.medie, student1.medie);
            if (compareGrade == 0) {
                return student1.getNume().compareTo(student2.getNume());
            }
            return compareGrade;
        });
    }

    void postStudentInfo(FileWriter out, String command) {
        String studentName = command.split(" - ")[1];
        Student student = findStudent(studentName);
        try (BufferedWriter bw = new BufferedWriter(out)) {
            bw.write("***");
            bw.newLine();
            if (student instanceof StudentLicenta) {
                bw.write("Student Licenta: " + student.getNume() + " - " + student.medie + " - " + student.cursOptional);
                bw.newLine();
            }
            if (student instanceof StudentMaster) {
                bw.write("Student Master: " + student.getNume() + " - " + student.medie + " - " + student.cursOptional);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    void postCourseInfo(FileWriter out, String command) {
        String courseName = command.split(" - ")[1];
        Curs<?> course = findCourse(courseName);
        try (BufferedWriter bw = new BufferedWriter(out)) {
            bw.write("***");
            bw.newLine();
            bw.write(course.getDenumireCurs() + " (" + course.getCapacitateMaxima() + ")");
            bw.newLine();
            sortStudentsByName(course.studentiInrolati);
            for (Student student : course.studentiInrolati) {
                bw.write(student.getNume() + " - " + student.medie);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    Student findStudent(String studentName) {
        for (Student student : students) {
            if (student.getNume().equals(studentName)) {
                return student;
            }
        }
        return null;
    }

    Curs<?> findCourse(String courseName) {
        for (Curs<?> course : courses) {
            if (course.getDenumireCurs().equals(courseName)) {
                return course;
            }
        }
        return null;
    }
}
