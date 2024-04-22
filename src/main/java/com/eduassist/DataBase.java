package com.eduassist;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {

    private static String username;
    static ArrayList<Subject> subjects = new ArrayList<>();

    public static void setData(String user) {
        username = user;
        createSubjects();
    }

    public static boolean checkUserData(String username, String password) {

        boolean found = false;

        Connection conn = null;
        try {

            conn = DriverManager.getConnection("jdbc:sqlite:src/DataBase/userLogIn.db");

            String sql = "SELECT * FROM User WHERE username = ? AND password = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            found = rs.next();

        } catch (SQLException e) {
            System.err.println("Error al leer datos de la base de datos SQLite: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión a la base de datos SQLite: " + e.getMessage());
            }
        }

        return found;
    }

    private static void createSubjects() {

        Connection conn = null;

        try {

            conn = DriverManager.getConnection("jdbc:sqlite:src/DataBase/Subject.db");

            Statement stmt = conn.createStatement();

            String sql = "SELECT * FROM " + username;

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                String name = rs.getString("name");
                String teacher = rs.getString("teacher");
                String classroom = rs.getString("classroom");
                String percentage = rs.getString("percentage");
                String grades = rs.getString("grades");
                String gradeNames = rs.getString("gradeNames");
                String schedule = rs.getString("schedule");
                int credits = rs.getInt("credits");
                int umes = rs.getInt("umes");
                float targetAverage = rs.getFloat("targetAverage");

                Subject subject = new Subject(name, teacher, classroom, percentage, grades, gradeNames, schedule, credits, umes, targetAverage);
                subjects.add(subject);

            }

        } catch (SQLException e) {
            System.err.println("Error al leer datos de la base de datos SQLite: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión a la base de datos SQLite: " + e.getMessage());
            }
        }
    }

    public static ArrayList<Subject> getSubjects() {
        return subjects;
    }

}

