package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import model.Person;

public class AddController extends PersonManageController {

    @FXML
    @Override
    protected void saveButtonClick(ActionEvent event) {
        if(formIsEmpty()) {
            callNullFieldsWindow();
            return;
        }
        Person person = getPersonFromForm();
        this.saveButtonCallBack.callingBack(person);

        ((Stage)saveButton.getScene().getWindow()).close();
    }
}
