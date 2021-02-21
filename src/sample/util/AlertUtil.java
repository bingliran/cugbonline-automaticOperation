package sample.util;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.Main;
import sample.controller.AlertController;
import sample.entity.AlertInit;
import sample.entity.Constant;
import sample.entity.EventHandlerByAlert;

import java.io.IOException;

public class AlertUtil {

    public static AlertController getAlert(String title, String message) {
        return getAlert(title, message, null, null, null);
    }

    public static AlertController getAlert(String title, String message, Image icon) {
        return getAlert(icon, title, message, null, null, null, null, null);
    }


    public static AlertController getAlert(String title, String body, EventHandlerByAlert trueEventHandler, EventHandlerByAlert falseEventHandler, AlertInit init) {
        return getAlert(Constant.DEFAULT_IMAGE, title, body, trueEventHandler, falseEventHandler, null, null, init);
    }

    public static AlertController getAlert(Image icon, String title, String body, EventHandlerByAlert trueEventHandler, EventHandlerByAlert falseEventHandler, AlertInit init) {
        return getAlert(icon, title, body, trueEventHandler, falseEventHandler, null, null, init);
    }

    public static AlertController getAlert(Image icon, String title, String body, EventHandlerByAlert trueEventHandler) {
        return getAlert(icon, title, body, trueEventHandler, null, null);
    }

    public static AlertController getAbilityModifyAlert(Image icon, String title, String body, EventHandlerByAlert trueEventHandler) {
        return getAlert(icon, title, body, trueEventHandler, null, (alertController) -> alertController.getBody().setEditable(true));
    }

    /**
     * @param icon              弹窗图标
     * @param title             标题
     * @param body              主要内容
     * @param trueEventHandler  点击确定事件
     * @param falseEventHandler 点击取消事件
     * @param trueTitle         正确的文本描述
     * @param falseTitle        错误的文本描述
     * @param init              初始化时执行方法
     * @return 弹窗控制器
     */
    public static AlertController getAlert(Image icon, String title, String body, EventHandlerByAlert trueEventHandler, EventHandlerByAlert falseEventHandler, String trueTitle, String falseTitle, AlertInit init) {
        try {
            AlertController alertController = Main.initStage(new Stage(), AlertUtil.class.getResource("../res/alert.fxml"));
            alertController.getBody().setText(body);
            alertController.getBody().setWrapText(true);
            alertController.getBody().setEditable(false);
            alertController.getTitle().setText(title);
            alertController.getIcon().setImage(icon);
            if (trueTitle != null) alertController.getT().setText(trueTitle);
            if (falseTitle != null) alertController.getF().setText(falseTitle);
            if (trueEventHandler != null)
                alertController.getT().setOnAction((event -> trueEventHandler.onClick(alertController)));
            if (falseEventHandler != null)
                alertController.getF().setOnAction((event -> falseEventHandler.onClick(alertController)));
            if (init != null) init.init(alertController);
            alertController.show();
            return alertController;
        } catch (IOException e) {
            throw new NullPointerException(e.getMessage());
        }
    }
}