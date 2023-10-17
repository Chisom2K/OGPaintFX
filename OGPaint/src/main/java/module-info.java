module com.example.ogpaint {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.logging;
    requires javafx.swing;
    requires junit;


    opens com.example.ogpaint to javafx.fxml;
    exports com.example.ogpaint;
}