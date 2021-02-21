package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.config.DragWindowHandler;
import sample.controller.BaseController;

import java.io.IOException;
import java.net.URL;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseController> T initStage(Stage primaryStage, URL fxml) throws IOException {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader(fxml);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        DragWindowHandler handler = new DragWindowHandler(primaryStage);
        scene.setOnMousePressed(handler);
        scene.setOnMouseDragged(handler);
        BaseController baseController = fxmlLoader.getController();
        baseController.setStage(primaryStage);
        primaryStage.setOnCloseRequest(e -> baseController.windowCloseRequest());
        Button exit = lookUp(root, "#exit");
        if (exit != null) exit.setOnAction(event -> baseController.exit());
        Button min = lookUp(root, "#min");
        if (min != null) min.setOnAction(event -> baseController.min());
        return (T) baseController;
    }

    public static <T extends BaseController> T initStageAndShow(Stage primaryStage, URL fxml) throws IOException {
        T controller = initStage(primaryStage, fxml);
        controller.show();
        return controller;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Control> T lookUp(Parent parent, String selector) {
        return (T) parent.lookup(selector);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initStageAndShow(primaryStage, getClass().getResource("sample.fxml"));
    }

}
