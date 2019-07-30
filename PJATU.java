package pjatu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PJATU extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent menu = FXMLLoader.load(getClass().getResource("MenuWindow.fxml"));
        Scene scene = new Scene(menu);
        stage.setScene(scene);
        stage.show();
    }
    
    
    
}
