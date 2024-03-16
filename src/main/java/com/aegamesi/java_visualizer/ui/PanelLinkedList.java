package com.aegamesi.java_visualizer.ui;

import com.aegamesi.java_visualizer.model.IDSLinkedList;
import com.aegamesi.java_visualizer.model.Value;
import wrapper_classes.WrapperWithValue;

import javax.swing.*;
import java.awt.*;

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
        // Assuming you want a vertical layout
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createTitledBorder("Linked List"));

        // Add each Value in the IDSLinkedList as a label to the panel
        for (Value value : linkedList.items) {
            JLabel label = new JLabel(value.toString());
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            listPanel.add(label);
        }

        // Assuming there's more UI logic here to represent pointers or other properties

        // Add the listPanel to the main PanelLinkedList
        this.add(listPanel, BorderLayout.CENTER);
    }
}
