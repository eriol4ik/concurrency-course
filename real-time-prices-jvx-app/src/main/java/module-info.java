module org.github.vad.section7 {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens org.github.vad.section7 to javafx.fxml;
    exports org.github.vad.section7;
}
