package memoryCard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.Parent;

/**
 * Juego de Memoria. Clase lanzadora
 * @author Javier Lesaca Medina
 */

public class Principal extends Application {
    @Override
    public void start(Stage stage) throws IOException {
       
        Parent root = FXMLLoader.load(getClass().getResource("/memoryCard/memoria.fxml"));
        Scene scene = new Scene(root);
        
        // Agregar la hoja de estilos CSS
        scene.getStylesheets().add(getClass().getResource("/memoryCard/estilos.css").toExternalForm());
        


        
        stage.setTitle("Memory GAME!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    
    }

    public static void main(String[] args) {
        
        //com.sun.javafx.application.PlatformImpl.startup((()->{}));
        
        launch(args);
    }
}