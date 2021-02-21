package sample.controller;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseController {
    protected Stage stage;

    public void show() {
        getStage().show();
    }

    public void showAndWait() {
        getStage().showAndWait();
    }

    public abstract void windowCloseRequest();//Stage被关闭

    public abstract void min();//最小化,

    public abstract void exit();//关闭
}
