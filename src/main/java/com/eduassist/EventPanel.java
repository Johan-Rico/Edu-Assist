package com.eduassist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EventPanel {

    @FXML
    private Accordion accordion;
    @FXML
    private TitledPane infoPane;
    @FXML
    private ColorPicker titleColor;
    @FXML
    private ColorPicker textColor;
    @FXML
    private Label eventNameLabel, infoLabel, errorLabel;
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

    private final String[] days = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
    private Event event;

    @FXML
    private void initialize() {

        accordion.setExpandedPane(infoPane);
        day.getItems().addAll(days);
        hour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12));
        duration.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, 17.0, 2.0, 0.5));
        minute.getItems().addAll("00", "30");
        period.getItems().addAll("AM", "PM");

        String style = "-fx-font: 14 system";
        day.setStyle(style);
        hour.setStyle(style);
        minute.setStyle(style);
        period.setStyle(style);
        duration.setStyle(style);

    }

    public void setInfo(Event event) {

        this.event = event;
        title.setText(event.getTitle());
        info.setText(event.getInfo());
        titleColor.setValue(stringToColor(event.getEventColor()));
        textColor.setValue(stringToColor(event.getTextColor()));
        updateInfo();
        updateColors();

        float[] date = event.getDate();
        day.setValue(days[(int) date[0]]);
        int hourValue = (int) date[1];

        if (hourValue >= 12) {
            period.setValue("PM");
        } else {
            period.setValue("AM");
        }

        if (hourValue > 12) {
            hourValue -= 12;
        }

        if ((date[1] * 2) % 2 == 0) {
            minute.setValue("00");
        } else {
            minute.setValue("30");
        }

        duration.getValueFactory().setValue((double) date[2] );
        hour.getValueFactory().setValue(hourValue);

    }

    @FXML
    private void changeColor(ActionEvent actionEvent) {
        event.setColors(colorToString(titleColor.getValue()), colorToString(textColor.getValue()));
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
        eventNameLabel.setBackground(new Background(new BackgroundFill(stringToColor(event.getEventColor()), null, null)));
        eventNameLabel.setTextFill(stringToColor(event.getTextColor()));
    }

    private void updateInfo() {
        eventNameLabel.setText(event.getTitle());
        infoLabel.setText(event.getInfo());
    }

    @FXML
    private void saveEvent(ActionEvent actionEvent) {

        int selectedDay = day.getSelectionModel().getSelectedIndex();
        float selectedHour = hour.getValue();
        Double selectedDuration = duration.getValue();
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

        if (!Calendar.checkCalendar(selectedDay,  (int) Math.floor(selectedHour * 2 - 10), selectedDuration * 2, event.getDate())) {
            errorLabel.setText("Ya hay un evento o materia en ese horario");
            return;
        }

        String date = selectedDay + " " + selectedHour + " " + selectedDuration;

        event.updateInfo(title.getText(), info.getText(), date);
        DataBase.updateEvent(event.getCode(), title.getText(), info.getText(), date);
        updateInfo();
        Calendar.updateWeek();

        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void deleteEvent(ActionEvent actionEvent) {

        DataBase.deleteEvent(event.getCode());
        Calendar.updateWeek();
        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();

    }
}