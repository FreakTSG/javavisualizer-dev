package com.aegamesi.java_visualizer.ui.graphics.aggregations;

import com.aegamesi.java_visualizer.ui.graphics.PositionalGraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;

import java.awt.*;
import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

public class Container extends AggregateRectangularGraphicElement {
    private static final long serialVersionUID = 1L;

    protected boolean horizontal;
    protected int topCellSpacing, bottomCellSpacing, leftCellSpacing, rightCellSpacing, innerCellSpacing;

    protected ArrayList<LocalizableAggregateGraphicElement> localizableAggregateGraphicElements;
    protected Point nextAggregateGraphicElementPosition;

    public Container(Point position, Dimension dimension, boolean horizontal) {
        this(position, dimension, Color.BLACK, TRANSPARENT_COLOR, horizontal);
    }

    public Container(Point position, Dimension dimension, Color borderColor, Color backgroundColor, boolean horizontal) {
        super(position, dimension, borderColor, backgroundColor);
        this.horizontal = horizontal;
        localizableAggregateGraphicElements = new ArrayList<>();
        setCellSpacing(4);
        nextAggregateGraphicElementPosition = new Point(position.x + leftCellSpacing, position.y + topCellSpacing);
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
        repositionAggregateGraphicElement();
    }

    public Point getCenterPosition() {
        return new Point(position.x + dimension.width / 2, position.y + dimension.height / 2);
    }

    public void setCellSpacing(int cellSpacing) {
        topCellSpacing = bottomCellSpacing = leftCellSpacing = rightCellSpacing = innerCellSpacing = cellSpacing;
        repositionAggregateGraphicElement();
    }

    public int getTopCellSpacing() {
        return topCellSpacing;
    }

    public void setTopCellSpacing(int topCellSpacing) {
        this.topCellSpacing = topCellSpacing;
        repositionAggregateGraphicElement();
    }

    public int getBottomCellSpacing() {
        return bottomCellSpacing;
    }

    public void setBottomCellSpacing(int bottomCellSpacing) {
        this.bottomCellSpacing = bottomCellSpacing;
        repositionAggregateGraphicElement();
    }

    public int getLeftCellSpacing() {
        return leftCellSpacing;
    }

    public void setLeftCellSpacing(int leftCellSpacing) {
        this.leftCellSpacing = leftCellSpacing;
        repositionAggregateGraphicElement();
    }

    public int getRightCellSpacing() {
        return rightCellSpacing;
    }

    public void setRightCellSpacing(int rightCellSpacing) {
        this.rightCellSpacing = rightCellSpacing;
        repositionAggregateGraphicElement();
    }

    public void setOutterCellSpacing(int outterCellSpacing) {
        topCellSpacing = bottomCellSpacing = leftCellSpacing = rightCellSpacing = outterCellSpacing;
        repositionAggregateGraphicElement();
    }

    public int getInnerCellSpacing() {
        return innerCellSpacing;
    }

    public void setInnerCellSpacing(int innerCellSpacing) {
        this.innerCellSpacing = innerCellSpacing;
        repositionAggregateGraphicElement();
    }

    public void repositionAggregateGraphicElement() {
        ArrayList<LocalizableAggregateGraphicElement> localizableAggregateGraphicElementsAuxes = new ArrayList<>(localizableAggregateGraphicElements);
        if (horizontal) {
            dimension.width = leftCellSpacing + rightCellSpacing;
            dimension.height = getMaxHeight() + topCellSpacing + bottomCellSpacing;
        } else {
            dimension.width = getMaxWidth() + leftCellSpacing + rightCellSpacing;
            dimension.height = topCellSpacing + bottomCellSpacing;
        }
        localizableAggregateGraphicElements.clear();
        nextAggregateGraphicElementPosition = new Point(position.x + leftCellSpacing, position.y + topCellSpacing);
        for (LocalizableAggregateGraphicElement localizableAggregateGraphicElement : localizableAggregateGraphicElementsAuxes) {
            addAux(localizableAggregateGraphicElement);
        }
    }

    private int getMaxWidth() {
        int maxWidth = 0;
        for (LocalizableAggregateGraphicElement localizableAggregateGraphicElement : localizableAggregateGraphicElements) {
            int width = localizableAggregateGraphicElement.aggregateGraphicElement.getDimension().width;
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        return maxWidth;
    }

    private int getMaxHeight() {
        int maxHeight = 0;
        for (LocalizableAggregateGraphicElement localizableAggregateGraphicElement : localizableAggregateGraphicElements) {
            int height = localizableAggregateGraphicElement.aggregateGraphicElement.getDimension().height;
            if (height > maxHeight) {
                maxHeight = height;
            }
        }
        return maxHeight;
    }

    public void add(AggregateRectangularGraphicElement aggregateGraphicElement) {
        add(aggregateGraphicElement, Location.CENTER);
    }

    public void add(AggregateRectangularGraphicElement aggregateGraphicElement, Location location) {
        localizableAggregateGraphicElements.add(new LocalizableAggregateGraphicElement(aggregateGraphicElement, location));
        aggregateGraphicElement.setParent(this);
        repositionAggregateGraphicElement();
    }

    public void addFirst(AggregateRectangularGraphicElement aggregateGraphicElement) {
        addFirst(aggregateGraphicElement, Location.CENTER);
    }

    public void addFirst(AggregateRectangularGraphicElement aggregateGraphicElement, Location location) {
        localizableAggregateGraphicElements.add(0, new LocalizableAggregateGraphicElement(aggregateGraphicElement, location));
        aggregateGraphicElement.setParent(this);
        repositionAggregateGraphicElement();
    }

    private void addAux(LocalizableAggregateGraphicElement localizableAggregateGraphicElement) {
        addAux(localizableAggregateGraphicElement.aggregateGraphicElement, localizableAggregateGraphicElement.location);
    }

    private void addAux(AggregateRectangularGraphicElement aggregateGraphicElement, Location location) {
        localizableAggregateGraphicElements.add(new LocalizableAggregateGraphicElement(aggregateGraphicElement, location));
        //posiciona, por omissão, em cima à esquerda
        int increment;
        Point aggregateGraphicElementPositionAux = new Point(nextAggregateGraphicElementPosition);
        if (horizontal) {
            increment = aggregateGraphicElement.getDimension().width;
            dimension.width += increment;
            if (localizableAggregateGraphicElements.size() > 1) {
                dimension.width += innerCellSpacing;
            }
            nextAggregateGraphicElementPosition.x += increment + innerCellSpacing;
        } else {
            increment = aggregateGraphicElement.getDimension().height;
            dimension.height += increment;
            if (localizableAggregateGraphicElements.size() > 1) {
                dimension.height += innerCellSpacing;
            }
            nextAggregateGraphicElementPosition.y += increment + innerCellSpacing;
        }
        int height = aggregateGraphicElement.getDimension().height + topCellSpacing + bottomCellSpacing;
        if (height > dimension.height) {
            dimension.height = height;
        }
        int width = aggregateGraphicElement.getDimension().width + leftCellSpacing + rightCellSpacing;
        if (width > dimension.width) {
            dimension.width = width;
        }
        /////////////////////////////////
        //posiciona segundo a localização
        /////////////////////////////////
        if (horizontal) {
            switch (location) {
                case CENTER:
                    aggregateGraphicElementPositionAux.translate(0, (dimension.height - aggregateGraphicElement.getDimension().height - topCellSpacing - bottomCellSpacing) / 2);
                    break;
                case BOTTOM:
                    aggregateGraphicElementPositionAux.translate(0, dimension.height - aggregateGraphicElement.getDimension().height - topCellSpacing - bottomCellSpacing);
                    break;
            }
        } else {
            switch (location) {
                case CENTER:
                    aggregateGraphicElementPositionAux.translate((dimension.width - aggregateGraphicElement.getDimension().width - leftCellSpacing - rightCellSpacing) / 2, 0);
                    break;
                case RIGHT:
                    aggregateGraphicElementPositionAux.translate(dimension.width - aggregateGraphicElement.getDimension().width - leftCellSpacing - rightCellSpacing, 0);
                    break;
            }
        }
        aggregateGraphicElement.setPosition(aggregateGraphicElementPositionAux);
    }

    public void remove(AggregateRectangularGraphicElement aggregateGraphicElement) {
        //TODO com hashMap não funcionou bem porque é importante manter a ordem de inserção
        for (LocalizableAggregateGraphicElement localizableAggregateGraphicElement : localizableAggregateGraphicElements) {
            if (localizableAggregateGraphicElement.aggregateGraphicElement == aggregateGraphicElement) {
                localizableAggregateGraphicElements.remove(localizableAggregateGraphicElement);
                localizableAggregateGraphicElement.aggregateGraphicElement.setParent(null);
                return;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (LocalizableAggregateGraphicElement localizableAggregateGraphicElement : localizableAggregateGraphicElements) {
            localizableAggregateGraphicElement.aggregateGraphicElement.paint(g);
        }
    }

    @Override
    public PositionalGraphicElement getPositionalGraphicElement(Point position) {
        for (LocalizableAggregateGraphicElement localizableAggregateGraphicElement : localizableAggregateGraphicElements) {
            AggregateRectangularGraphicElement aggregateGraphicElement = localizableAggregateGraphicElement.aggregateGraphicElement;
            if (aggregateGraphicElement instanceof Reference ||
                    aggregateGraphicElement instanceof Container ||
                    aggregateGraphicElement instanceof NormalTextElement
            ) {
                PositionalGraphicElement positionalGraphicElement = aggregateGraphicElement.getPositionalGraphicElement(position);
                if (positionalGraphicElement != null) {
                    return positionalGraphicElement;
                }
            }
        }
        return super.getPositionalGraphicElement(position);
    }

    @Override
    public void setPosition(Point position) {
        super.setPosition(position);
        repositionAggregateGraphicElement();
    }

    public void deepRemove() {
        //passa os filhos para o pai e deixa de ser filho do pai
        if (!(parent instanceof Container parentContainer)) {
            return;
        }

        parentContainer.remove(this);
        parentContainer.setHorizontal(horizontal);
        for (LocalizableAggregateGraphicElement localizableAggregateGraphicElement : localizableAggregateGraphicElements) {
            parentContainer.add(localizableAggregateGraphicElement.aggregateGraphicElement, localizableAggregateGraphicElement.location);
        }
    }

    public void removeAllButFirstGraphicElements() {
        ArrayList<LocalizableAggregateGraphicElement> auxLocalizableAggregateGraphicElements = new ArrayList<>(this.localizableAggregateGraphicElements);
        for (int i = 1; i < auxLocalizableAggregateGraphicElements.size(); i++) {
            remove(auxLocalizableAggregateGraphicElements.get(i).aggregateGraphicElement);
        }
    }

    public void removeAllGraphicElements() {
        dimension = new Dimension(10, 10);
        ArrayList<LocalizableAggregateGraphicElement> auxLocalizableAggregateGraphicElements = new ArrayList<>(this.localizableAggregateGraphicElements);
        for (LocalizableAggregateGraphicElement localizableAggregateGraphicElement : auxLocalizableAggregateGraphicElements) {
            remove(localizableAggregateGraphicElement.aggregateGraphicElement);
        }
    }

    public void update() {
        dimension = new Dimension(10, 10);
        ArrayList<LocalizableAggregateGraphicElement> auxLocalizableAggregateGraphicElements = new ArrayList<>(this.localizableAggregateGraphicElements);
        for (LocalizableAggregateGraphicElement localizableAggregateGraphicElement : auxLocalizableAggregateGraphicElements) {
            remove(localizableAggregateGraphicElement.aggregateGraphicElement);
        }
        for (LocalizableAggregateGraphicElement localizableAggregateGraphicElement : auxLocalizableAggregateGraphicElements) {
            add(localizableAggregateGraphicElement.aggregateGraphicElement, localizableAggregateGraphicElement.location);
        }
    }

    public boolean intersects(Container container) {
        Rectangle rectangle = new Rectangle(position.x, position.y, dimension.width, dimension.height);
        Rectangle containerRectangle = new Rectangle(container.position.x, container.position.y, container.dimension.width, container.dimension.height);
        return rectangle.intersects(containerRectangle);
    }

    public List<LocalizableAggregateGraphicElement> getGraphicElements() {
        return localizableAggregateGraphicElements;
    }

    public ArrayList<NormalTextElement> getTextElements() {
        final ArrayList<NormalTextElement> normalTextElements = new ArrayList<>();
        for (LocalizableAggregateGraphicElement localizableAggregateGraphicElement : localizableAggregateGraphicElements) {
            if (localizableAggregateGraphicElement.aggregateGraphicElement instanceof NormalTextElement) {
                normalTextElements.add((NormalTextElement) localizableAggregateGraphicElement.aggregateGraphicElement);
            }
        }
        return normalTextElements;
    }

    public void forceNonNegativePosition() {
        if (position.x < 0) {
            position.x = 0;
        }
        if (position.y < 0) {
            position.y = 0;
        }
        repositionAggregateGraphicElement();
    }

    public class LocalizableAggregateGraphicElement implements Serializable {
        public AggregateRectangularGraphicElement aggregateGraphicElement;
        protected Location location;

        public LocalizableAggregateGraphicElement(AggregateRectangularGraphicElement aggregateGraphicElement, Location location) {
            this.aggregateGraphicElement = aggregateGraphicElement;
            this.location = location;
        }
    }
}
