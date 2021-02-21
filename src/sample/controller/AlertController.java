package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import lombok.Getter;


@Getter
public class AlertController extends BaseController {
    @FXML
    private ImageView icon;
    @FXML
    private Label title;
    @FXML
    private TextArea body;
    @FXML
    private Button t;
    @FXML
    private Button f;

    @Override
    public void exit() {
        this.getStage().close();
    }

    public void initialize() {
        body.setWrapText(true);
    }

    @Override
    public void windowCloseRequest() {
        exit();
    }

    @Override
    public void min() {
        getStage().setIconified(true);
    }
}
