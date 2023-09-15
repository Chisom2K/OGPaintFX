module com.example.ogpaint {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.logging;
    requires javafx.swing;


    opens com.example.ogpaint to javafx.fxml;
    exports com.example.ogpaint;
}