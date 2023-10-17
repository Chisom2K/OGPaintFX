package com.example.ogpaint;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Chisom Ogbuefi
 */

public class OGPaint extends Application {
    //"/Users/chisomogbuefi/Documents/CS250 paint/OGPaint/Images ";
    public final static String IMAGE_FOLDER ="OGPaint/Images";
            //"/Users/chisomogbuefi/Documents/CS250\\ paint/OGPaint/Images\\";

    private final static String TITLE = "OGPaint";
    private final static String VER_NUM = "1.0";
    private final static int WINDOW_LENGTH = 1700;
    private final static int WINDOW_HEIGHT = 1000;

    public static Stage mainStage;
    public static TabPane tabpane;
    public static OGToolBar toolbar;
    public static OGMenuBar menubar ;
    @Override
    public void start(Stage primeStage) {
        OGPaint.mainStage = primeStage;   //associates the stage being shown with the public mainStage
        //layout objects
        tabpane = new TabPane();
        toolbar = new OGToolBar();
        menubar = new OGMenuBar();
        BorderPane bp = new BorderPane();
        VBox topbar = new VBox(menubar, toolbar);
        Scene scene = new Scene(bp, WINDOW_LENGTH, WINDOW_HEIGHT);

        mainStage.setOnCloseRequest((WindowEvent w) -> {
            w.consume();
            OGPopUp.createExitAlert(mainStage);
        });
        //Setting up layout
        bp.setTop(topbar);
        bp.setCenter(tabpane);

        tabpane.getTabs().add(new OGTab());
        tabpane.getSelectionModel().selectFirst();

        primeStage.setMaximized(true);
        primeStage.setTitle(TITLE + " - " + VER_NUM);
        primeStage.setScene(scene);
        primeStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static OGTab getCurrentTab(){return (OGTab)tabpane.getSelectionModel().getSelectedItem();}
    /**
     * Removes the current tab in use
     */
    public static void removeCurrentTab(){OGPaint.tabpane.getTabs().remove(OGPaint.getCurrentTab());}
}