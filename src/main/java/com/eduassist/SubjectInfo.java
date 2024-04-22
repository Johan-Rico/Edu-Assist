package com.eduassist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Accordion;
import javafx.scene.layout.GridPane;

public class SubjectInfo {

    private final String cellStyle = "-fx-alignment: center; al-fx-font: 14 system; -fx-font-weight: bold; -fx-background-color: #fbfbfb;-fx-border-width: 1px; -fx-border-color: #545454; -fx-text-fill: #444;";

    @FXML
    private Button targetGradeButton, neededGradeButton, updateTargetButton;
    @FXML
    private GridPane gradesGrid;
    @FXML
    private Label weightedAverageLabel, totalAverageLabel, nameLabel;
    @FXML
    private TextField targetAverageField;
    @FXML
    private TitledPane gradesPane;
    @FXML
    private Accordion accordion;

    private Subject subject;

    private boolean inTarget = false;

    @FXML
    public void initialize() {

        accordion.setExpandedPane(gradesPane);
        neededGradeButton.setDisable(true);
        updateTargetButton.setDisable(true);

    }

    public void setInfo(Subject subject) {
        this.subject = subject;
        nameLabel.setText(subject.getName());
        setGrades();

        weightedAverageLabel.setText(Float.toString(subject.getWeightedAverage()));
        totalAverageLabel.setText(Float.toString(subject.getTotalAverage()));
        targetAverageField.setText(Float.toString(subject.getTargetAverage()));

        checkText();

    }

    private void setGrades() {

        float[] percentage = subject.getPercentage();
        String[] gradeNames = subject.getGradeNames();

        for (int i = 0; i < percentage.length; i++) {

            Label labelPerc = new Label(Integer.toString((int)(percentage[i] * 100)) + "%");
            Label labelGradeName = new Label(gradeNames[i]);
            Label labelGrade = new Label();

            labelPerc.setPrefSize(100,30);
            labelGradeName.setPrefSize(200,30);
            labelGrade.setPrefSize(100,30);

            labelPerc.setStyle(cellStyle);
            labelGradeName.setStyle(cellStyle + "-fx-background-color: #e6e6e6");
            labelGrade.setStyle(cellStyle);

            gradesGrid.add(labelPerc, 0, i);
            gradesGrid.add(labelGradeName, 1, i);
            gradesGrid.add(labelGrade, 2, i);
        }
        
        updateGrades();
    }

    private void updateGrades() {

        float[] grades = subject.getGrades();

        for (int i = 0; i < grades.length; i++) {

            float gradeValue;
            String color = "";

            if (grades[i] == -1) {
                if (inTarget) {
                    gradeValue = subject.getTargetGrade();
                } else {
                    gradeValue = subject.getNeededGrade();
                }
                if (gradeValue >= 3.5) {
                    color = " -fx-text-fill: Red";
                } else if (gradeValue >= 2) {
                    color = " -fx-text-fill: #9bb329";
                } else {
                    color = " -fx-text-fill: #009a00";
                }
            } else {
                gradeValue = grades[i];
            }

            Label gradeLabel = (Label) gradesGrid.getChildren().get(3 * (i + 1));
            gradeLabel.setStyle(cellStyle + color);
            gradeLabel.setText(Float.toString(gradeValue));
        }
    }

    @FXML
    private void targetGrade(ActionEvent actionEvent) {
        targetGradeButton.setDisable(true);
        neededGradeButton.setDisable(false);
        inTarget = true;

        updateGrades();
    }

    @FXML
    private void neededGrade(ActionEvent actionEvent) {
        neededGradeButton.setDisable(true);
        targetGradeButton.setDisable(false);
        inTarget = false;

        updateGrades();
    }

    @FXML
    private void updateTarget(ActionEvent actionEvent) {
        subject.setTargetAverage(Float.parseFloat(targetAverageField.getText()));
        updateTargetButton.setDisable(true);

        updateGrades();
    }

    private void checkText() {
        targetAverageField.textProperty().addListener((_, oldValue, newValue) -> {

            updateTargetButton.setDisable(targetAverageField.getText().equals(Float.toString(subject.getTargetAverage())));

            if (newValue.length() > oldValue.length()) {
                if (!newValue.matches("(\\d)?\\.?\\d?")) {
                    targetAverageField.setText(oldValue);
                } else if (oldValue.isEmpty() && !(Character.isDigit(newValue.charAt(0)) && Integer.parseInt(newValue) <= 5)) {
                    targetAverageField.setText(oldValue);
                } else if (newValue.length() == 2) {
                    if (Integer.parseInt(oldValue) == 5) {
                        targetAverageField.setText(oldValue);
                    } else if (newValue.charAt(1) != '.') {
                        targetAverageField.setText(newValue.charAt(0) + "." + newValue.charAt(1));
                    }
                }
            }
        });
    }

}
