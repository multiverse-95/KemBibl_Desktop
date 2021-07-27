package kemBibl;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("app.fxml"));
        primaryStage.setTitle("КемБибл");
        primaryStage.setScene(new Scene(root, 931, 696));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
                ButtonType yes_del = new ButtonType("Да", ButtonBar.ButtonData.OK_DONE); // Создание кнопки подтвердить
                ButtonType no_del = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE); // Создание кнопки отменить
                Alert alert =new Alert(Alert.AlertType.CONFIRMATION , "Test", yes_del, no_del);
                alert.setTitle("Выход из приложения"); // Название предупреждения
                alert.setHeaderText("Подтвердите выход из приложения!"); // Текст предупреждения
                alert.setContentText("Вы действительно хотите выйти из приложения?");
                // Вызов подтверждения элемента
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == yes_del){
                        System.out.println("Exit!");
                        Platform.exit();
                        System.exit(0);
                    } else if(rs ==no_del){
                        we.consume();
                    }
                });
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
