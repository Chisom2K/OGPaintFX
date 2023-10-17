package com.example.ogpaint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 * Extends Tab; makes the layout better and adds scrolling to canvases
 * @author Chisom Ogbuefi
 */
public class OGTab extends Tab {

    public final static String AUTOSAVE_DIR = System.getProperty("user.home") + File.separator + "OGPaint" + File.separator;
    public Pane canvasPane;   //static
    public final static String LOGS_PATH = "logs.txt";

    private final static int MILS_IN_SECS = 1000;
    private static FileChooser chooseFile;

    private int autosaveSecs;
    private double scale;
    private boolean unsavedChanges;
    private File path;
    private String title;
    private OGCanvas canvas;
    private Image autosaveBackup;
    private ScrollPane scroll;
    private StackPane canvasStack;
    private Timer autosaveTimer;
    private TimerTask autosave;
    private Timer loggerTimer;
    private TimerTask logger;

    public OGTab(){
        super();
        this.unsavedChanges = true;
        this.path = null;
        this.title = "Untitled";
        this.canvas = new OGCanvas();
        setup();
    }
    public OGTab(File file){
        super();
        this.unsavedChanges = false;
        this.path = file;
        this.title = path.getName();
        this.canvas = new OGCanvas();
        setup();
    }

    public void setup(){
        this.scale = 1;



        chooseFile = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.bmp");


        this.canvasPane = new Pane(canvas);
        this.canvasStack = new StackPane();
        this.canvasStack.getChildren().addAll(canvasPane);   //canvas -> canvasPane -> stackPane -> scroll
        this.scroll = new ScrollPane(this.canvasStack);

        this.setContent(scroll);
        this.setText((unsavedChanges ? "*" : "") + this.title);
        this.setOnCloseRequest((Event e) -> {
            e.consume();            //consumes the normal event call
            if(this.unsavedChanges) //if the canvas has unsaved changes, pull up the warning
                OGPopUp.createUnsavedAlert();
            else
                OGPaint.removeCurrentTab();
        });

        this.scroll.setPrefViewportWidth(this.canvas.getWidth()/2);
        this.scroll.setPrefViewportHeight(this.canvas.getHeight()/2);

        this.autosaveSecs = 30;
        this.autosaveTimer = new Timer();
        this.autosave = new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        autosaveImage();
                        autosaveTimer.schedule(autosave, 0, autosaveSecs*MILS_IN_SECS);
                    }
                });
            }
        };
        this.autosaveTimer.schedule(this.autosave, 30000, this.autosaveSecs*MILS_IN_SECS);

        this.loggerTimer = new Timer();
        this.logger = new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        File loggerFile = new File(LOGS_PATH);
                        try{
                            loggerFile.createNewFile();
                        } catch(Exception ex){
                            System.out.println(ex);
                        }
                        try{
                            FileWriter fw = new FileWriter(LOGS_PATH, true);
                            BufferedWriter bw = new BufferedWriter(fw);
                            bw.write(OGToolBar.getCurrentTool() + " | " + LocalDate.now() + " | " + LocalTime.now());
                            bw.newLine();
                            bw.close();
                        }
                        catch(Exception ex){
                            System.out.println(ex);
                        }
                    }
                });
            }
        };
        this.loggerTimer.scheduleAtFixedRate(this.logger, 5000, 30000);
    }
    /**
     * Autosaves the image in autosaveBackup to the specified autosave directory
     */
    public void autosaveImage(){
        if(this.unsavedChanges && this.path != null) {
            this.autosaveBackup = this.canvas.getRegion(0, 0, this.canvas.getWidth(), this.canvas.getHeight()); //snapshot of the current canvas
            File backupFile = new File(AUTOSAVE_DIR + LocalDate.now() + Instant.now().toEpochMilli() + ".png");
            try {                           //backup path is just date + time in secs since epoch and then the .png file type
                backupFile.createNewFile();
                ImageIO.write(SwingFXUtils.fromFXImage(this.autosaveBackup, null),
                        "png",
                        new FileOutputStream(backupFile));
                System.out.println("Autosaved!");
            } catch (IOException ex) {
                Logger.getLogger(OGTab.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * Updates the timer on the autosave task which allows user to change interval
     */
    public void updateAutosaveTimer(){
        this.autosaveSecs = OGToolBar.getAutosaveTime();
        this.autosave.cancel();
        this.autosaveTimer.purge();
        this.autosave = new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(new Runnable(){
                    @Override
                    public void run(){
                        autosaveImage();
                        autosaveTimer.schedule(autosave, 0, autosaveSecs*MILS_IN_SECS);
                    }
                });
            }
        };
        this.autosaveTimer.schedule(this.autosave, 0, this.autosaveSecs*MILS_IN_SECS);

    }

    public OGCanvas getCanvas(){return this.canvas;}
    /**
     * Gets the height of the canvas on the tab
     * @return The height as a double
     */
    public double getCanvasHeight(){return this.canvas.getHeight();}
    /**
     * Gets the width of the canvas on the tab
     * @return The width as a double
     */
    public double getCanvasWidth(){return this.canvas.getWidth();}
    /**
     * Draws an image to the canvas in the tab starting at the point x, y
     * @param im The image object to draw
     * @param x The x coordinate to draw the top left point of the image
     * @param y The y coordinate to draw the top left point of the image
     */
    public void drawImageAt(Image im, double x, double y){this.canvas.drawImageAt(im, x, y);}
    /**
     * Sets whether or not there is unsaved changes to true or false
     * @param unsavedChanges The boolean value to set the unsaved changes attribute to
     */
    public void setUnsavedChanges(boolean unsavedChanges){this.unsavedChanges = unsavedChanges;}
    /**
     * Sets the title to the specified string
     * @param title The string representative of the new title
     */
    public void setTitle(String title){
        this.title = title;
        this.updateTabTitle();
    }
    /**
     * Sets the file path
     * @param path The File object to set the new path to
     */
    public void setFilePath(File path){this.path = path;}
    /**
     * Updates the title of the tab to reflect unsaved changes
     * @return Whether or not there are any unsaved changes
     */
    public boolean getUnsavedChanges(){return unsavedChanges;}
    /**
     * Gets the title
     * @return The String representative of the title
     */
    public String getTitle(){return this.title;}
    /**
     * Gets the File that represents the path of the tab
     * @return The File object representative of the path
     */
    public File getFilePath(){return this.path;}
    /**
     * Updates the tab's title to reflect title changes or unsaved changes
     */
    public void updateTabTitle(){
        if(this.path != null)
            this.title = this.path.getName();   //sets it to path name in case of update/save as
        if(this.unsavedChanges)
            this.setText("*" + this.title);
        else
            this.setText(this.title);
    }
    /**
     * Saves a snapshot of the current canvas to the image specified at the path
     */
    public void saveImage(){
        Image im = this.canvas.getRegion(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        try{
            if(this.path != null){
                ImageIO.write(SwingFXUtils.fromFXImage(im, null), "png", this.path);
                this.setUnsavedChanges(false);
                this.setTitle(this.getFilePath().getName());
            }
        }catch(IOException ex){
            System.out.println(ex.toString());
        }
        this.setUnsavedChanges(false);
        this.updateTabTitle();          //updates the title of the tab at the end to remove *
    }
    /**
     * Saves an image at a path that isn't the path the canvas is already at
     */
    public void saveImageAs(){
        File path = chooseFile.showSaveDialog(OGPaint.mainStage);
        this.setFilePath(path);
        this.saveImage();
    }
    /**
     * Saves an image at a given path
     * @param path The given path to save the image to
     */
    public void saveImageAs(File path){
        this.setFilePath(path);
        this.saveImage();
    }
    /**
     * Opens an image from a file selected by the user
     */
    public static void openImage(){
        File path = chooseFile.showOpenDialog(OGPaint.mainStage);
        OGTab temp;
        if(path != null)
            temp = new OGTab();
        else
            temp = new OGTab(path);
        temp.updateTabTitle();          //makes sure the title is updated
        temp.canvas.drawImage(path);    //draws image from same path
        OGPaint.tabpane.getTabs().add(temp);  //adds it to the tabpane
        OGPaint.tabpane.getSelectionModel().select(temp);
    }
    /**
     * Opens an image from a file passed to the function
     * @param path The path to attempt to open the image from
     */
    public static void openImage(File path){
        OGTab temp;
        if(path == null)
            temp = new OGTab();
        else
            temp = new OGTab(path);
        temp.updateTabTitle();          //makes sure the title is updated
        temp.canvas.drawImage(path);    //draws image from same path
        OGPaint.tabpane.getTabs().add(temp);  //adds it to the tabpane
        OGPaint.tabpane.getSelectionModel().select(temp);
    }
    public static void openBlankImage(){
        OGTab temp = new OGTab(); //will open blank image by default
        OGPaint.tabpane.getTabs().add(temp);  //adds it to the tabpane
        OGPaint.tabpane.getSelectionModel().select(temp);
    }

    /**
     * Zooms in the pane by a factor of 1.15
     */
    public void zoomIn(){
        if(this.getScale() <= 10){
            this.setScale(this.getScale() * 1.15);
        }
        this.updateScale();
    }
    /**
     * Zooms out the pane by a factor of 1.15
     */
    public void zoomOut(){
        if(this.getScale() >= 0.1){
            this.setScale(this.getScale() / 1.15);
        }
        this.updateScale();
    }
    /**
     * Sets the scale to the specified value
     * @param scale The double to set scale to
     */
    public void setScale(double scale){this.scale = scale;}
    /**
     * Gets the scale of the stuff inside the tab
     * @return The scale as a double
     */
    public double getScale(){return this.scale;}
    /**
     * Resets the scale to normal (1)
     */
    public void resetScale(){this.setScale(1);}
    /**
     * Updates the scale of the canvas pane
     */
    public void updateScale(){
        this.canvasPane.setScaleX(this.getScale());
        this.canvasPane.setScaleY(this.getScale());
        this.canvasPane.setPrefSize(this.canvas.getWidth()*this.getScale()*2, this.canvas.getHeight()*this.getScale()*2);
        OGToolBar.setZoomLabel(this.getScale());
    }

    /**
     * Undoes the previous change to the image and draws to canvas
     */
    public void undo(){
        this.canvas.undo();
        this.setUnsavedChanges(true);
    }
    /**
     * Redoes the change that you undid and draws to canvas
     */
    public void redo(){
        this.canvas.redo();
        this.setUnsavedChanges(true);
    }
    /**
     * Updates the stacks
     */
    public void updateStacks(){
        this.canvas.updateStacks();
        this.setUnsavedChanges(true);
        this.updateTabTitle();
    }

}
