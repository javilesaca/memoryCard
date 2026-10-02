module memory.card {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens memoryCard to javafx.fxml;

    exports memoryCard;
}
