package controller;

import db.IPersonDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.DateStringConverter;
import model.Person;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.ResourceBundle;

// контроллер для реализации окна добавления/изменения данных о Person,
// содержит общий функционал для AddController и EditController
abstract public class PersonManageController implements Initializable {

    interface SaveButtonCallBack {
        void callingBack(Person person);
    }
    protected SaveButtonCallBack saveButtonCallBack;

    @FXML
    protected TextField nameTextField;
    @FXML
    protected Text ageValueText;
    @FXML
    protected DatePicker birthdayDatePicker;
    @FXML
    protected Button saveButton;
    @FXML
    protected Button cancelButton;

    protected IPersonDAO personDAO;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        if(nameTextField.getText().length() == 0)
            nameTextField.setStyle("-fx-background-color: FF9999");
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() == 0)
                nameTextField.setStyle("-fx-background-color: FF9999");
            else
                nameTextField.setStyle(null);
        });
        ageValueText.setText(null);
        birthdayDatePicker.getEditor().setTextFormatter(datePickerMask());
        birthdayDatePicker.getEditor().setOnMouseClicked(event ->
                birthdayDatePicker.getEditor().selectAll());
        birthdayDatePicker.setValue(LocalDate.now());
    }

    @FXML
    abstract protected void saveButtonClick(ActionEvent event);

    @FXML
    protected void cancelButtonClick(ActionEvent event) {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    public void registerSaveButtonCallBack(SaveButtonCallBack callback) {
        saveButtonCallBack = callback;
    }

    protected Person getPersonFromForm() {
        Person person = new Person();
        person.setName(nameTextField.getText());
        person.setAge(Integer.parseInt(ageValueText.getText()));
        person.setBirthday(birthdayDatePicker.getValue());
        return person;
    }

    protected void callNullFieldsWindow() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("You must fill all fields");
        alert.showAndWait();
    }

    protected boolean formIsEmpty() {
        return (nameTextField.getText().length() == 0
                || ageValueText == null
                || birthdayDatePicker == null);
    }

    private int defineAge(LocalDate birthday) {
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    // маска(ну почти) для ввода даты
    private TextFormatter<Date> datePickerMask() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateStringConverter converter = new DateStringConverter(dateFormat);
        return new TextFormatter<Date>(converter, null, c -> {
            if (c.isContentChange()) {
                if(c.getControlNewText().length() > 10)
                    return null;
                c.getControl().setStyle(null);
                try {
                    LocalDate date = LocalDate.parse(c.getControlNewText(), dateFormatter);
                    birthdayDatePicker.setValue(date);
                    ageValueText.setText((Integer.toString(defineAge(birthdayDatePicker.getValue()))));
                } catch (DateTimeParseException e) {
                    c.getControl().setStyle("-fx-background-color: FF9999");
                    ageValueText.setText(null);
                }
            }
            if (c.isAdded()) {
                if (c.getControlNewText().length() > 10)
                    return null;

                int caretPosition = c.getCaretPosition();
                if (caretPosition == 2 || caretPosition == 5) {
                    c.setText(c.getText() + ".");
                    c.setCaretPosition(c.getControlNewText().length());
                    c.setAnchor(c.getControlNewText().length());
                }
            }
            return c;
        });
    }
}
