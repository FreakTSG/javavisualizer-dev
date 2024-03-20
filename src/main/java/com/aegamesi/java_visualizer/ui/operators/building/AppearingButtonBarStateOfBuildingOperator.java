package com.aegamesi.java_visualizer.ui.operators.building;

import com.aegamesi.java_visualizer.ui.ButtonBar;
import com.aegamesi.java_visualizer.ui.graphics.representations.Representation;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class AppearingButtonBarStateOfBuildingOperator extends StateOfBuildingOperator {

    protected ContextMenuThread contextMenuThread;

    public AppearingButtonBarStateOfBuildingOperator(BuildingOperator operator) {
        super(operator);
    }

    @Override
    public void init() {
        super.init();
        contextMenuThread = new ContextMenuThread(); // o mouseMoved irá atualizar
        final Point mousePointerPosition = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(mousePointerPosition.x, mousePointerPosition.y + 1);
        robot.mouseMove(mousePointerPosition.x, mousePointerPosition.y);

        contextMenuThread.start();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);

        operator.getButtonBar().setVisible(false);
        contextMenuThread.reset(e.getPoint());

        operator.setSourceRepresentation(operator.getCanvas().getMovableRepresentation(e.getX(), e.getY()));
        OverStateOfBuildingOperator.overPosition = e.getPoint();

    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
//        operator.getButtonBar().setVisible(false);
        contextMenuThread.cancel();
//        operator.setCurrentState(BuildingOperator.EXIT_CANVAS);
    }

    @Override
    public void dispose() {
        super.dispose();
        contextMenuThread.cancel();
    }

    enum ContextMenuThreadState {
        STARTED, STOPPED
    }

    class ContextMenuThread extends Thread {

        private long lastTime;
        private ContextMenuThreadState state;
        private Point mousePosition;

        public ContextMenuThread() {
            reset(new Point());
        }

        @Override
        public void run() {
            while (state != ContextMenuThreadState.STOPPED) {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (System.currentTimeMillis() - lastTime >= 1000) {
                    Representation representation = operator.getSourceRepresentation();
//                    System.out.println("==>" + representation + "-" + (representation != null ? representation.getOwner() : ""));

                    if (representation == null) {
                        operator.setCurrentState(BuildingOperator.OVER_EMPTY_AREA);
                    } else {
                        operator.setButtonBar(representation.getButtonBar(mousePosition));
                        operator.setCurrentState(BuildingOperator.OVER_NON_EMPTY_AREA);
                    }
                    final ButtonBar buttonBar = operator.getButtonBar();

                    Rectangle canvasVisibleRect = canvas.getVisibleRect();
                    Dimension buttonBarSize = buttonBar.getSize();
                    Point mousePosition = MouseInfo.getPointerInfo().getLocation();
                    int x = mousePosition.x, y = mousePosition.y;
                    Point canvasMousePosition = canvas.getMousePosition();
//                    System.out.println("buttonBarSize.width = " + buttonBarSize.width +
//                            " canvasMousePosition.x = " + canvasMousePosition.x*canvas.getZoom() +
//                            " canvasVisibleRect.width = " + canvasVisibleRect.width);
//                    System.out.println("canvasVisibleRect.x = " + canvasVisibleRect.x);
                    if (buttonBarSize.width + canvasMousePosition.x * canvas.getZoom() > canvasVisibleRect.x + canvasVisibleRect.width) {
                        x -= buttonBarSize.width;
                    }
                    if (buttonBarSize.height + canvasMousePosition.y * canvas.getZoom() > canvasVisibleRect.y + canvasVisibleRect.height) {
                        y -= buttonBarSize.height;
                    }
                    buttonBar.setLocation(x, y);
                    buttonBar.setVisible(true);
//                    System.out.println(buttonBar + " VISIVEL na classe " + getClass().getSimpleName());
                    cancel();
                }
            }
        }

        public void reset(Point mousePosition) {
            this.mousePosition = mousePosition;
            state = ContextMenuThreadState.STARTED;
            lastTime = System.currentTimeMillis();
        }

        public void cancel() {
            state = ContextMenuThreadState.STOPPED;
        }
    }

}

