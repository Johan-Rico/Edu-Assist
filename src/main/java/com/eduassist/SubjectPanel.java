package com.eduassist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SubjectPanel {

    private final String cellStyle = "-fx-alignment: center; al-fx-font: 14 system; -fx-font-weight: bold; -fx-background-color: #fbfbfb;-fx-border-width: 1px; -fx-border-color: #545454; -fx-text-fill: #444;";

    @FXML
    private Button targetGradeButton, neededGradeButton, updateTargetButton;
    @FXML
    private GridPane gradesGrid;
    @FXML
    private Label weightedAverageLabel, totalAverageLabel, subjectNameLabel;
    @FXML
    private TextField targetAverageField;
    @FXML
    private TitledPane gradesPane, infoPane;
    @FXML
    private Accordion accordion;
    @FXML
    private VBox infoBox;
    @FXML
    private ColorPicker titleColor, textColor;

    private Subject subject;

    private boolean inTarget = false;

    @FXML
    private void initialize() {

        accordion.setExpandedPane(gradesPane);
        neededGradeButton.setDisable(true);
        updateTargetButton.setDisable(true);

    }

    public void setInfo(Subject subject) {

        this.subject = subject;
        subjectNameLabel.setText(subject.getName() + "\n" + subject.getCode());
        titleColor.setValue(stringToColor(subject.getSubjectColor()));
        textColor.setValue(stringToColor(subject.getTextColor()));
        updateColors();
        setGrades();

        weightedAverageLabel.setText(Float.toString(subject.getWeightedAverage()));
        totalAverageLabel.setText(Float.toString(subject.getTotalAverage()));
        targetAverageField.setText(Float.toString(subject.getTargetAverage()));

        if (subject.isEmpty()) {
            gradesPane.setDisable(true);
            accordion.setExpandedPane(infoPane);
        }

        checkText();
        fillInfo();

    }

    private void setGrades() {

        float[] percentage = subject.getPercentage();
        String[] gradeNames = subject.getGradeNames();

        if (percentage != null) {
            for (int i = 0; i < percentage.length; i++) {

                Label labelPerc = new Label(Integer.toString((int) (percentage[i] * 100)) + "%");
                Label labelGradeName = new Label(gradeNames[i]);
                Label labelGrade = new Label();

                labelPerc.setPrefSize(100, 30);
                labelGradeName.setPrefSize(200, 30);
                labelGrade.setPrefSize(100, 30);

                labelPerc.setStyle(cellStyle);
                labelGradeName.setStyle(cellStyle + "-fx-background-color: #e6e6e6");
                labelGrade.setStyle(cellStyle);

                gradesGrid.add(labelPerc, 0, i);
                gradesGrid.add(labelGradeName, 1, i);
                gradesGrid.add(labelGrade, 2, i);
            }
        updateGrades();
        }

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
        targetAverageField.setDisable(false);
        disableButton();
        inTarget = true;

        updateGrades();
    }

    @FXML
    private void neededGrade(ActionEvent actionEvent) {
        neededGradeButton.setDisable(true);
        targetGradeButton.setDisable(false);
        targetAverageField.setDisable(true);
        updateTargetButton.setDisable(true);
        inTarget = false;

        updateGrades();
    }

    @FXML
    private void updateTarget(ActionEvent actionEvent) {
        subject.setTargetAverage(Float.parseFloat(targetAverageField.getText()));
        updateTargetButton.setDisable(true);
        targetAverageField.setText(Float.toString(subject.getTargetAverage()));

        updateGrades();
    }

    private void checkText() {
        targetAverageField.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.matches("\\d?\\.?\\d?")) {
                targetAverageField.setText(oldValue);
            } else if (!newValue.isEmpty() && Character.isDigit(newValue.charAt(0)) && Float.parseFloat(newValue) > 5) {
                targetAverageField.setText(oldValue);
            }

            if (newValue.matches("\\d\\d")) {
                targetAverageField.setText(Character.toString(newValue.charAt(0)) + "." + Character.toString(newValue.charAt(1)));
            }

            disableButton();

        });
    }

    private void disableButton() {
        if (!targetAverageField.getText().isEmpty() && !targetAverageField.getText().equals(".")) {
            updateTargetButton.setDisable(Float.parseFloat(targetAverageField.getText()) == subject.getTargetAverage());
        } else {
            updateTargetButton.setDisable(true);
        }
    }

    private void fillInfo() {

        addLabel("MATERIA: ", subject.getName());
        addLabel("CÓDIGO: ", Integer.toString(subject.getCode()));
        addLabel("PROFESOR: ", subject.getTeacher());
        addLabel("SALÓN: ", subject.getClassroom());
        if (!subject.isEmpty()) {
            addLabel("PROMEDIO PONDERADO: ", Float.toString(subject.getWeightedAverage()));
            addLabel("PROMEDIO TOTAL: ", Float.toString(subject.getTotalAverage()));
        }
        addLabel("CRÉDITOS: ", Integer.toString(subject.getCredits()));
        addLabel("UMES: ", Integer.toString(subject.getUmes()));

    }

    private void addLabel(String nameStg, String valueStg) {

        Label nameLabel = new Label(nameStg);
        Label valueLabel = new Label(valueStg);

        nameLabel.setStyle("al-fx-font: 24 arial; -fx-font-weight: bold;");
        valueLabel.setStyle("al-fx-font: 24 arial; -fx-font-weight: normal;");

        HBox box = new HBox(nameLabel, valueLabel);
        infoBox.getChildren().add(box);

    }

    @FXML
    private void changeColor(ActionEvent actionEvent) {
        subject.setColors(colorToString(titleColor.getValue()), colorToString(textColor.getValue()));
        updateColors();
        Calendar.updateWeek();
    }

    private String colorToString(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        double a = color.getOpacity();

        return String.format("rgba(%d, %d, %d, %.2f)", r, g, b, a);
    }

    private Color stringToColor(String color) {

        String[] components = color.substring(5, color.length() - 1).split(",");

        double red = Double.parseDouble(components[0].trim());
        double green = Double.parseDouble(components[1].trim());
        double blue = Double.parseDouble(components[2].trim());
        double alpha = Double.parseDouble(components[3].trim());

        red /= 255.0;
        green /= 255.0;
        blue /= 255.0;

        return new Color(red, green, blue, alpha);

    }

    private void updateColors() {
        subjectNameLabel.setBackground(new Background(new BackgroundFill(stringToColor(subject.getSubjectColor()), null, null)));
        subjectNameLabel.setTextFill(stringToColor(subject.getTextColor()));
    }

}