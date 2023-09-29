package com.example.ogpaint;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;

/**
 * Extends MenuBar; puts all of the customization into one file where I can find it easily
 * @author Chisom Ogbuefi
 */

public class OGMenuBar extends MenuBar {

    public final static String HELP_PATH = "help.txt";
    public final static String REL_NOTES_PATH = "release-notes.txt";
    public final static String ABOUT_PATH = "about.txt";

    public OGMenuBar() {
        super();
        Random rand = new Random();

        Menu file = new Menu("_File");
        MenuItem[] fileOptions = {new MenuItem("New file"), new MenuItem("Open file..."),
                new MenuItem("Save file"),new MenuItem("Save file as..."), new MenuItem("Undo"),
                new MenuItem("Redo"), new MenuItem("_Exit")}; //All of my file suboptions

        Menu options = new Menu("_Options");
        MenuItem[] optionsOptions = {new MenuItem("Zoom In"), new MenuItem("Zoom Out")};

        Menu help = new Menu("_Help");
        MenuItem[] helpOptions = {new MenuItem("_About"), new MenuItem("Hel_p"),
                new MenuItem("_Release Notes")};

        getMenus().add(file);
        getMenus().add(options);
        getMenus().add(help);

        for(MenuItem i : fileOptions)
            file.getItems().add(i);   //adds all of the sub-options to the File option using the best for loop
        for(MenuItem i : optionsOptions)
            options.getItems().add(i);//adds all of the sub-options to the Options option using the best for loop
        for(MenuItem i : helpOptions)
            help.getItems().add(i);   //adds all of the sub-options to the Help option using the best for loop

        fileOptions[0].setOnAction((ActionEvent e) -> { //open new/blank image
            OGTab.openBlankImage();
        });
        fileOptions[1].setOnAction((ActionEvent e) -> { //open existing image
            OGTab.openImage();
        });
        fileOptions[2].setOnAction((ActionEvent e) -> { //save image
            if(OGPaint.getCurrentTab().getFilePath() == null)   //if there is no filepath, save as, otherwise save
                OGPaint.getCurrentTab().saveImageAs();
            else
                OGPaint.getCurrentTab().saveImage();
        });
        fileOptions[3].setOnAction((ActionEvent e) -> { //saves image as
            try{
                OGPaint.getCurrentTab().saveImageAs();
            }catch(Exception ex){
                System.out.println(ex);
            }
        });
        fileOptions[4].setOnAction((ActionEvent e) -> { //undo
            OGPaint.getCurrentTab().undo();
        });
        fileOptions[5].setOnAction((ActionEvent e) -> { //redo
            OGPaint.getCurrentTab().redo();
        });

        /* When the last item in the fileOptions array is clicked or activated,
         it triggers an exit alert dialog to confirm whether the user wants to exit the application.
         */
        fileOptions[fileOptions.length - 1].setOnAction((ActionEvent e) -> {
            OGPopUp.createExitAlert(OGPaint.mainStage);
        });

        helpOptions[0].setOnAction((ActionEvent e) -> {  //help window
            try {
                OGPopUp.createTextWindow(new File(HELP_PATH), "Help", 650, 350);
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            }
        });
        helpOptions[1].setOnAction((ActionEvent e) -> {  //about window
            try {
                OGPopUp.createTextWindow(new File(ABOUT_PATH), "About", 650, 350);
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            }
        });
        helpOptions[2].setOnAction((ActionEvent e) -> {  //release notes
            try {
                OGPopUp.createTextWindow(new File(REL_NOTES_PATH), "Release Notes", 635, 500);
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            }
        });


        optionsOptions[0].setOnAction((ActionEvent e) -> {  //zoom in
            OGPaint.getCurrentTab().zoomIn();
        });
        optionsOptions[1].setOnAction((ActionEvent e) -> {  //zoom out
            OGPaint.getCurrentTab().zoomOut();
        });

        // keybinds down below
        fileOptions[1].setAccelerator(KeyCombination.keyCombination("Ctrl+O"));      //sets open option to Ctrl+O
        fileOptions[3].setAccelerator(KeyCombination.keyCombination("Ctrl+S"));      //sets save option to Ctrl+S
        fileOptions[5].setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));      //sets undo to Ctrl+Z
        fileOptions[6].setAccelerator(KeyCombination.keyCombination("Ctrl+A"));      //sets redo to Ctrl+A
        optionsOptions[0].setAccelerator(KeyCombination.keyCombination("Ctrl+]"));   //sets zoom in to Ctrl+]
        optionsOptions[1].setAccelerator(KeyCombination.keyCombination("Ctrl+["));   //sets zoom out to Ctrl+[
        helpOptions[2].setAccelerator(KeyCombination.keyCombination("Ctrl+R"));      //sets release notes to Ctrl+R





    }

}
