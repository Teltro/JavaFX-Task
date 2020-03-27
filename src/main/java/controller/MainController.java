package controller;

import core.Main;
import db.IPersonDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.PersonDTO;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainController implements Initializable {
    @FXML
    private TreeTableView<PersonDTO> personTTV;
    @FXML
    private TreeTableColumn<PersonDTO, String> nameColumn;
    @FXML
    private TreeTableColumn<PersonDTO, Number> ageColumn;
    @FXML
    private TreeTableColumn<PersonDTO, LocalDate> birthdayColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private IPersonDAO personDAO;
    private TreeItem<PersonDTO> selectedItem;

    public MainController(IPersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        personTTV.setRowFactory(param -> {
            TreeTableRow<PersonDTO> row = new TreeTableRow<PersonDTO>();
            // предупреждение, что у Person сегодня день рождения
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    if (selectedItem.getValue().getId() > 0 && (selectedItem.getValue().getBirthday().equals(LocalDate.now()))) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Info");
                        alert.setHeaderText(null);
                        alert.setContentText("Today is " + selectedItem.getValue().getName() + "'s birthday");
                        alert.showAndWait();
                    }
                }
            });
            return row;
        });

        // инициализация данных
        TTVInit();

        personTTV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // корневой элемент имеет id == 0
            if (newValue != null && newValue.getValue().getId() > 0)
                selectedItem = newValue;
            else
                selectedItem = null;
        });
    }

    @FXML
    private void addButtonClick(ActionEvent event) {
        AddController controller = new AddController();
        controller.registerSaveButtonCallBack(person -> {
            personDAO.add(person);
            int maxId = 0;
            for (TreeItem<PersonDTO> treeItem : personTTV.getRoot().getChildren()) {
                if (treeItem.getValue().getId() > maxId)
                    maxId = treeItem.getValue().getId();
            }
            TTVLoadNewDate(maxId);
            sort();
        });
        callManagePersonWindow(controller, "Adding");
    }

    @FXML
    private void editButtonClick(ActionEvent event) {
        if (selectedItem == null)
            callSelectedItemIsNUllDialog();
        else {
            EditController editController = new EditController(selectedItem.getValue());
            editController.registerSaveButtonCallBack(person -> {
                personDAO.update(person);
                sort();
            });
            callManagePersonWindow(editController, "Editing");
        }
    }

    // вызов окна для добавления/изменения Person
    private void callManagePersonWindow(PersonManageController controller, String windowTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/PersonManageWindow.fxml"));
            loader.setController(controller);
            Parent root = loader.load();
            Stage addWindowStage = new Stage();
            addWindowStage.setTitle(windowTitle);

            addWindowStage.initOwner(Main.getPrimaryStage());
            addWindowStage.setScene(new Scene(root));
            addWindowStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sort() {
        personTTV.getRoot().getChildren().sort(Comparator.comparing(p -> p.getValue().getName().toLowerCase()));
    }

    private void callSelectedItemIsNUllDialog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("You must select a person");
        alert.showAndWait();
    }

    @FXML
    private void deleteButtonClick(ActionEvent event) {
        if(selectedItem == null) {
            callSelectedItemIsNUllDialog();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Delete Person");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to remove " + selectedItem.getValue().getName());

        Optional<ButtonType> optional = alert.showAndWait();
        if (optional.get() == ButtonType.CANCEL) {
            alert.close();
            return;
        }

        PersonDTO personToDelete = selectedItem.getValue();
        personTTV.getRoot().getChildren().remove(selectedItem);
        personDAO.delete(personToDelete.getId());

        selectedItem = null;
        personTTV.getSelectionModel().clearSelection(personTTV.getSelectionModel().getSelectedIndex());
    }

    // обновление всех данных (пока не используется)
    private void TTVDataReload() {
        personTTV.getRoot().getChildren().clear();
        ArrayList<TreeItem<PersonDTO>> treeItems = new ArrayList<TreeItem<PersonDTO>>();
        personDAO.getList().forEach(person -> {
            treeItems.add(new TreeItem<PersonDTO>(new PersonDTO(person)));
        });
        personTTV.getRoot().getChildren().addAll(treeItems);
    }

    // загрузка новых данных
    // метод получает максимальный id из элементов TreeTableView
    // и загружает из бд данные с большим значением id
    private void TTVLoadNewDate(int id) {
        ArrayList<TreeItem<PersonDTO>> treeItems = new ArrayList<>();
        personDAO.getItemsAfterId(id).forEach(person -> {
            treeItems.add(new TreeItem<PersonDTO>(new PersonDTO(person)));
        });
        personTTV.getRoot().getChildren().addAll(treeItems);
    }

    private void TTVInit() {
        ArrayList<TreeItem<PersonDTO>> treeItems = new ArrayList<TreeItem<PersonDTO>>();
        TreeItem<PersonDTO> root =
                new TreeItem<>(new PersonDTO(0, "Name", 11, LocalDate.of(1111, 11, 11)));
        personDAO.getList().forEach(person -> {
            treeItems.add(new TreeItem<PersonDTO>(new PersonDTO(person)));
        });
        root.getChildren().setAll(treeItems);

        nameColumn.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
        ageColumn.setCellValueFactory(param -> param.getValue().getValue().ageProperty());
        birthdayColumn.setCellValueFactory(param -> param.getValue().getValue().birthdayProperty());
        // установка формата данных для birthdayColumn
        birthdayColumn.setCellFactory(new Callback<TreeTableColumn<PersonDTO, LocalDate>, TreeTableCell<PersonDTO, LocalDate>>() {
            @Override
            public TreeTableCell<PersonDTO, LocalDate> call(TreeTableColumn<PersonDTO, LocalDate> param) {
                return new TreeTableCell<PersonDTO, LocalDate>() {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

                    @Override
                    protected void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty)
                            setText(null);
                        else
                            setText(formatter.format(item));
                    }
                };
            }
        });

        personTTV.setRoot(root);
        sort();
    }
}
