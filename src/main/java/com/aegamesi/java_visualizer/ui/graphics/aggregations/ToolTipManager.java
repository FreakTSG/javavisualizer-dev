package com.aegamesi.java_visualizer.ui.graphics.aggregations;

import java.io.Serializable;

public class ToolTipManager implements Serializable {
    private static final long serialVersionUID = 1L;

    protected boolean showToolTipText;

    public ToolTipManager() {
        this.showToolTipText = false;
    }

    public boolean isShowToolTipText() {
        return showToolTipText;
    }

    public void setShowToolTipText(boolean showToolTipText) {
        this.showToolTipText = showToolTipText;
    }

}