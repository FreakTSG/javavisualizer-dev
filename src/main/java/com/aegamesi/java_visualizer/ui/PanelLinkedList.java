package com.aegamesi.java_visualizer.ui;

import com.aegamesi.java_visualizer.model.IDSLinkedList;
import com.aegamesi.java_visualizer.model.Value;
import wrapper_classes.WrapperWithValue;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// This class is responsible for visually representing a linked list
public class PanelLinkedList<T extends WrapperWithValue> extends JPanel {
    private IDSLinkedList<T> linkedList;

    public PanelLinkedList(IDSLinkedList<T> linkedList) {
        this.linkedList = linkedList;
        setLayout(new BorderLayout());
        buildUI();
    }

    // Method to build the user interface
    private void buildUI() {
        NodePanel listPanel = new NodePanel();
        listPanel.setLayout(null); // We will manage the layout manually

        // Wrap the listPanel in a JBScrollPane for better handling of long lists
        JBScrollPane scrollPane = new JBScrollPane(listPanel);
        this.add(scrollPane, BorderLayout.CENTER);

        // Assume we have a method to layout our nodes
        layoutNodes(listPanel);
    }

    private void layoutNodes(NodePanel listPanel) {
        // Logic to layout nodes goes here
        // For simplicity, let's just lay them out in a horizontal line
        int x = 10; // Starting x position
        int y = 30; // Y position stays constant
        int width = 50; // Width of each node
        int height = 30; // Height of each node
        for (Value value : linkedList.items) {
            NodeComponent nodeComp = new NodeComponent(value);
            nodeComp.setBounds(x, y, width, height);
            listPanel.add(nodeComp);
            listPanel.addNode(nodeComp); // Add node to NodePanel for connection drawing
            x += width + 10; // Move x position for next node
        }
        listPanel.setPreferredSize(new Dimension(x, height + 60)); // Set the preferred size to the full layout
    }

    // Custom component to represent a node
    private static class NodeComponent extends JComponent {
        private final Value value;

        public NodeComponent(Value value) {
            this.value = value;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            // Center the value string in the node
            FontMetrics fm = g.getFontMetrics();
            String valueString = value.toString();
            int stringWidth = fm.stringWidth(valueString);
            int stringAscent = fm.getAscent();
            int x = (getWidth() - stringWidth) / 2;
            int y = (getHeight() + stringAscent) / 2;
            g.drawString(valueString, x, y);
        }
    }

    // Inner class for custom node panel
    private class NodePanel extends JPanel {
        private java.util.List<NodeComponent> nodes = new ArrayList<>();

        public void addNode(NodeComponent node) {
            nodes.add(node);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Paint connecting lines or arrows here
            for (int i = 0; i < nodes.size() - 1; i++) {
                NodeComponent startNode = nodes.get(i);
                NodeComponent endNode = nodes.get(i + 1);
                int startX = startNode.getX() + startNode.getWidth();
                int startY = startNode.getY() + startNode.getHeight() / 2;
                int endX = endNode.getX();
                int endY = startY;

                g.setColor(Color.BLACK);
                g.drawLine(startX, startY, endX, endY);
            }
        }
    }
}
