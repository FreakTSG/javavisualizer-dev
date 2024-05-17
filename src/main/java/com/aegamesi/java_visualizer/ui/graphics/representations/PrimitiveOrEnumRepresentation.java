package com.aegamesi.java_visualizer.ui.graphics.representations;

import com.aegamesi.java_visualizer.ui.ConstantsIDS;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.NormalTextElement;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;

import java.awt.*;

public class PrimitiveOrEnumRepresentation<TPrimitive extends Object> extends GeneralGenericRepresentation<TPrimitive> {
    private static final long serialVersionUID = 1L;


    public PrimitiveOrEnumRepresentation(Point position, TPrimitive owner, MyCanvas myCanvas) {
        this(position, owner, myCanvas, false);
    }

    public PrimitiveOrEnumRepresentation(Point position, TPrimitive owner, MyCanvas myCanvas, boolean horizontal) {
        super(position, owner, myCanvas, horizontal);
        update();
    }

    @Override
    public void init() {

    }

    public void update() {
        container.removeAllGraphicElements();
        container.add(new NormalTextElement(String.valueOf(owner), ConstantsIDS.FONT_SIZE_TEXT), Location.LEFT);
    }

    @Override
    public void edit(Point position) {
    }

    public String getCreationCode() {
//        return Utils.getCreationCode(owner.getValue());
        //não pode ser o código de cima porque as classes dos primitivos têm muitos atributos
        //e não há construtores que os recebam
        //TEM de ser algo bem mais simples
        Object ownerValue = owner;
        Class<?> ownerValueClass = ownerValue.getClass();
        if (ownerValueClass == String.class) {
            return "\"" + ownerValue + "\"";
        }
        if (ownerValueClass == Character.class) {
            Character charValue = (Character) ownerValue;
            return charValue == 0 ? "0" : "'" + charValue + "'";
        }
        if (ownerValueClass.isEnum()) {
//            idsToolWindow.addImportToEditorPaneSourceCode(fieldType.getName());
            return ownerValueClass.getSimpleName() + "." + ownerValue;
        }
//        return "new " + Utils.getClassSimpleName(valueTypeName) + "(" + ownerValue + ")";
        return String.valueOf(ownerValue);

    }
}
