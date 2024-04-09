package com.aegamesi.java_visualizer.plugin;

import com.aegamesi.java_visualizer.model.ExecutionTrace;
import com.aegamesi.java_visualizer.ui.IDSToolWindow;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.VisualizationPanel;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.BorderLayout;

class MainPane extends JPanel {
	private JLabel placeholderLabel;
	private VisualizationPanel viz;
    private MyCanvas myCanvas;

    private final float[] ZOOM_LEVELS = {0.25f, 0.333f, 0.5f, 0.666f, 0.75f, 0.8f, 0.9f, 1.0f, 1.1f, 1.25f, 1.5f, 1.75f, 2.0f, 2.5f, 3.0f, 4.0f};

    public MainPane() {
        setLayout(new BorderLayout());
        initializeCanvas();
        placeholderLabel = new JLabel("No visualization available", SwingConstants.CENTER);
        add(placeholderLabel, BorderLayout.CENTER);
    }
    private void initializeCanvas() {
        this.myCanvas = IDSToolWindow.getMyCanvas(); // Make sure this method correctly returns an instance of MyCanvas
        if (this.myCanvas == null) {
            // Initialize MyCanvas here if it's not done elsewhere
            this.myCanvas = new MyCanvas(new IDSToolWindow()); // Make sure IDSToolWindow is correctly instantiated before this
        }
    }
	void setTrace(ExecutionTrace trace) {
        if (this.myCanvas == null) {
            initializeCanvas(); // Make sure MyCanvas is initialized
        }
        this.myCanvas.updateRepresentations(trace);
        // Remove placeholder and add the scroll pane containing MyCanvas
        if (placeholderLabel.getParent() != null) {
            remove(placeholderLabel);
        }
        JBScrollPane scrollPane = new JBScrollPane(this.myCanvas);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
	}

    void zoom(int direction) {
        if (viz != null) {
            float currentZoom = getZoom();
            int closestLevel = -1;
            float closestLevelDistance = Float.MAX_VALUE;
            for (int i = 0; i < ZOOM_LEVELS.length; i += 1) {
                float dist = Math.abs(ZOOM_LEVELS[i] - currentZoom);
                if (dist < closestLevelDistance) {
                    closestLevelDistance = dist;
                    closestLevel = i;
                }
            }

            int level = Math.max(0, Math.min(ZOOM_LEVELS.length - 1, closestLevel + direction));
            float newZoom = ZOOM_LEVELS[level];
            PropertiesComponent.getInstance().setValue(JavaVisualizerManager.KEY_ZOOM, newZoom, 1.0f);
            viz.setScale(newZoom);
        }
    }

    private float getZoom() {
        return PropertiesComponent.getInstance().getFloat(JavaVisualizerManager.KEY_ZOOM, 1.0f);
    }
}
