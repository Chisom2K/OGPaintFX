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

    public OGCanvas(){
        super();
        x = y = 0;
        this.clipboard = null;


        this.setWidth(1200);
        this.setHeight(800);
        //draws white background
        this.setFillColor(Color.WHITE);
        this.setLineColor(Color.WHITE);
        this.setFillShape(true);
        this.setFillShape(false);

        this.setLineColor(Color.WHITE);
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
                    this.drawLine(x, y, e.getX(), e.getY());
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
                    this.drawLine(x, y, e.getX(), e.getY());
                    OGPaint.getCurrentTab().setUnsavedChanges(true);
                    break;
            }

        });
    }


}
