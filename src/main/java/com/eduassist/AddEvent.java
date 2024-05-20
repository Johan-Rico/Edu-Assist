package com.eduassist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddEvent {

    @FXML
    private TextField title;
    @FXML
    private TextArea info;
    @FXML
    private ChoiceBox<String> day, minute, period;
    @FXML
    private Spinner<Integer> hour;
    @FXML
    private Spinner<Double> duration;
    @FXML
    private ColorPicker eventColor, textColor;
    @FXML
    private Label eventNameLabel, errorLabel;


    private final String[] days = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

    @FXML
    private void initialize() {

        day.getItems().addAll(days);
        hour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12));
        duration.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.5, 17.0, 2.0, 0.5));
        minute.getItems().addAll("00", "30");
        period.getItems().addAll("AM", "PM");

        String style = "-fx-font: 14 system";
        day.setStyle(style);
        hour.setStyle(style);
        minute.setStyle(style);
        period.setStyle(style);
        duration.setStyle(style);
    }

    public void setInfo(int d, int h) {

        day.setValue(days[d]);
        int hourValue = h / 2;

        if (h < 16) {
            hourValue += 5;
        } else {
            hourValue -= 7;
        }

        hour.getValueFactory().setValue(hourValue);

        if (h < 14) {
            period.setValue("AM");
        } else {
            period.setValue("PM");
        }

        if (h % 2 == 0) {
            minute.setValue("00");
        } else {
            minute.setValue("30");
        }

    }

    @FXML
    private void cancelEvent(ActionEvent actionEvent) {

        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void saveEvent(ActionEvent actionEvent) {

        int selectedDay = day.getSelectionModel().getSelectedIndex();
        float selectedHour = hour.getValue();
        double selectedDuration = duration.getValue();
        String selectedPeriod = period.getValue();

        if (selectedHour < 5 && selectedPeriod.equals("AM")) {
            errorLabel.setText("No se pueden crear eventos antes de las 5:00 AM");
            return;
        }

        if (selectedHour == 12 && selectedPeriod.equals("AM")) {
            errorLabel.setText("No se pueden crear eventos antes de las 5:00 AM");
            return;
        }

        if (selectedHour > 9 && selectedPeriod.equals("PM") && selectedHour < 12) {
            errorLabel.setText("No se pueden crear eventos después de las 10:00 PM");
            return;
        }

        if (minute.getValue().equals("30")) {
            selectedHour += 0.5f;
        }
        if (selectedPeriod.equals("PM") && selectedHour < 12) {
            selectedHour += 12;
        }

        if (selectedHour + selectedDuration > 22) {
            errorLabel.setText("No se pueden crear eventos después de las 10:00 PM");
            return;

        }

        if (!Calendar.checkCalendar(selectedDay,  (int) Math.floor(selectedHour * 2 - 10), selectedDuration * 2)) {
            errorLabel.setText("Ya hay un evento o materia en ese horario");
            return;
        }

        String date = selectedDay + " " + selectedHour + " " + selectedDuration;

        Event event = new Event(DataBase.selectEventCode(), title.getText(), info.getText(), date, colorToString(eventColor.getValue()), colorToString(textColor.getValue()));
        DataBase.addEvent(event);

        Calendar.updateWeek();

        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void updateColors() {
        eventNameLabel.setBackground(new Background(new BackgroundFill(eventColor.getValue(), null, null)));
        eventNameLabel.setTextFill(textColor.getValue());
    }

    private String colorToString(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        double a = color.getOpacity();

        return String.format("rgba(%d, %d, %d, %.2f)", r, g, b, a);
    }

}
