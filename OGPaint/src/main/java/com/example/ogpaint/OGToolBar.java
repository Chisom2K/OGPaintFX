package com.example.ogpaint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class OGToolBar extends ToolBar {
    private final static String[] DRAW_TOOLS = {"Freehand"};
    private final static Integer[] LINE_WIDTH_VALS = {1,2,5,10,15,20,25,50,100};

    private final Button RND_COLOR_BUTTON;
    private final Button RNBW_FUN_BUTTON;

    private static ComboBox<String> toolsBox;
    private static ComboBox<Integer> widthsBox;
    private static ColorPicker fillColorPicker;
    private static ColorPicker lineColorPicker;
    private static TextField numSides;
    private static Label zoomLabel;
    private static CheckBox setFill;
    private Random rnd;
    private static int usingWidth;
    private static int usingTool;
    private static int usingNumSides;



    public OGToolBar() {
        super();
        //declaring all objects & values
        usingWidth = 1;
        usingTool = 0;
        usingNumSides = 3;


        RND_COLOR_BUTTON = new Button("");
        RNBW_FUN_BUTTON = new Button("");

        toolsBox = new ComboBox<>(FXCollections.observableArrayList(DRAW_TOOLS));
        widthsBox = new ComboBox<>(FXCollections.observableArrayList(LINE_WIDTH_VALS));
        zoomLabel = new Label("100%");
        fillColorPicker = new ColorPicker();
        lineColorPicker = new ColorPicker();
        setFill = new CheckBox();
        rnd = new Random();

        getItems().addAll(new Label(" Tools: "), toolsBox, new Separator(),
                new Label(" Line Color: "), lineColorPicker, new Label(" Fill Color: "),
                fillColorPicker, RND_COLOR_BUTTON, new Separator(), RNBW_FUN_BUTTON,
                new Separator(), new Label("Zoom: "), zoomLabel,new Separator(), new Label(" Line Width: "), widthsBox, new Label(" Fill "),
                setFill, new Separator());

        //setting the default values for things that look bad w/o them
        lineColorPicker.setValue(Color.BLACK);
        fillColorPicker.setValue(Color.WHITE);
        toolsBox.setValue("None");

        widthsBox.setEditable(true);
        widthsBox.setPrefWidth(90);
        widthsBox.setValue(1);


        RND_COLOR_BUTTON.setTooltip(new Tooltip("Random Fill and Line Color"));
        RNBW_FUN_BUTTON.setTooltip(new Tooltip("Rainbow Fun Mode"));

        try {
            //setting graphics on buttons
            int size = 21;
        RND_COLOR_BUTTON.setGraphic(new ImageView(new Image(new FileInputStream(OGPaint.IMAGE_FOLDER + "random.png"), size, size, true, true)));
        RNBW_FUN_BUTTON.setGraphic(new ImageView(new Image(new FileInputStream(OGPaint.IMAGE_FOLDER + "rainbow.png"), size, size, true, true)));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OGToolBar.class.getName()).log(Level.SEVERE, null, ex);
        }

        widthsBox.setOnAction((ActionEvent e) -> {   //changes the value of usingWidth when the ComboBox is used/value changes
            usingWidth = widthsBox.getValue();
        });

        RND_COLOR_BUTTON.setOnAction((ActionEvent e) -> { //changes the colors in the fill and line color pickers to the same rand color
            double[] rgb = {rnd.nextDouble(),rnd.nextDouble(),rnd.nextDouble()};    //gets rand vals for RGB from 0 to 1
            Color randCol = Color.color(rgb[0],rgb[1],rgb[2]);
            lineColorPicker.setValue(randCol);
            fillColorPicker.setValue(randCol);
        });
        //mode buttons
        RNBW_FUN_BUTTON.setOnAction((ActionEvent e) -> {
            WritableImage wi = new WritableImage(
                    (int) OGPaint.getCurrentTab().getCanvasWidth(),
                    (int) OGPaint.getCurrentTab().getCanvasHeight());
            PixelWriter pw = wi.getPixelWriter();
            for(int y = 0; y < wi.getHeight(); y++)
                for(int x = 0; x < wi.getWidth(); x++)
                    pw.setColor(x, y, Color.color(rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble()));
            OGPaint.getCurrentTab().drawImageAt(wi, 0, 0);

        });

    }

    /**
     * Sets the zoom label to the specified zoom value
     * @param zoomVal A double representing the zoom value (i.e. the scale of the canvas)
     */
    public static void setZoomLabel(double zoomVal){ zoomLabel.setText(String.format("%.1f", zoomVal * 100) + "%"); }
    public static String getCurrentTool(){ return DRAW_TOOLS[usingTool]; }
    /**
     * Gets the color from the line color picker
     * @return The Color object representing the color in the picker
     */
    public static Color getLineColor(){ return lineColorPicker.getValue(); }
    /**
     * Gets the color from the fill color picker
     * @return The Color object representing the color in the picker
     */
    public static Color getFillColor(){ return fillColorPicker.getValue(); }
    /**
     * Sets the color of the line color picker
     * @param color The color to set the line color picker to
     */
    public static void setLineColor(Color color){ lineColorPicker.setValue(color); }
    /**
     * Sets the color of the fill color picker
     * @param color The color to set the fill color picker to
     */
    public static void setFillColor(Color color){ fillColorPicker.setValue(color); }
    /**
     * Gets the line width from the editable combo box
     * @return The integer representing the width
     */
    public static int getLineWidth(){ return usingWidth; }
    /**
     * Gets the status of the fill check box
     * @return The boolean value representative of whether the check box is checked
     */
    public static boolean getFillStatus(){ return setFill.isSelected();}


}
