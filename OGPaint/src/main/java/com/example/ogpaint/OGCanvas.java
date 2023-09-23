package com.example.ogpaint;

import java.util.Stack;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


/**
 * Extends the DrawCanvas; mainly has the listeners that allow for drawing to the canvas
 * @author Chisom Ogbuefi
 */

public class OGCanvas extends OGDrawCanvas{
    private double x, y;
    private Image clipboard;

    private Stack<Image> undoStack;
    private Stack<Image> redoStack;

    public OGCanvas(){
        super();
        x = y = 0;
        this.clipboard = null;
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();



        this.setWidth(1200);
        this.setHeight(800);
        //draws white background
        this.setFillColor(Color.WHITE);
        this.setLineColor(Color.WHITE);
        this.setFillShape(true);
        this.drawRect(0,0,this.getWidth(),this.getHeight());
        this.setFillShape(false);

        this.undoStack.push(this.getRegion(0,0, this.getWidth(), this.getHeight())); //pushes the blank/original image to first pos of undo stack


        this.setOnMousePressed(e -> {   //updates all of this on click
            x = e.getX();
            y = e.getY();
            this.setLineColor(OGToolBar.getLineColor());
            this.setFillColor(OGToolBar.getFillColor());
            this.setLineWidth(OGToolBar.getLineWidth());
            this.setFillShape(OGToolBar.getFillStatus());
            switch (OGToolBar.getCurrentTool()) {
                case("Line"):
                    this.drawLine(x, y, x, y);
                    this.updateStacks();
                    break;
                case("Rectangle"):
                    this.drawRect(x,y,x,y);
                    this.updateStacks();
                    break;
                case("Square"):
                    this.drawSquare(x, y, x, y);
                    this.updateStacks();
                    break;
                case ("Triangle"):
                    this.drawTriangle(x, y, e.getX(), e.getY(), x + (e.getX() - x) / 2, y);
                    this.updateStacks();
                    break;
                case("Ellipse"):
                    this.drawEllipse(x,y,x,y);
                    this.updateStacks();
                    break;
                case("Circle"):
                    this.drawNgon(x, y, x, y, 314);
                    this.updateStacks();
                    break;
            }

        });
        this.setOnMouseDragged(e -> {
            switch(OGToolBar.getCurrentTool()) {
                case ("Freehand"):
                    this.drawLine(x, y, e.getX(), e.getY());
                    x = e.getX();
                    y = e.getY();
                    break;
                case("Line"):
                    this.undo();
                    this.drawLine(x, y, e.getX(), e.getY());
                    this.updateStacks();
                    break;
                case("Rectangle"):
                    this.undo();
                    this.drawRect(x, y, e.getX(), e.getY());
                    this.updateStacks();
                    break;
                case("Square"):
                    this.undo();
                    this.drawSquare(x, y, e.getX(), e.getY());
                    this.updateStacks();
                    break;
                case("Triangle"):
                    this.undo();
                    this.drawTriangle(x, y, e.getX(), e.getY(), x + (e.getX() - x) / 2, y);
                    //this.drawTriangle(x, y, e.getX(), e.getY(), x + (e.getX() - x) / 2, y);
                    this.updateStacks();
                    break;
                case("Ellipse"):
                    this.undo();
                    this.drawEllipse(x,y,e.getX(),e.getY());
                    this.updateStacks();
                    break;
                case("Circle"):
                    this.undo();
                    this.drawNgon(x, y, e.getX(), e.getY(), 314);
                    this.updateStacks();
                    break;
            }

        });
        this.setOnMouseReleased(e -> {
            switch (OGToolBar.getCurrentTool()) {
                case ("Freehand"):
                    this.drawLine(x, y, e.getX(), e.getY());
                    OGPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Line"):
                    this.undo();
                    this.drawLine(x, y, e.getX(), e.getY());
                    OGPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Rectangle"):
                    this.undo();
                    this.drawRect(x, y, e.getX(), e.getY());
                    OGPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Square"):
                    this.undo();
                    this.drawSquare(x, y, e.getX(), e.getY());
                    OGPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Triangle"):
                    this.undo();
                    this.drawTriangle(x, y, e.getX(), e.getY(), x + (e.getX() - x) / 2, y);
                    OGPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Ellipse"):
                    this.undo();
                    this.drawEllipse(x, y, e.getX(), e.getY());
                    OGPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
                case("Circle"):
                    this.undo();
                    this.drawNgon(x, y, e.getX(), e.getY(), 314);
                    OGPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
            }

            OGPaint.getCurrentTab().updateTabTitle();
            this.updateStacks();
            x = y = 0;  //resets the x and y coordinates at the end to be safe
        });
    }
    /**
     * Undoes the previous change to the image and draws to canvas
     */
    public void undo(){
        Image im = undoStack.pop();
        if(!undoStack.empty()){
            redoStack.push(im);
            this.drawImage(undoStack.peek());
        }
        else{   //puts image back because in this case it's the base/only one in stack
            this.drawImage(im);
            undoStack.push(im);
        }
    }
    /**
     * Redoes the change that you undid and draws to canvas
     */
    public void redo(){
        if(!redoStack.empty()){
            Image im = redoStack.pop();
            undoStack.push(im);
            this.drawImage(im);
        }
    }
    /**
     * Updates the stacks
     */
    public void updateStacks(){
        undoStack.push(this.getRegion(0,0, this.getWidth(), this.getHeight()));
        redoStack.clear();  //clear redo every time to avoid problems with breaking time
    }



}
