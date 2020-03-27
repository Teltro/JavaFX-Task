package core;

import controller.MainController;
import db.IPersonDAO;
import db.PersonDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // для доступа из вне к основному окну
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Main.fxml"));
        IPersonDAO personDAO = new PersonDAO();
        primaryStage.setResizable(false);
        MainController mainController = new MainController(personDAO);
        loader.setController(mainController);
        Parent root = loader.load();
        primaryStage.setTitle("Task App");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        Main.primaryStage = primaryStage;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}