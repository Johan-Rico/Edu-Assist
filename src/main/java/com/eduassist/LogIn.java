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
        String user = username.getText();
        String pass = password.getText();

        if (DataBase.checkUserData(user, pass)) {
            errorLabel.setVisible(false);
            DataBase.setData(user);
            m.changeScene("Calendar.fxml", "Edu-Assist");

        }
        else if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Ingrese su usuario y contraseña");
        } else {
            errorLabel.setText("Usuario o contraseña incorrectos");
            username.setText("");
            password.setText("");
        }
    }
}

