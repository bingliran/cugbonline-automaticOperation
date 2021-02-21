package sample.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import sample.config.CourseListCallback;
import sample.entity.Course;

import java.util.Deque;

public class CourseController extends BaseController {

    @FXML
    private ListView<Course> listView;
    @FXML
    private Label title;

    public void initialize() {
        listView.setCellFactory(new CourseListCallback());
    }

    @Override
    public void windowCloseRequest() {
        exit();
    }

    @Override
    public void min() {
        getStage().setIconified(true);
    }

    @Override
    public void exit() {
        getStage().close();
    }

    public void setCourseList(Deque<Course> deque, String title) {
        this.title.setText(title + "(课程列表)");
        ObservableList<Course> observableList = listView.getItems();
        observableList.clear();
        Course course;
        while ((course = deque.pollLast()) != null) observableList.add(course);

    }
}
