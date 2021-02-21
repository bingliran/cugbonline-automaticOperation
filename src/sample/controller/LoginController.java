package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;
import sample.util.SessionUtil;

import java.io.IOException;

public class LoginController extends BaseController {
    @FXML
    private TextField Cookie;

    public void login() throws IOException {
        SessionUtil.setCookie(Cookie.getText().trim());
        Main.initStageAndShow(new Stage(), getClass().getResource("../res/menu.fxml"));
        exit();
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
}
