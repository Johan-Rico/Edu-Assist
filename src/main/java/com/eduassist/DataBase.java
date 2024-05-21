package com.eduassist;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.sql.*;
import java.util.ArrayList;

public class DataBase {

    private static String username;
    private static String userName;
    private static ArrayList<Subject> subjects = new ArrayList<>();
    private static ArrayList<Event> events = new ArrayList<>();


    //*
    // -----------------> para programar el código en el IDE

    private static final String userLoginDB = "jdbc:sqlite:DataBase/userLogIn.db";
    private static final String subjectDB = "jdbc:sqlite:DataBase/Subject.db";
    private static final String eventDB = "jdbc:sqlite:DataBase/Event.db";

    //*/


    /*
    // -----------------> para crear el .jar

    private static final String path;

    static {
        try {
            path = new File(LogIn.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + FileSystems.getDefault().getSeparator();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String userLoginDB = "jdbc:sqlite:" + path + "DataBase/userLogIn.db";
    private static final String subjectDB = "jdbc:sqlite:" + path + "DataBase/Subject.db";
    private static final String eventDB = "jdbc:sqlite:" + path + "DataBase/Event.db";

    //*/


    public static void setData(String user) {
        username = user;
        Connection conn = null;
        createSubjects();
        createEvents();
    }

    public static boolean checkUserData(String username, String password) {

        boolean found = false;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(userLoginDB);

            String sql = "SELECT * FROM User WHERE username = ? AND password = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);


            ResultSet rs = pstmt.executeQuery();

            found = rs.next();

            if (found) { userName = rs.getString("name"); }

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

                int code = rs.getInt("code");
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
                String subjectColor = rs.getString("titleColor");
                String textColor = rs.getString("textColor");

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

    private static void createEvents() {

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(eventDB);

            Statement stmt = conn.createStatement();

            String sql = "SELECT * FROM " + username;

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                int code = rs.getInt("code");
                String title = rs.getString("title");
                String info = rs.getString("info");
                String date = rs.getString("date");
                String eventColor = rs.getString("titleColor");
                String textColor = rs.getString("textColor");

                Event event = new Event(code, title, info, date, eventColor, textColor);
                events.add(event);
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

    public static int selectEventCode() {

        int newCode = 0;
        boolean found;

        do {
            found = false;

            for (Event event : events) {
                if (event.getCode() == newCode) {
                    found = true;
                    newCode++;
                    break;
                }
            }

        } while (found);

        return newCode;
    }

    public static void deleteEvent(int code) {

        for (Event event : events) {
            if (event.getCode() == code) {
                events.remove(event);
                break;
            }
        }

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(eventDB);

            String sql = String.format("DELETE FROM %s WHERE code = %d", username, code);

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

    public static void updateEvent(int code, String title, String info, String date) {

        for (Event event : events) {
            if (event.getCode() == code) {
                event.updateInfo(title, info, date);
                break;
            }
        }

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(eventDB);

            String sql = String.format("UPDATE %s SET title = '%s', info = '%s', date = '%s' WHERE code = %d", username, title, info, date, code);

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

    public static void updateTarget(int code, float target) {

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(subjectDB);

            String sql = String.format("UPDATE %s SET targetAverage = %f WHERE code = %d", username, target, code);

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

    public static void updateColor(int code, String titleColor, String textColor, boolean isSubject) {

        Connection conn = null;

        try {
            if (isSubject) {
                conn = DriverManager.getConnection(subjectDB);
            } else {
                conn = DriverManager.getConnection(eventDB);
            }

            String sql1 = String.format("UPDATE %s SET titleColor = '%s' WHERE code = %d", username, titleColor, code);
            String sql2 = String.format("UPDATE %s SET textColor = '%s' WHERE code = %d", username, textColor, code);

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

    public static void addEvent(Event event) {
        events.add(event);

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(eventDB);

            String sql = String.format("INSERT INTO %s (code, title, info, date, titleColor, textColor) VALUES (%d, '%s', '%s', '%s', '%s', '%s')", username, event.getCode(), event.getTitle(), event.getInfo(), event.getStringDate(), event.getEventColor(), event.getTextColor());

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

    public static ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public static ArrayList<Event> getEvents() {
        return events;
    }

    public static String getUserName() { return userName; }

}