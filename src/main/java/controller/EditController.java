package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import model.Person;
import model.PersonDTO;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EditController extends PersonManageController {

    private PersonDTO personDTO;
    public EditController(PersonDTO personDTO) {
        this.personDTO = personDTO;
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        nameTextField.setText(personDTO.getName());
        ageValueText.setText(Integer.toString(personDTO.getAge()));
        birthdayDatePicker.setValue((LocalDate)personDTO.getBirthday());
    }

    @FXML
    @Override
    protected void saveButtonClick(ActionEvent event) {
        if(formIsEmpty()) {
            callNullFieldsWindow();
            return;
        }
        personDTO.setName(nameTextField.getText());
        personDTO.setAge(Integer.parseInt(ageValueText.getText()));
        personDTO.setBirthday(birthdayDatePicker.getValue());
        ((Stage)cancelButton.getScene().getWindow()).close();

        this.saveButtonCallBack.callingBack(new Person(personDTO));

        ((Stage)saveButton.getScene().getWindow()).close();
    }
}
