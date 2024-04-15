package com.eduassist;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Calendar {

    @FXML
    private AnchorPane calendarPane;

    @FXML
    public void initialize() {

        GridPane calendarGrid = createCalendarGrid();
        calendarPane.getChildren().add(calendarGrid);
    }

    private final String[] days = {"LUNES","MARTES","MIÉRCOLES","JUEVES","VIERNES", "SÁBADO", "DOMINGO"};

    private GridPane createCalendarGrid() {

        GridPane calendarPane = new GridPane();

        int cellHeight = 30;
        int cellWidth = 200;
        
        calendarPane.add(hourGrid(cellHeight), 0, 0);

        for (int week = 0; week < 4; week++) {

            int weekIndex = week * 7;

            for (int row = 0; row < 7; row++) {
                GridPane columnGrid = new GridPane();

                Label day = new Label(days[row]);

                day.setPrefSize(cellWidth + 1, cellHeight * 2);
                day.setStyle("-fx-font: 18 system; -fx-font-weight: bold; -fx-background-color: #fafafa; -fx-alignment: center; -fx-border-color: #d6d6d6; -fx-border-width: 1px");
                columnGrid.add(day, 0, 0);

                for (int col = 1; col < 35; col++) {
                    Rectangle cell = new Rectangle(cellWidth, cellHeight);
                    cell.setFill(Color.valueOf("#fff"));
                    cell.setStyle("-fx-stroke: #d6d6d6; -fx-stroke-width: 1;");
                    if (row == 3 && col == 1) {
                        cell.setHeight(cellHeight * 3 + 2);
                    }
                    if (row == 3 && col == 2) {
                        cell.setHeight(0);
                        cell.setStyle("-fx-stroke-width: 0;");
                    }
                    if (row == 3 && col == 3) {
                        cell.setHeight(0);
                        cell.setStyle("-fx-stroke-width: 0;");
                    }

                    cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            cell.setFill(Color.BLUE);
                        }
                    });
                    columnGrid.add(cell, 0, col);
                }
                calendarPane.add(columnGrid, row + weekIndex + 1, 0);
            }
        }
        return calendarPane;
    }

    private static GridPane hourGrid(int cellHeight) {

        GridPane hourGrid = new GridPane();

        Label hourTitle = new Label("HORA");
        hourTitle.setPrefSize(100, cellHeight * 2);
        hourTitle.setStyle("-fx-font: 18 system; -fx-font-weight: bold; -fx-background-color: #fafafa; -fx-alignment: center; -fx-border-color: #d6d6d6; -fx-border-width: 1px;");
        hourGrid.add(hourTitle,0,0);

        for (int h = 5; h < 22; h++) {
            String min = ":00";
            for (int i = 0; i < 2; i ++) {
                Label hour = new Label(h + min);
                hour.setPrefSize(100, cellHeight + 1);
                hour.setStyle("-fx-font: 16 system; -fx-font-weight: bold; -fx-background-color: #fafafa; -fx-alignment: center; -fx-border-color: #d6d6d6; -fx-border-width: 1px;");
                hourGrid.add(hour, 0, 2 * (h-4) + i);
                min = ":30";
            }
        }
        return hourGrid;
    }

}
