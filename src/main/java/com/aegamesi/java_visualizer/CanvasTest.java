package com.aegamesi.java_visualizer;

import com.aegamesi.java_visualizer.model.ExecutionTrace;
import com.aegamesi.java_visualizer.ui.Canvas;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class CanvasTest {
    public static void main(String[] args) {
try {
        // Create a new JFrame
        JFrame frame = new JFrame("Canvas Test");


        // Create an instance of Canvas
        Canvas canvas = Canvas.getCanvas();

        // Add the canvas to the frame
        frame.getContentPane().add(canvas);

        // Set the default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the frame
        frame.setSize(500, 500);

        // Make the frame visible
        frame.setVisible(true);
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    }


}
