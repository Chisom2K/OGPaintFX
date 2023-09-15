package com.example.ogpaint;

import java.io.File;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.StrokeLineCap;

/**
 * Extends the canvas and makes it easier for me to draw stuff
 * @author Chisom Ogbuefi
 */
public class OGDrawCanvas extends Canvas {
    private boolean fillShape;
    private GraphicsContext gc;

    public OGDrawCanvas(){
        super();
        this.fillShape = false;
        this.gc = this.getGraphicsContext2D();
        this.gc.setLineCap(StrokeLineCap.ROUND);
    }

    public void drawLine(double x1, double y1, double x2, double y2){gc.strokeLine(x1, y1, x2, y2);}

    /**
     * Draws an image to the canvas starting at the origin
     * @param im The image object to draw
     */
    public void drawImage(Image im){

        this.setWidth(im.getWidth());
        this.setHeight(im.getHeight());
        this.gc.drawImage(im, 0, 0);
    }
    /**
     * Draws an image to the canvas starting at the origin
     * @param file The file to get the image from
     */
    public void drawImage(File file){
        if(file != null){
            Image im = new Image(file.toURI().toString());
            this.drawImage(im);
        }
    }
    /**
     * Draws an image to the canvas starting at the point x, y
     * @param im The image object to draw
     * @param x The x coordinate to draw the top left point of the image
     * @param y The y coordinate to draw the top left point of the image
     */
    public void drawImageAt(Image im, double x, double y){this.gc.drawImage(im, x, y);}
    /**
     * Gets the color at the coordinate (x,y)
     * @param x The x value to get the color at
     * @param y The y value to get the color at
     * @return The Color at that pixel
     */
    public Color getColorAtPixel(double x, double y){
        return this.getRegion(x, y, x+1, y+1).getPixelReader().getColor(0, 0);
    }
    /**
     * Gets the image using the bounds (x1,y1) and (x2,y2)
     * @param x1 Starting x value (top left)
     * @param y1 Starting y value (top left)
     * @param x2 Ending x value (bottom right)
     * @param y2 Ending y value (bottom right)
     * @return The image in that region
     */
    public Image getRegion(double x1, double y1, double x2, double y2){
        SnapshotParameters sp = new SnapshotParameters();
        WritableImage wi = new WritableImage((int)Math.abs(x1 - x2),(int)Math.abs(y1 - y2));

        sp.setViewport(new Rectangle2D(
                (x1 < x2 ? x1 : x2),
                (y1 < y2 ? y1 : y2),
                Math.abs(x1 - x2),
                Math.abs(y1 - y2)));

        this.snapshot(sp, wi);
        return wi;
    }


    /**
     * Clears everything on the canvas
     */
    public void clearCanvas(){
        this.gc.clearRect(0,0, this.getWidth(), this.getHeight());
    }
    /**
     * Sets the fillShape attribute to the boolean value given
     * @param fillShape Boolean value to tell us whether to fill the shapes or not
     */
    public void setFillShape(boolean fillShape){this.fillShape = fillShape;}
    /**
     * Gives us the fillShape attribute boolean value
     * @return Whether or not to fill the shape or not
     */
    public boolean getFillShape(){return this.fillShape;}
    /**
     * Sets all lines and colors to the specified color
     * @param color The color to set the lines/borders
     */
    public void setLineColor(Color color){gc.setStroke(color);}
    /**
     * Gets the color that the lines and borders are set to
     * @return The Color object that represents line/border color
     */
    public Color getLineColor(){return (Color)gc.getStroke();}
    /**
     * Sets the fills of the shapes to the specified color
     * @param color The color to set the fill of shapes
     */
    public void setFillColor(Color color){gc.setFill(color);}
    /**
     * Gets the color that the fill is set to
     * @return The Color object that represents the fill color
     */
    public Color getFillColor(){return (Color)gc.getFill();}
    /**
     * Sets the width of the lines/borders
     * @param width The value to set line width to
     */
    public void setLineWidth(double width){this.gc.setLineWidth(width);}
    /**
     * Gets the width of the lines/borders
     * @return The value representing the line/border width
     */
    public double getLineWidth(){return this.gc.getLineWidth();}
}
