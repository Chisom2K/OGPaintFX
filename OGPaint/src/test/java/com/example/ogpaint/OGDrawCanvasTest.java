package com.example.ogpaint;

import com.example.ogpaint.OGDrawCanvas;
import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

class OGDrawCanvasTest {
    public OGDrawCanvasTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setFillShape method, of class OGDrawCanvas.
     */
    @Test
    public void testSetFillShape(){
        boolean fillShape = false;
        System.out.println("setFillShape");
        OGDrawCanvas instance = new OGDrawCanvas();
        instance.setFillShape(fillShape);
        assertEquals(fillShape,instance.getFillShape());//if getFillShape returns value defined above, we good
    }

    /**
     * Test of setLineColor method, of class OGDrawCanvas.
     */
    @Test
    public void testSetLineColor() {
        System.out.println("setLineColor");
        Color color = Color.MAROON;
        OGDrawCanvas instance = new OGDrawCanvas();
        instance.setLineColor(color);
        assertEquals(color, instance.getLineColor());   //if getLineColor returns value defined above, we good
    }

    /**
     * Test of setFillColor method, of class OGDrawCanvas.
     */
    @Test
    public void testSetFillColor() {
        System.out.println("setFillColor");
        Color color = Color.CHARTREUSE;
        OGDrawCanvas instance = new OGDrawCanvas();
        instance.setFillColor(color);
        assertEquals(color, instance.getFillColor());   //if getLineColor returns value defined above, we good
    }

    /**
     * Test of setLineWidth method, of class OGDrawCanvas.
     */
    @Test
    public void testSetLineWidth() {
        System.out.println("setLineWidth");
        double width = 10.0;
        OGDrawCanvas instance = new OGDrawCanvas();
        instance.setLineWidth(width);
        //had to add a third param delta
        assertEquals(10.0,instance.getLineWidth(),0.1); //if getLineWidth returns value defined above, we good
    }
}