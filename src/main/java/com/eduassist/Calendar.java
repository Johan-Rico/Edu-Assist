package com.eduassist;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;

import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;

public class Calendar {

    @FXML
    private AnchorPane calendarPane;
    @FXML
    private ScrollPane hourScrollPane, dayScrollPane, calendarScrollPane;
    @FXML
    private Label userNameLabel;

    private static final int cellHeight = 34;
    private static final int cellWidth = 200;
    private static ArrayList<Subject> subjects;
    private static boolean[][] calendar;
    private static GridPane calendarGrid;

    @FXML
    private void initialize() {

        userNameLabel.setText(DataBase.getUserName());
        subjects = DataBase.getSubjects();
        calendarGrid = createCalendarGrid();
        updateWeek();
        calendarPane.getChildren().add(calendarGrid);

        hourScrollPane.vvalueProperty().bindBidirectional(calendarScrollPane.vvalueProperty());
        calendarScrollPane.vvalueProperty().bindBidirectional(hourScrollPane.vvalueProperty());

        dayScrollPane.hvalueProperty().bindBidirectional(calendarScrollPane.hvalueProperty());
        calendarScrollPane.hvalueProperty().bindBidirectional(dayScrollPane.hvalueProperty());

    }

    private static GridPane createCalendarGrid() {

        GridPane calendarGridPane = new GridPane();

        for (int col = 0; col < 7; col++) {         // Crear los 7 días de la semana
            GridPane columnGrid = new GridPane();

            for (int row = 0; row < 34; row++) {        // Crear todas las filas de cada día

                Button cell = new Button();

                int finalCol = col;
                int finalRow = row;

                columnGrid.add(cell, 0, row);

            }

            calendarGridPane.add(columnGrid, col + 1, 0);
        }
        return calendarGridPane;
    }

    private static void resetCalendar() {

        calendar = new boolean[7][34];

        for (int col = 0; col < 7; col++) {
            GridPane columnGrid = (GridPane) calendarGrid.getChildren().get(col);
            for (int row = 0; row < 34; row++) {

                Button cell = (Button) columnGrid.getChildren().get(row);
                cell.setStyle("-fx-border-color: #d6d6d6; -fx-border-width: 1px; -fx-background-color: #fff");

                cell.setWrapText(true);
                cell.setTextAlignment(TextAlignment.CENTER);
                cell.setText("");

                cell.setMinSize(cellWidth, 0);
                cell.setPrefSize(cellWidth, cellHeight);
                cell.setMaxSize(cellWidth, cellHeight);

                int finalCol = col;
                int finalRow = row;

                cell.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent ev) {
                        try {
                            addEvent(finalCol, finalRow);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

            }
        }

    }

    public static void updateWeek() {

        resetCalendar();

        for (Subject subject : subjects) {
            float[][] schedule = subject.getSchedule();
            for (float[] date : schedule) {

                Button subjectButton = addEventToCalendar(date, subject.getName() + "\n" + subject.getClassroom(), subject.getSubjectColor(), subject.getTextColor());

                subjectButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent ev) {
                        try {
                            showSubject(subject);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }

        for (Event event : DataBase.getEvents()) {

            Button eventButton = addEventToCalendar(event.getDate(), event.getTitle(), event.getEventColor(), event.getTextColor());

            eventButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent ev) {
                    try {
                        showEvent(event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

    }

    private static Button addEventToCalendar(float[] date, String title, String titleColor, String textColor) {

        int day = (int) date[0];
        int hour = (int) (date[1] * 2) - 10;
        int time = (int) (date[2] * 2);

        GridPane col = (GridPane) calendarGrid.getChildren().get(day);

        Button button = (Button) col.getChildren().get(hour);
        button.setText(title);
        button.setStyle("-fx-font: 15 system; -fx-font-weight: bold; -fx-background-color: " + titleColor +
                "; -fx-border-color: #606060; -fx-border-width: 1px; -fx-text-fill: " + textColor);
        button.setPrefSize(cellWidth, cellHeight * time);
        button.setMaxSize(cellWidth, cellHeight * time);
        button.setPrefSize(cellWidth, cellHeight * time);

        calendar[day][hour] = true;

        for (int t = 1; t < time; t++) {
            Button cell = (Button) col.getChildren().get(hour + t);
            cell.setPrefHeight(0);
            cell.setStyle("-fx-border-width: 0px");
            calendar[day][hour + t] = true;
        }

        return button;

    }

    private static void showSubject(Subject subject) throws IOException {

        Stage subjectStage = new Stage();
        FXMLLoader loader = new FXMLLoader(Calendar.class.getResource("SubjectPanel.fxml"));
        StackPane subjectPanel = loader.load();

        SubjectPanel panel = loader.getController();
        panel.setInfo(subject);

        Scene scene = new Scene(subjectPanel, 400, 600);

        subjectStage.setResizable(false);
        subjectStage.setTitle(subject.getName());
        subjectStage.getIcons().add(Main.getIcon());
        subjectStage.setScene(scene);

        subjectStage.initModality(Modality.APPLICATION_MODAL);
        subjectStage.showAndWait();

    }


    private static void showEvent(Event event) throws IOException {

        Stage eventStage = new Stage();
        FXMLLoader loader = new FXMLLoader(Calendar.class.getResource("EventPanel.fxml"));
        StackPane eventPanel = loader.load();

        EventPanel panel = loader.getController();
        panel.setInfo(event);

        Scene scene = new Scene(eventPanel, 400, 600);

        eventStage.setResizable(false);
        eventStage.setTitle(event.getTitle());
        eventStage.getIcons().add(Main.getIcon());
        eventStage.setScene(scene);

        eventStage.initModality(Modality.APPLICATION_MODAL);
        eventStage.showAndWait();

    }

    private static void addEvent(int day, int hour) throws IOException {

        Stage addEventStage = new Stage();
        FXMLLoader loader = new FXMLLoader(Calendar.class.getResource("AddEvent.fxml"));
        StackPane addEventPanel = loader.load();

        AddEvent panel = loader.getController();
        panel.setInfo(day, hour);

        Scene scene = new Scene(addEventPanel, 400, 600);

        addEventStage.setResizable(false);
        addEventStage.setTitle("Agregar evento");
        addEventStage.getIcons().add(Main.getIcon());
        addEventStage.setScene(scene);

        addEventStage.initModality(Modality.APPLICATION_MODAL);
        addEventStage.showAndWait();

    }

    public static boolean checkCalendar(int day, int hour, Double time) {

        for (int t = 0; t < time; t++) {
            if (calendar[day][hour + t]) {
                return false;
            }
        }
        return true;

    }

    public static boolean checkCalendar(int day, int hour, Double time, float[] date) {

        float eventDay = date[0];
        float eventHour = date[1] * 2 - 10;
        float eventTime = date[2] * 2;

        for (int t = 0; t < time; t++) {
            if (calendar[day][hour + t] && (day != eventDay || eventHour > hour + t || eventHour + eventTime < hour + t)) {
                return false;
            }
        }
        return true;

    }

    private void openLink(String link) {
        try {
            Desktop.getDesktop().browse(new URI(link));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void openInteractiva(ActionEvent actionEvent) {
        openLink("https://interactivavirtual.eafit.edu.co/");
    }

    @FXML
    private void openReservas(ActionEvent actionEvent) {
        openLink("https://bdigital.spapps.eafit.edu.co/openroom/index.php");
    }

    @FXML
    private void openEpik(ActionEvent actionEvent) {
        openLink("https://www.eafit.edu.co/epik");
    }

}
