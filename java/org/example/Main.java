package org.example;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No test provided!");
            return;
        }
        String testName = args[0];
        String directoryPath = "src/main/resources/" + testName;
        String readPath = directoryPath + "/" + testName + ".in";
        String writePath = directoryPath + "/" + testName + ".out";
        File in = new File(readPath);

        Secretariat secretar = new Secretariat();

        try (Scanner scanner = new Scanner(in)) {
            while (scanner.hasNextLine()) {
                String fullCommand = scanner.nextLine();
                String command = fullCommand.split(" - ")[0];
                if (command.equals("adauga_student")) {
                    try (FileWriter out = new FileWriter(writePath, true)) {
                        secretar.addStudent(fullCommand, out);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }

                if (command.equals("citeste_mediile")) {
                    secretar.readNotes(directoryPath);
                }

                if (command.equals("posteaza_mediile")) {
                    try (FileWriter out = new FileWriter(writePath, true)) {
                        secretar.postNotes(out);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }

                if (command.equals("contestatie")) {
                    secretar.modifyGrade(fullCommand);
                }

                if (command.equals("adauga_curs")) {
                    secretar.addCourse(fullCommand);
                }
                if (command.equals("adauga_preferinte")) {
                    secretar.addPreferences(fullCommand);
                }
                if (command.equals("repartizeaza")) {
                    secretar.allocateCourse();
                }
                if (command.equals("posteaza_curs")) {
                    try (FileWriter out = new FileWriter(writePath, true)) {
                        secretar.postCourseInfo(out, fullCommand);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (command.equals("posteaza_student")) {
                    try (FileWriter out = new FileWriter(writePath, true)) {
                        secretar.postStudentInfo(out, fullCommand);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
