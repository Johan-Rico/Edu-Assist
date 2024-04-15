package com.eduassist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;

import java.io.IOException;

public class LogIn {

    public LogIn() {

    }

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorLabel;


    public void initialize() {
        username.requestFocus();
    }

        public void userLogIn(ActionEvent event) throws IOException {
        checkLogin();
    }

    private void checkLogin() throws IOException {
        Main m = new Main();
        if (username.getText().equals("j") && password.getText().equals("123")) {
            errorLabel.setVisible(false);
            m.changeScene("Calendar.fxml", "Inicio");
        }
        else if (username.getText().isEmpty() || password.getText().isEmpty()) {
            errorLabel.setText("Ingrese su usuario y contraseña");
        } else {
            errorLabel.setText("Usuario o contraseña incorrectos");
            username.setText("");
            password.setText("");
        }
    }
}

