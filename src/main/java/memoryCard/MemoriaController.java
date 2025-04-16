package memoryCard;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


/**
 * Clase Controladora
 *
 * @author Javier Lesaca Medina
 */
public class MemoriaController implements Initializable {

    // definición de variables internas para el desarrollo del juego
    private JuegoMemoria juego;         // instancia que controlará el estado del juego (tablero, parejas descubiertas, etc)
    private ArrayList<Button> cartas;   // array para almacenar referencias a las cartas @FXML definidas en la interfaz 
    private int segundos = 0;             // tiempo de juego
    private boolean primerBotonPulsado = false, segundoBotonPulsado = false; // indica si se han pulsado ya los dos botones para mostrar la pareja
    private int idBoton1, idBoton2;     // identificadores de los botones pulsados
    private boolean esPareja;           // almacenará si un par de botones pulsados descubren una pareja o no
    private int intento = 0;

    @FXML
    private AnchorPane main; // panel principal (incluye la notación @FXML porque hace referencia a un elemento de la interfaz)
    @FXML
    private GridPane tablero;
    @FXML
    private Label intentos, tiempo;// etiquetas muestran intentos y tiempo
    @FXML
    private Button c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16;
    @FXML
    private Button comenzar;
    @FXML
    private Button salir;
    @FXML
    private ImageView imagenFinal; // ImageView para mostrar la imagen final

    // Crea un objeto Media 
    private MediaPlayer reproductor;

    // linea de tiempo para gestionar la finalización del intento al pasar 1.5 segundos
    private final Timeline finIntento = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> finalizarIntento()));

    // linea de tiempo para gestionar el contador de tiempo del juego
    private Timeline contadorTiempo;

    //private MediaPlayer mediaPlayer;
    /**
     * Método interno que configura todos los aspectos necesarios para
     * inicializar el juego.
     *
     * @param url No utilizaremos este parámetro (null).
     * @param resourceBundle No utilizaremos este parámetro (null).
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        juego = new JuegoMemoria(); // instanciación del juego (esta instancia gestionará el estado de juego)
        juego.iniciarJuego();       // comienzo de una nueva partida
        cartas = new ArrayList<>(); // inicialización del ArrayList de referencias a cartas @FXML

        // guarda en el ArrayList "cartas" todas las referencias @FXML a las cartas para gestionarlo cómodamente
        cartas.addAll(Arrays.asList(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16));

        // Inicialización de todos los aspectos necesarios
        intentos.setText("0"); //pongo el campo de los intentos a 0
        tiempo.setText("0"); ////pongo el campo del tiempo a 0

        // Inicialización del contador de tiempo de la partida
        contadorTiempo = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            // Incrementa los segundos
            segundos++;
            // Actualiza la etiqueta de tiempo para mostrar los segundos transcurridos
            tiempo.setText(Integer.toString(segundos));

        }));
        contadorTiempo.setCycleCount(Timeline.INDEFINITE);  // reproducción infinita
        contadorTiempo.playFromStart();// iniciar el contador en este momento

        // metodo que inicia música de fondo para que se reproduzca cuando se inicia el juego
        reproducirMusica();

    }

    /**
     * Acción asociada al botón <strong>Comenzar nuevo juego</strong> que
     * permite comenzar una nueva partida.
     *
     * Incluye la notación @FXML porque será accesible desde la interfaz de
     * usuario
     *
     * @param event Evento que ha provocado la llamada a este método
     */
    @FXML
    private void reiniciarJuego(ActionEvent event) {

        // detener el contador de tiempo 
        contadorTiempo.stop();
        
        //detener la reproducción de la música de fondo
        detenerMusica();

        //ocultar la imagenfinal
        imagenFinal.setVisible(false);

        // Restablecer los valores de la partida
        juego.iniciarJuego();

        /* hacer visibles las 16 cartas de juego ya que es posible que no todas estén visibles 
           si se encontraron parejas en la partida anterior */
        for (Button carta : cartas) {
            carta.setVisible(true);
            carta.setDisable(false); // Habilitar las cartas
            carta.setGraphic(null);
        }

        // Reiniciar el tiempo transcurrido
        segundos = 0;

        tiempo.setText("0");

        // Reiniciar el número de intentos
        intento = 0;
        
        intentos.setText("0");


        // llamar al método initialize para terminar de configurar la nueva partida
        initialize(null, null);
    }

    /**
     * Este método deberá llamarse cuando el jugador haga clic en cualquiera de
     * las cartas del tablero.
     *
     * Incluye la notación @FXML porque será accesible desde la interfaz de
     * usuario
     *
     * @param event Evento que ha provocado la llamada a este método (carta que
     * se ha pulsado)
     */
    @FXML
    private void mostrarContenidoCasilla(ActionEvent event) {

        // Obtener la carta pulsada
        Button carta = (Button) event.getSource();

        int indiceCarta = cartas.indexOf(carta); // Obtener el índice de la carta en el ArrayList

        String contenidoCarta = juego.getCartaPosicion(indiceCarta);// Obtener el contenido de la carta en el tablero

        // Gestionar correctamente la pulsación de las cartas
        if (!primerBotonPulsado) {
            primerBotonPulsado = true;
            idBoton1 = indiceCarta;
            // la primera carta pulsada se desactivará para eventos de ratón y no se podrá pulsar durante este intento
            carta.setMouseTransparent(true);

        } else if (!segundoBotonPulsado) {
            //finIntento.play();
            segundoBotonPulsado = true;
            idBoton2 = indiceCarta;
            // Desactivará para eventos de ratón para todas las cartas para evitar que se puedan pulsar
            for (Button cartaActual : cartas) {
                cartaActual.setMouseTransparent(true);
            }
            // Llamar al método para finalizar el intento
            finIntento.play();

        }

        // Descubrir la imagen asociada a la carta
        String rutaImagen = "/memoryCard/assets/cartas/" + contenidoCarta + ".png"; // Ruta de la imagen
        Image imagen = new Image(getClass().getResourceAsStream(rutaImagen));
        ImageView imageView = new ImageView(imagen);
        imageView.setFitWidth(carta.getWidth());
        imageView.setFitHeight(carta.getHeight());
        carta.setGraphic(imageView);

    }

    /**
     * Este método permite finalizar un intento realizado. Se pueden dar dos
     * situaciones:
     * <ul>
     * <li>Si se ha descubierto una pareja: en este caso se ocultarán las cartas
     * desapareciendo del tablero. Además, se debe comprobar si la pareja
     * descubierta es la última pareja del tablero y en ese caso terminar la
     * partida.</li>
     * <li>Si NO se ha descubierto una pareja: las imágenes de las cartas deben
     * volver a ocultarse (colocando su imagen a null).</li>
     * </ul>
     * Este método será interno y NO se podrá acceder desde la interfaz, por lo
     * que NO incorpora notación @FXML.
     */
    private void finalizarIntento() {

        // Verificar si se ha descubierto una pareja
        esPareja = juego.compruebaJugada(idBoton1, idBoton2);

        // Incrementar el contador de intentos
        intento++;
        intentos.setText(String.valueOf(intento));

        // Si se ha descubierto una pareja
        if (esPareja) {
            // Desactivar las cartas y ocultarlas del tablero
            cartas.get(idBoton1).setDisable(true);
            cartas.get(idBoton2).setDisable(true);
            cartas.get(idBoton1).setVisible(false);
            cartas.get(idBoton2).setVisible(false);
            // Reproducir efecto de sonido de pareja encontrada
            reproducirSonido("exito.mp3");
        } else {
            // Si NO se ha descubierto una pareja, restablecer el contenido de las cartas
            cartas.get(idBoton1).setDisable(false);
            cartas.get(idBoton2).setDisable(false);
            cartas.get(idBoton2).setGraphic(null);
            cartas.get(idBoton1).setGraphic(null);
            // Reproducir efecto de sonido de pareja no encontrada
            reproducirSonido("fallo.mp3");
        }

        // Habilitar todas las cartas para que se puedan pulsar
        for (Button cartaActual : cartas) {
            cartaActual.setMouseTransparent(false);
        }

        // Reiniciar las variables para el próximo intento
        primerBotonPulsado = false;
        segundoBotonPulsado = false;

        // Verificar si el juego ha finalizado
        if (juego.compruebaFin()) {
            // Detener el contador de tiempo
            contadorTiempo.stop();

            // Reproducir música o efecto de sonido de victoria
            reproducirSonido("victoria.mp3");

            // Mostrar imagen de victoria
            String rutaImagenVictoria = "/memoryCard/assets/interfaz/victoria.png"; // Ruta de la imagen
            Image imagenVictoria = new Image(getClass().getResourceAsStream(rutaImagenVictoria));
            imagenFinal.setImage(imagenVictoria);
            imagenFinal.setVisible(true);

            // Ajustar el tamaño de la imagen final para que coincida con el tamaño 
            imagenFinal.setFitWidth(main.getWidth());
            imagenFinal.setFitHeight(main.getHeight());
        }
    }

    /**
     * Este método permite salir de la aplicación. Debe mostrar una alerta de
     * confirmación que permita confirmar o rechazar la acción Al confirmar la
     * acción la aplicación se cerrará (opcionalmente, se puede incluir algún
     * efecto de despedida) Incluye la notación @FXML porque será accesible
     * desde la interfaz de usuario
     */
    @FXML
    private void salir() {

        // Alerta de confirmación que permita elegir si se desea salir o no del juego
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Confirmación");
        alert.setContentText("¿Seguro que quieres salir?");
        alert.showAndWait().ifPresent(response -> {
            // SOLO si se confirma la acción se cerrará la ventana y el juego finalizará. 
            if (response == ButtonType.OK) {
                Platform.exit();
            }
        });
    }

    private void reproducirMusica() {
        URL resource = getClass().getResource("/memoryCard/assets/sonidos/fondo.mp3");
        if (resource != null) {
            Media media = new Media(resource.toString());
            reproductor = new MediaPlayer(media);
            reproductor.setCycleCount(MediaPlayer.INDEFINITE);
            reproductor.play();
        }else{
            System.out.println("Archivo de musica no encontrado.");
        }
    }

    // Método para detener la música
    private void detenerMusica() {
        if (reproductor != null) {
            reproductor.stop();
        }
    }

    //Metodo para reproducir sonidos 
    private void reproducirSonido(String nombreArchivo) {
        URL resource = getClass().getResource("/memoryCard/assets/sonidos/" + nombreArchivo);
        if (resource != null) {
            Media media = new Media(resource.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } else {
            System.out.println("Archivo de sonido no encontrado: " + nombreArchivo);
        }
    }
}
