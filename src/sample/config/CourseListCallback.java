package sample.config;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import sample.controller.CourseItemController;
import sample.entity.Course;

import java.io.IOException;

public class CourseListCallback implements Callback<ListView<Course>, ListCell<Course>> {
    @Override
    public ListCell<Course> call(ListView<Course> listView) {
        return new ListCell<Course>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../res/courseItem.fxml"));
                        Parent root = fxmlLoader.load();
                        CourseItemController courseItemController = fxmlLoader.getController();
                        courseItemController.setCourse(item);
                        setGraphic(root);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (!empty) {
                    setText(null);
                    setGraphic(null);
                }
            }
        };
    }
}