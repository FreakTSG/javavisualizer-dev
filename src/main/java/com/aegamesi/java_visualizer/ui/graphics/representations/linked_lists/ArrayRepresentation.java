package com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists;

import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.representations.GeneralRepresentation;

import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.IDSToolWindow;
import com.aegamesi.java_visualizer.ui.graphics.GraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.OutConnector;
import com.aegamesi.java_visualizer.ui.graphics.PositionalGraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.StraightConnection;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.*;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.Representation;
import com.aegamesi.java_visualizer.ui.graphics.representations.RepresentationWithInConnectors;
import com.aegamesi.java_visualizer.utils.Utils;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.Container;


import java.awt.*;
import java.lang.reflect.Array;

public class ArrayRepresentation extends GeneralRepresentation<Object> {
    private static final long serialVersionUID = 1L;


    private String componentTypeName;

//    private ArrayList<AggregateGraphicElementContainer> aggregateGraphicElementContainers;
//    private ArrayList<Container> containersToDeepRemove;

    private ArrayRepresentation(Object owner, String componentTypeName, MyCanvas canvas) {
        this(new Point(), owner, componentTypeName, canvas);
    }

    public ArrayRepresentation(Point position, Object owner, String componentTypeName, MyCanvas canvas) {
        super(position, owner, canvas, true);
        this.componentTypeName = componentTypeName;
    }

    public void init() {
        container.setTopCellSpacing(0); // é reposto depois quando se remove o contentor interno
        container.setLeftCellSpacing(0);
        container.setInnerCellSpacing(0);


        container.setHorizontal(false);
        container.setTopCellSpacing(4);
        String arrayElementSimpleTypeName = Utils.getClassSimpleName(owner.getClass().getName());
        Class<?> classForName;
        if (Utils.isPrimitiveOrPrimitiveWrapperType(arrayElementSimpleTypeName) ||
                Utils.isACompactReferenceableType(arrayElementSimpleTypeName) ||
                ((classForName = Utils.getClassForName(owner.getClass().getName())) != null &&
                        classForName.isEnum())) {

        } else {
            for (int i = 0; i < Array.getLength(owner); i++) {
                Reference reference = new ArrayReference(owner, i, Location.CENTER, true) {
                    @Override
                    protected String getUnsetReferenceExpression() {
                        return "null";
                    }
                };
                container.add(createAggregateGraphicElementWithIndex(reference, i), Location.RIGHT);
            }
        }
    }

    public void update() {
        container.removeAllGraphicElements();
        init();
    }

    public String getComponentTypeName() {
        return componentTypeName;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Point position = new Point(getPosition());
        position.translate(0, -3);
        Color oldColor = g.getColor();
        g.setColor(Color.BLACK);
        g.drawString(Utils.getClassSimpleName(getCompleteTypeName()), position.x, position.y);
        g.setColor(oldColor);
    }

    private Container createAggregateGraphicElementWithIndex(AggregateRectangularGraphicElement aggregateGraphicElement, int index) {
        Container containerWithoutInConnectors = new Container(new Point(), new Dimension(), true);
        containerWithoutInConnectors.setInnerCellSpacing(0);
        containerWithoutInConnectors.setTopCellSpacing(0);
        containerWithoutInConnectors.setBottomCellSpacing(0);
        containerWithoutInConnectors.setBorderShown(false);
        NormalTextElement indexNormalTextElement = new NormalTextElement(String.valueOf(index), ConstantsIDS.FONT_SIZE_INDEX);
        indexNormalTextElement.setBackgroundColor(GraphicElement.TRANSPARENT_COLOR);
        indexNormalTextElement.setBorderColor(GraphicElement.TRANSPARENT_COLOR);
        containerWithoutInConnectors.add(indexNormalTextElement);
        containerWithoutInConnectors.add(aggregateGraphicElement);
        return containerWithoutInConnectors;
    }

    public String getCompleteTypeName() {
        return componentTypeName + "[]";
    }

// @Override
// public ButtonBar getButtonBar(Point position) {
//     final PositionalGraphicElement positionalGraphicElement = container.getPositionalGraphicElement(position);
//     return positionalGraphicElement instanceof ArrayReference && ((ArrayReference) positionalGraphicElement).getFieldValue() != null ?
//             ((ArrayReference) positionalGraphicElement).getButtonBar() :
//             positionalGraphicElement instanceof OutConnector && ((OutConnector) positionalGraphicElement).getReference().getFieldValue() != null ?
//                     ((ArrayReference) ((OutConnector) positionalGraphicElement).getReference()).getButtonBar() :
//                     IDSToolWindow.getButtonBar(IDSToolWindow.COLLECTION_BUTTON_BAR);
// }

 // public String getCreationCode() {
 //     StringBuilder sb = new StringBuilder("new ");
 //     final String type = this.componentTypeName;
 //     final String[] parts = type.split("\\[", 2);

 //     Class<?> clazz = Utils.getClassForName(type);
 //     String classSimpleName = Utils.getClassSimpleName(type);
 //     boolean withInitialization = parts.length == 1 && clazz != null &&
 //             (clazz.isEnum() || Utils.isPrimitiveOrPrimitiveWrapperType(classSimpleName) || Utils.isACompactReferenceableType(classSimpleName));

 //     sb.append(Utils.getClassSimpleName(parts[0]).split("<")[0]).append("[");
 //     WrapperWithValue[] ownerValue = owner.getValue();
 //     if (!withInitialization) {
 //         sb.append(ownerValue.length);
 //     }
 //     sb.append("]");
 //     if (parts.length > 1) {
 //         sb.append("[").append(parts[1]);
 //     } else if (withInitialization) {
 //         sb.append("{");
 //         for (int i = 0; i < ownerValue.length - 1; i++) {
 //             sb.append(Utils.getCode(ownerValue[i].getValue(), canvas.getIDSToolWindow())).append(", ");
 //         }
 //         if (ownerValue.length > 0) {
 //             sb.append(Utils.getCode(ownerValue[ownerValue.length - 1].getValue(), canvas.getIDSToolWindow()));
 //         }
 //         sb.append("}");
 //     }

 //     return sb.toString();
 // }

    public void add(Representation sourceRepresentation, PositionalGraphicElement positionalGraphicElement) {
        final OutConnector outConnector = positionalGraphicElement instanceof ArrayReference ?
                ((ArrayReference) positionalGraphicElement).getOutConnector() :
                ((OutConnector) positionalGraphicElement);

       myCanvas.add(new StraightConnection<>(outConnector, ((RepresentationWithInConnectors) sourceRepresentation)));
        outConnector.getReference().setFieldValue(sourceRepresentation.getOwner());
    }

//    private class AggregateGraphicElementContainer {
//        Container container;
//        AggregateRectangularGraphicElement aggregateGraphicElement;
//
//        public AggregateGraphicElementContainer(Container container, AggregateRectangularGraphicElement aggregateGraphicElement) {
//            this.container = container;
//            this.aggregateGraphicElement = aggregateGraphicElement;
//        }
//
//        void remove() {
//            container.remove(aggregateGraphicElement);
//            container.setTopCellSpacing(4);
//            container.repositionAggregateGraphicElement();
//        }
//    }




}
