package com.aegamesi.java_visualizer.ui;

import com.aegamesi.java_visualizer.ui.graphics.representations.ButtonBar;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.ResourceBundle;

public class IDSToolWindow {

    private static final int OPERATION_NAME_MAX_VISIBLE_SIZE = 30;
    public static MyCanvas myCanvas;
    private final JPanel panel;
    private URLClassLoader urlClassLoader;
    private static HashMap<String, ButtonBar> buttonBarByKey;
    public static final String COMPARATOR_BUTTON_BAR = "COMPARATOR";
    private ClassCreation generatedClass;


    public IDSToolWindow() {


        clearClassLoader();
        panel = new JPanel(new BorderLayout());

        myCanvas = new MyCanvas(this);

        JScrollPane scrollPaneCanvas = new JScrollPane(myCanvas);
        myCanvas.setAutoscrolls(true);
        myCanvas.setBackground(Color.WHITE);
        panel.add(scrollPaneCanvas, BorderLayout.CENTER);


        panel.setVisible(true);


    }


    public void clearClassLoader() {
        ClassLoader classLoader = getClass().getClassLoader();
        ResourceBundle.clearCache(classLoader);
        java.beans.Introspector.flushCaches();

//        ResourceBundle.clearCache(urlClassLoader);

        try {
//            ByteArrayOutputStream byteArrayOutputStream = canvas.saveStateToMemory();
            final File root = new File(".");
            urlClassLoader = new URLClassLoader(new URL[]{root.toURI().toURL()}, getClass().getClassLoader());
//            canvas.loadStateFromMemory(byteArrayOutputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public URLClassLoader getUrlClassLoader() {
        return urlClassLoader;
    }

    public JComponent getComponent() {
        return panel;
    }

    public static MyCanvas getMyCanvas() {
        return myCanvas;
    }

    public static ButtonBar getButtonBar(String key) {
        return buttonBarByKey.get(key);
    }
    public void addImportToEditorPaneSourceCode(String anImport) {
        generatedClass.addImport(anImport);
    }
}
