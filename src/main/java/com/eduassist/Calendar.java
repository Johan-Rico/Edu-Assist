package com.eduassist;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
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
    private AnchorPane header;
    @FXML
    private AnchorPane calendarPane;
    @FXML
    private ScrollPane hourScrollPane;
    @FXML
    private ScrollPane dayScrollPane;
    @FXML
    private ScrollPane calendarScrollPane;

    private static final int cellHeight = 30;
    private static final int cellWidth = 200;
    private static ArrayList<Subject> subjects = new ArrayList<>();
    private static GridPane calendarGrid;

    @FXML
    public void initialize() {

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
                cell.setStyle("-fx-border-color: #d6d6d6; -fx-border-width: 1px; -fx-background-color: #fff");

                cell.setMinSize(cellWidth, 0);
                cell.setPrefSize(cellWidth, cellHeight);
                cell.setMaxSize(cellWidth, cellHeight);

                cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        // Añadir algo al calendario
                    }
                });
                columnGrid.add(cell, 0, row);

            }

            calendarGridPane.add(columnGrid, col + 1, 0);
        }
        return calendarGridPane;
    }

    public static void updateWeek() {

        for (Subject subject : subjects) {

            float[][] schedule = subject.getSchedule();

            for (float[] cl : schedule) {

                int day = (int) cl[0];
                int hour = (int) (cl[1] * 2) - 10;
                int time = (int) (cl[2] * 2);

                GridPane col = (GridPane) calendarGrid.getChildren().get(day);

                Button subjectButton = new Button(subject.getName() + "\n" + subject.getClassroom());
                subjectButton.setWrapText(true);
                subjectButton.setTextAlignment(TextAlignment.CENTER);
                subjectButton.setStyle("-fx-font: 14 system; -fx-font-weight: bold; -fx-background-color: " + subject.getSubjectColor() +
                        "; -fx-border-color: #606060; -fx-border-width: 1px; -fx-text-fill: " + subject.getTextColor());
                subjectButton.setPrefSize(cellWidth, cellHeight * time);
                col.add(subjectButton, 0, hour);

                subjectButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            showInfo(subject);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                });


                for (int t = 0; t < time; t++) {
                    Button cell = (Button) col.getChildren().get(hour + t);
                    cell.setPrefHeight(0);
                    cell.setStyle("-fx-border-width: 0px");
                }
            }
        }

    }

    private static void showInfo(Subject subject) throws IOException {

        Stage subjectStage = new Stage();
        FXMLLoader loader = new FXMLLoader(Calendar.class.getResource("SubjectInfo.fxml"));
        StackPane subjectPanel = loader.load();

        SubjectInfo info = loader.getController();
        info.setInfo(subject);

        Scene scene = new Scene(subjectPanel, 400, 600);


        subjectStage.setResizable(false);
        subjectStage.setTitle(subject.getName());
        subjectStage.setScene(scene);

        subjectStage.initModality(Modality.APPLICATION_MODAL);
        subjectStage.showAndWait();

    }

    public void openInteractiva(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://interactivavirtual.eafit.edu.co/"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void openReservas(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://bdigital.spapps.eafit.edu.co/openroom/index.php"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
