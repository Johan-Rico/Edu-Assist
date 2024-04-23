package com.eduassist;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Calendar {

    @FXML
    private AnchorPane calendarPane;

    private static final int cellHeight = 30;
    private static final int cellWidth = 200;
    private  static ArrayList<Subject> subjects = new ArrayList<>();

    @FXML
    public void initialize() {
        subjects = DataBase.subjects;
        GridPane calendarGrid = createCalendarGrid();
        calendarPane.getChildren().add(calendarGrid);
        //updateCalendarTest(calendarGrid);
    }

    private final String[] days = {"LUNES","MARTES","MIÉRCOLES","JUEVES","VIERNES", "SÁBADO", "DOMINGO"};


    private GridPane createCalendarGrid() {

        GridPane calendarGridPane = new GridPane();

        calendarGridPane.add(hourGrid(), 0, 0);

        for (int col = 0; col < 7; col++) {         // Crear los 7 días de la semana
            GridPane columnGrid = new GridPane();

            Label day = new Label(days[col]);

            day.setPrefSize(cellWidth, cellHeight * 2);
            day.setStyle("-fx-font: 16 system; -fx-font-weight: bold; -fx-background-color: #fafafa; -fx-alignment: center; -fx-border-color: #d6d6d6; -fx-border-width: 1px");
            columnGrid.add(day, 0, 0);

            for (int row = 1; row < 35; row++) {        // Crear todas las filas de cada día

                Button cell = new Button();
                cell.setStyle("-fx-border-color: #d6d6d6; -fx-border-width: 1px; -fx-background-color: #fff");

                cell.setMinSize(cellWidth,0);
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
        updateWeek(calendarGridPane);

        return calendarGridPane;
    }

    private static GridPane hourGrid() {

        GridPane hourGrid = new GridPane();

        Label hourTitle = new Label("HORA");
        hourTitle.setPrefSize(150, cellHeight * 2);
        hourTitle.setStyle("-fx-font: 16 system; -fx-font-weight: bold; -fx-background-color: #fafafa; -fx-alignment: center; -fx-border-color: #d6d6d6; -fx-border-width: 1px;");
        hourGrid.add(hourTitle, 0, 0);

        for (int h = 5; h < 22; h++) {
            String min1 = ":00";
            String min2 = ":30";
            for (int i = 0; i < 2; i++) {
                Label hour = new Label(h + min1 + " - " + (h + i) + min2);
                hour.setPrefSize(150, cellHeight);
                hour.setStyle("-fx-font: 14 system; -fx-font-weight: bold; -fx-background-color: #fafafa; -fx-alignment: center; -fx-border-color: #d6d6d6; -fx-border-width: 1px;");
                hourGrid.add(hour, 0, 2 * (h - 4) + i);
                min1 = ":30";
                min2 = ":00";
            }
        }
        return hourGrid;
    }

    private void updateWeek(GridPane grid) {

        for (Subject subject : subjects) {

            float[][] schedule = subject.getSchedule();

            for (float[] cl : schedule) {

                int day = (int)cl[0];
                int hour = (int)(cl[1] * 2) - 9;
                int time = (int)(cl[2] * 2);

                GridPane col = (GridPane) grid.getChildren().get(day + 1);

                Button subjectButton = new Button(subject.getName() + "\n" + subject.getClassroom());
                subjectButton.setWrapText(true);
                subjectButton.setTextAlignment(TextAlignment.CENTER);
                subjectButton.setStyle("-fx-font: 14 system; -fx-font-weight: bold; -fx-background-color: #0024aa; -fx-border-color: #606060; -fx-border-width: 1px; -fx-text-fill: #e4e4e4");
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

    private void showInfo(Subject subject) throws IOException {

        Stage subjectStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SubjectInfo.fxml"));
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


}
