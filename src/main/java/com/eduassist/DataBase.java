package com.eduassist;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {

    private static String username;
    private static ArrayList<Subject> subjects = new ArrayList<>();

    private static final String userLoginDB = "jdbc:sqlite:src/DataBase/userLogIn.db";
    private static final String subjectDB = "jdbc:sqlite:src/DataBase/Subject.db";

    public static void setData(String user) {
        username = user;
        createSubjects();
    }

    public static boolean checkUserData(String username, String password) {

        boolean found = false;
        Connection conn = null;

        try {

            conn = DriverManager.getConnection(userLoginDB);

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

            conn = DriverManager.getConnection(subjectDB);

            Statement stmt = conn.createStatement();

            String sql = "SELECT * FROM " + username;

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                String code = rs.getString("code");
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
                String subjectColor = rs.getString("SubjectColor");
                String textColor = rs.getString("TextColor");

                Subject subject = new Subject(code, name, teacher, classroom, percentage, grades, gradeNames, schedule, credits, umes, targetAverage, subjectColor, textColor);
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

    public static void updateTarget(String code, float target) {

        Connection conn = null;

        try {

            conn = DriverManager.getConnection(subjectDB);

            String sql = String.format("UPDATE %s SET targetAverage = %f WHERE code = '%s'", username, target, code);

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                System.out.println("Error al ejecutar la sentencia SQL: " + e.getMessage());
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

    public static void updateColor(String code, String subjectColor, String textColor) {

        Connection conn = null;

        try {

            conn = DriverManager.getConnection(subjectDB);

            String sql1 = String.format("UPDATE %s SET SubjectColor = '%s' WHERE code = '%s'", username, subjectColor, code);
            String sql2 = String.format("UPDATE %s SET TextColor = '%s' WHERE code = '%s'", username, textColor, code);

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql1);
                stmt.executeUpdate(sql2);
            } catch (SQLException e) {
                System.out.println("Error al ejecutar la sentencia SQL: " + e.getMessage());
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

