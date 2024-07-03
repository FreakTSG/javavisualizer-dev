package com.aegamesi.java_visualizer.ui;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.ColecaoIteravel;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.IteradorIteravel;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.*;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaDuplaNaoOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaSimplesNaoOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaDuplaOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ordenadas.estruturas.ListaSimplesOrdenada;
import com.aegamesi.java_visualizer.model.*;
import com.aegamesi.java_visualizer.model.Frame;
import com.aegamesi.java_visualizer.ui.graphics.*;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.ArrayReference;
import com.aegamesi.java_visualizer.ui.graphics.aggregations.Reference;
import com.aegamesi.java_visualizer.ui.graphics.localizations.Location;
import com.aegamesi.java_visualizer.ui.graphics.representations.*;
import com.aegamesi.java_visualizer.ui.graphics.representations.hashtables.HashTableRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.hashtables.SortedHashTableRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.*;

import com.aegamesi.java_visualizer.utils.Vetor2D;
import com.aegamesi.java_visualizer.aed.Comparacao;
import com.github.weisj.jsvg.S;
import kotlin.comparisons.UComparisonsKt;
import ui.graphics.representations.linked_lists.SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;


import static com.aegamesi.java_visualizer.model.HeapObject.*;

public class MyCanvas extends JPanel implements MouseListener, MouseMotionListener {
    private RepresentationWithInConnectors draggedRepresentation = null;
    private Point dragOffset = null;


    public static IDSToolWindow IDSToolWindow;
    private static int i = 0;
    private final LinkedList<PositionalGraphicElement> positionalGraphicElements;
    public final HashMap<Object, RepresentationWithInConnectors> representationWithInConnectorsByOwner;

    private final HashMap<OutConnector, Connection> connectionByOutConnector;
    private final boolean compactMode;
    private final float zoom;
    private Point mousePosition = new Point(0, 0);
    private static final int START_X = 100;
    private static final int START_Y = 100;
    private static final int HORIZONTAL_SPACING = 50;
    private static final int VERTICAL_SPACING = 50;
    private Map<Object, RepresentationWithInConnectors> existingRepresentations = new HashMap<>();
    private final Map<HeapObjectKey, RepresentationWithInConnectors> contentRepresentationMap = new HashMap<>();
    private final Map<RepresentationWithInConnectors, Point> representationPositions = new HashMap<>();
    private Map<Long, Point> nodePositions = new HashMap<>();
    private String IteratorLabel ;
    private boolean showIterator = false;
    private Value currentIndex;
    private Point iteratorSquarePosition; // The position to draw the iterator square
    private Point iteratorTargetPosition; // The target position for the iterator's arrow

    private final Map<Long, HeapObject> entityMap = new HashMap<>();
    private final Map<Long, RepresentationWithInConnectors> representationMap = new HashMap<>();



    public MyCanvas(IDSToolWindow IDSToolWindow) {
        MyCanvas.IDSToolWindow = IDSToolWindow;
        compactMode = false;

        positionalGraphicElements = new LinkedList<>();
        representationWithInConnectorsByOwner = new HashMap<>();
//        wrapperById = new HashMap<>();
        connectionByOutConnector = new HashMap<>();
        addMouseListener(this);
        addMouseMotionListener(this);

        setFocusable(true);

        zoom = 1;
        setDoubleBuffered(true);
        repaint();

    }


    public void rebuildRepresentations() {
        for (RepresentationWithInConnectors representation : representationWithInConnectorsByOwner.values()) {
            if (representation instanceof DefaultRepresentation) {
                ((DefaultRepresentation<?>) representation).rebuild();
            }
        }
    }

    public void add(Connection connection) {
        final Connection oldConnection = connectionByOutConnector.get(connection.getSource());
        if (oldConnection != null) {
            oldConnection.getTarget().decrementReferenceCount();
        }
        connectionByOutConnector.put(connection.getSource(), connection);
        refresh();
    }

    public void updateRepresentations(ExecutionTrace trace) {
        createVisualRepresentations(trace, this); // You need to have this method defined based on your mock test
        revalidate();
        repaint();
    }


    public void createVisualRepresentations(ExecutionTrace trace, MyCanvas canvas) {
        System.out.println("Creating visual representations for the trace");
        Map<Long, HeapEntity> heapMap = buildHeapMap(trace);

        Map<Object, Point> storedPositions = new HashMap<>();
        for (Map.Entry<Object, RepresentationWithInConnectors> entry : canvas.representationWithInConnectorsByOwner.entrySet()) {
            storedPositions.put(entry.getKey(), entry.getValue().getPosition());
        }

        canvas.removeAllRepresentations();


        for (HeapEntity entity : trace.heap.values()) {

            if(entity instanceof HeapObject HeapObject){
                System.out.println("representationWithInConnectorsByOwner HeapObject:"+ representationWithInConnectorsByOwner );
                System.out.println("HeapObject fields fields: " + HeapObject.fields);
                System.out.println("HeapObject fields getClass: " + HeapObject.getClass());
                System.out.println("HeapObject fields getClass Name: " + HeapObject.getClass().getName());
                System.out.println("HeapObject fields getActualClass: " + HeapObject.getActualClass());
                if(!HeapObject.label.contains("anonymous")&&!HeapObject.label.contains("No")&&!HeapObject.label.contains("NoComElemento")
                        &&!HeapObject.label.contains("Base")&&!HeapObject.label.contains("Lista")&&!HeapObject.label.contains("Entrada")
                        &&!HeapObject.label.contains("Associacao")&&!HeapObject.label.contains("TabelaHashComIncrementoPorHash")
                        &&!HeapObject.label.contains("TabelaHashComIncrementoQuadratico")
                        &&!HeapObject.label.contains("Iterador")){
                    addProjectEntityRepresentation(HeapObject, canvas);
                    refreshCanvas(canvas);
                }
                if(HeapObject.fields.containsKey("name")){
                    System.out.println("Comparator found: "+HeapObject );
                    Value criterioValue = HeapObject.fields.get("name");
                    System.out.println("HeapObject fields criterioValue: " + criterioValue);
                    System.out.println("HeapObject fields criterioValue type: " + criterioValue.type);

                    if (criterioValue.type == Value.Type.STRING) {
                        String criterioName = criterioValue.stringValue;
                        Comparacao<?> comparacao = getComparatorFromString(criterioName);
                        if (comparacao != null) {
                            System.out.println("Comparator: " + comparacao);
                        } else {
                            System.out.println("Failed to get comparator from string.");
                        }
                    } else {
                        System.out.println("CRITERIO field is not a STRING.");
                    }
                }


                else {
                    System.out.println("HeapObject does not contain the field CRITERIO.");
                }
            }
        }

        System.out.println("MAPA ENTIDADES" + contentRepresentationMap);

        for (HeapEntity entity : trace.heap.values()) {
            System.out.println("Creating visual representation for entity: " + entity);

            // Check if the entity is a list of lists (HeapList) and handle it separately
            if (entity instanceof HeapList) {

                System.out.println("Processing a HeapList which might be a list of lists.");
                HeapList heapList = (HeapList) entity;
                System.out.println("HeapList items: " + heapList.items);
                System.out.println("HeapList label: " + heapList.label);
                System.out.println("HeapList id: " + heapList.id);
                System.out.println("HeapList type: " + heapList.type);
                System.out.println("HeapList Class: " + heapList.getClass());



                if(heapList.label.contains("Entrada"))    {
                    System.out.println("Entrei na tabela hash Entrada");

                    System.out.println("HeapList items: " + heapList.items);
                    System.out.println("HeapList label: " + heapList.label);


                }

                else {
                    System.out.println("Entrei no Integer");
                    createListRepresentation(heapList, heapMap, canvas);
                    refreshCanvas(canvas);
                }


            } else if (entity instanceof HeapObject ) {

                HeapObject HeapObject = (HeapObject) entity;

                System.out.println("HeapObject data:"+ HeapObject.label );
                System.out.println("O que esta aqui: " + (!HeapObject.label.contains("anonymous")&&!HeapObject.label.contains("No")&&!HeapObject.label.contains("NoComElemento")&&!HeapObject.label.contains("Base")));

                System.out.println("HeapObject fields: " + ((HeapObject) entity).fields);
                System.out.println("HeapObject label: " + entity.label);
                System.out.println("HeapObject id: " + entity.id);
                System.out.println("HeapObject type: " + entity.type);
                System.out.println("HeapObject Class: " + entity.getClass());


                Comparacao<Object> comparator = (o1, o2) -> {
                    System.out.println("Comparing objects: " + o1 + " and " + o2);
                    if (o1 instanceof HeapObject && o2 instanceof HeapObject) {
                        Long id1 = ((HeapObject) o1).id;
                        Long id2 = ((HeapObject) o2).id;
                        return id1.compareTo(id2);
                    }
                    if (o1.getClass() == o2.getClass() && o1 instanceof Comparable) {
                        return ((Comparable) o1).compareTo(o2);
                    }
                    throw new IllegalArgumentException("Unsupported object types for comparison");
                };

                if (HeapObject.label.contains("org.example.aed.Comparacao")) {
                    System.out.println("Comparator found: " );

                    Comparacao<?> comparacao = (Comparacao<?>) HeapObject.fields.get("name");
                    System.out.println("Comparator: " + comparacao);
                }
                if(isSimpleList(HeapObject)){
                    System.out.println("Entrei na lista simples nao ordenada");
                    ListaSimplesNaoOrdenada<?> simpleList=convertHeapObjectToListofLists(HeapObject, heapMap,canvas,comparator);
                    System.out.println("Simple list converted "+simpleList);
                    addSimpleListRepresentation(simpleList, canvas);

                } else if(HeapObject.label.contains("ListaDuplaNaoOrdenada")){

                   System.out.println("Entrei na lista dupla nao ordenada");
                   System.out.println("HeapObject fields: " + HeapObject.fields);
                  ListaDuplaNaoOrdenada<?> doubleList=convertHeapObjectToDoubleList(HeapObject, heapMap,canvas,comparator);
                  System.out.println("Double list converted "+doubleList);
                  addDoubleListRepresentation(doubleList, canvas);

               }else if(HeapObject.label.contains("Iterador")){

                    IteratorLabel=getVariableNameForHeapObject(trace, HeapObject.id);
                    System.out.println("Variable name for HeapObject: "+IteratorLabel);
                    System.out.println("Entrei no iterador");
                    currentIndex = HeapObject.fields.get("currentIndex");
                    addIteratorRepresentation(currentIndex);
                    showIterator = true;
                }else if(isSortedSimpleList(HeapObject, heapMap)){

                   System.out.println("Entrei na lista Simples ordenada");
                   ListaSimplesOrdenada<Object> simpleSortedList = convertHeapObjectToSortedSimpleList(HeapObject, heapMap,comparator);
                   System.out.println("Sorted SImple list converted "+simpleSortedList);
                   addSortedSimpleListRepresentation(simpleSortedList, canvas);

               }else if (HeapObject.label.contains("ListaDuplaOrdenada")) {
                   System.out.println("Entrei na lista Dupla ordenada");
                  ListaDuplaOrdenada<Object> doubleSortedList = convertHeapObjectToSortedDoubleList(HeapObject, heapMap,comparator);
                  System.out.println("Sorted Double list converted "+doubleSortedList);
                  addSortedDoubleListRepresentation(doubleSortedList, canvas);

               }
                else if (HeapObject.label.contains("TabelaHashComIncrementoPorHash")) {
                    System.out.println("TabelaHashComIncrementoPorHash found: " );
                    long tamanho= HeapObject.fields.get("tamanhoTabelaAnterior").longValue;
                    System.out.println("Tamanho da tabela: "+tamanho);
                    System.out.println("TabelaHashComIncrementoPorHash fields: " + HeapObject.fields);
                    System.out.println("TabelaHashComIncrementoPorHash label: " + HeapObject.label);
                    System.out.println("TabelaHashComIncrementoPorHash fields get: " + HeapObject.fields.get("tabela"));

                    TabelaHashComIncrementoPorHash<?,?> tabelaHashComIncrementoPorHash=convertHeapObjectToTabelaHash(HeapObject, heapMap,canvas);
                    System.out.println("TabelaHashComIncrementoPorHash: " + tabelaHashComIncrementoPorHash);



                    for (TabelaHash<?, ?>.Entrada<?, ?> entrada : tabelaHashComIncrementoPorHash.tabela) {
                        if (entrada != null && entrada.isAtivo()) {
                            Associacao<?, ?> associacao = entrada.getAssociacao();
                            System.out.println("Associacao found: ");
                            System.out.println("Associacao hashCode: " + System.identityHashCode(associacao));

                            Object valueObject = associacao.getValor();
                            System.out.println("Value Object: " + valueObject + " (identityHashCode: " + System.identityHashCode(valueObject) + ")");

                            // Create a new representation for the valueObject
                            if(isPrimitiveOrEnum(valueObject)){
                                PrimitiveOrEnumRepresentation primitiveOrEnumRepresentation = new PrimitiveOrEnumRepresentation(new Point(), valueObject, canvas);
                                canvas.add(valueObject, primitiveOrEnumRepresentation);
                                canvas.representationWithInConnectorsByOwner.put(valueObject, primitiveOrEnumRepresentation);
                            }


                            System.out.println("representationWithInConnectorsByOwner valores: " + canvas.representationWithInConnectorsByOwner);
                        }
                    }


                    canvas.add(tabelaHashComIncrementoPorHash, new HashTableRepresentation(new Point(0, 0), tabelaHashComIncrementoPorHash, canvas));

                    refreshCanvas(canvas);
                }
                else if(HeapObject.label.contains("TabelaHashComIncrementoQuadratico")){
                    System.out.println("TabelaHashComIncrementoQuadratico found: " );
                    long tamanho= HeapObject.fields.get("numeroElementos").longValue;
                    System.out.println("Tamanho da tabela: "+tamanho);
                    System.out.println("TabelaHashComIncrementoQuadratico fields: " + HeapObject.fields);
                    System.out.println("TabelaHashComIncrementoQuadratico label: " + HeapObject.label);
                    System.out.println("TabelaHashComIncrementoQuadratico fields get: " + HeapObject.fields.get("tabela"));

                    TabelaHashComIncrementoQuadratico<?,?> TabelaHashComIncrementoQuadratico=convertHeapObjectToTabelaHashComIncrementoQuadratico(HeapObject, heapMap,canvas);
                    System.out.println("TabelaHashComIncrementoPorHash: " + TabelaHashComIncrementoQuadratico);



                    for (TabelaHash<?, ?>.Entrada<?, ?> entrada : TabelaHashComIncrementoQuadratico.tabela) {
                        if (entrada != null && entrada.isAtivo()) {
                            Associacao<?, ?> associacao = entrada.getAssociacao();
                            System.out.println("Associacao found: ");
                            System.out.println("Associacao hashCode: " + System.identityHashCode(associacao));

                            Object valueObject = associacao.getValor();
                            System.out.println("Value Object: " + valueObject + " (identityHashCode: " + System.identityHashCode(valueObject) + ")");

                            // Create a new representation for the valueObject
                            if(isPrimitiveOrEnum(valueObject)){
                                PrimitiveOrEnumRepresentation primitiveOrEnumRepresentation = new PrimitiveOrEnumRepresentation(new Point(), valueObject, canvas);
                                canvas.add(valueObject, primitiveOrEnumRepresentation);
                                canvas.representationWithInConnectorsByOwner.put(valueObject, primitiveOrEnumRepresentation);
                            }


                            System.out.println("representationWithInConnectorsByOwner valores: " + canvas.representationWithInConnectorsByOwner);
                        }
                    }


                    canvas.add(TabelaHashComIncrementoQuadratico, new HashTableRepresentation(new Point(0, 0), TabelaHashComIncrementoQuadratico, canvas));

                    refreshCanvas(canvas);
                }
                else if (HeapObject.label.contains("TabelaHashOrdenada")) {
                    System.out.println("TabelaHashOrdenada found: ");
                    HeapObject noPorChaveHeapObject = (HeapObject) heapMap.get(HeapObject.fields.get("noPorChave").reference);
                    Long tamanhoValue = noPorChaveHeapObject.fields.get("tamanhoTabelaAnterior") != null ? noPorChaveHeapObject.fields.get("tamanhoTabelaAnterior").longValue : null;
                    System.out.println("Tamanho da tabela: " + tamanhoValue);
                    System.out.println("TabelaHashOrdenada fields: " + HeapObject.fields);
                    System.out.println("TabelaHashOrdenada label: " + HeapObject.label);
                    System.out.println("TabelaHashOrdenada fields get: " + HeapObject.fields.get("noPorChave"));

                    TabelaHashOrdenada<?, ?> tabelaHashOrdenada = convertHeapObjectToTabelaHashOrdenada(HeapObject, heapMap, canvas,comparator);
                    System.out.println("TabelaHashOrdenada: " + tabelaHashOrdenada);
                    ComparatorRepresentation<Comparacao<Object>> comparatorRepresentation =
                            new ComparatorRepresentation<>(new Point(START_X, START_Y - 50), comparator, canvas);
                    canvas.add(comparator, comparatorRepresentation);

                    for (TabelaHash<?, ?>.Entrada<?, ?> entrada : tabelaHashOrdenada.noPorChave.tabela) {
                        if (entrada != null && entrada.isAtivo()) {
                            Associacao<?, ?> associacao = entrada.getAssociacao();
                            System.out.println("Associacao found: ");
                            System.out.println("Associacao hashCode: " + System.identityHashCode(associacao));

                            Object valueObject = associacao.getValor();
                            System.out.println("Value Object: " + valueObject + " (identityHashCode: " + System.identityHashCode(valueObject) + ")");

                            // Create a new representation for the valueObject
                            if (isPrimitiveOrEnum(valueObject)) {
                                PrimitiveOrEnumRepresentation primitiveOrEnumRepresentation = new PrimitiveOrEnumRepresentation(new Point(), valueObject, canvas);
                                canvas.add(valueObject, primitiveOrEnumRepresentation);
                                canvas.representationWithInConnectorsByOwner.put(valueObject, primitiveOrEnumRepresentation);
                            }

                        }
                    }
                    canvas.add(tabelaHashOrdenada, new SortedHashTableRepresentation(new Point(0, 0), tabelaHashOrdenada, canvas));
                    refreshCanvas(canvas);
                }
            }
        }

        if (canvas.representationWithInConnectorsByOwner.isEmpty()) {
            System.out.println("No representations lista have been added to the canvas.");
        } else {
            System.out.println("Representations lista added to canvas: " + canvas.representationWithInConnectorsByOwner.size());
        }
        refreshCanvas(canvas);
        canvas.repaint();
    }

    private TabelaHashOrdenada<Object, Object> convertHeapObjectToTabelaHashOrdenada(HeapObject heapObject, Map<Long, HeapEntity> heapMap, MyCanvas canvas, Comparacao<Object> comparator) {
        // Extract noPorChave HeapObject
        HeapObject noPorChaveHeapObject = (HeapObject) heapMap.get(heapObject.fields.get("noPorChave").reference);
        Long tamanhoValue = noPorChaveHeapObject.fields.get("tamanhoTabelaAnterior") != null ? noPorChaveHeapObject.fields.get("tamanhoTabelaAnterior").longValue : null;
        if (tamanhoValue == null) {
            System.out.println("Field 'tamanhoTabelaAnterior' is null or not found.");
            return null;
        }
        long tamanho = tamanhoValue;

        // Create a new TabelaHashOrdenada instance
        TabelaHashOrdenada<Object, Object> tabelaHashOrdenada = new TabelaHashOrdenada<>(comparator, (int) tamanho);

        // Get the internal TabelaHashComIncrementoPorHash
        Value tabelaValue = noPorChaveHeapObject.fields.get("tabela");
        if (tabelaValue.type == Value.Type.REFERENCE) {
            HeapList tabelaList = (HeapList) heapMap.get(tabelaValue.reference);

            for (int i = 0; i < tabelaList.items.size(); i++) {
                Value entryValue = tabelaList.items.get(i);
                if (entryValue != null && entryValue.type == Value.Type.REFERENCE) {
                    HeapObject entry = (HeapObject) heapMap.get(entryValue.reference);
                    if (entry != null && entry.fields.get("ativo").booleanValue) {
                        Value associacaoValue = entry.fields.get("associacao");
                        if (associacaoValue != null && associacaoValue.type == Value.Type.REFERENCE) {
                            HeapObject associacao = (HeapObject) heapMap.get(associacaoValue.reference);
                            System.out.println("Associacao fields: " + associacao.fields);
                            Value chaveValue = associacao.fields.get("chave");
                            Value valorValue = associacao.fields.get("valor");

                            // Check if valorValue is a reference and navigate through elemento
                            if (valorValue != null && valorValue.type == Value.Type.REFERENCE) {
                                HeapObject valorObj = (HeapObject) heapMap.get(valorValue.reference);
                                if (valorObj != null) {
                                    Value elementoValue = valorObj.fields.get("elemento");
                                    if (elementoValue != null && elementoValue.type == Value.Type.REFERENCE) {
                                        HeapObject elemento = (HeapObject) heapMap.get(elementoValue.reference);
                                        valorValue = elemento.fields.get("valor");
                                    }
                                }
                            }

                            Object chave = extractActualValue(chaveValue, heapMap);
                            Object valor = extractActualValue(valorValue, heapMap);

                            System.out.println("Chave: " + chave + " Valor: " + valor);
                            tabelaHashOrdenada.inserir(chave, valor);
                        }
                    }
                } else {
                    System.out.println("Elemento value is null or not a reference for entry at index " + i + ": " + entryValue);
                }
            }
        }

        return tabelaHashOrdenada;
    }


    private TabelaHashComIncrementoPorHash<?, ?> convertHeapObjectToTabelaHash(HeapObject heapObject, Map<Long, HeapEntity> heapMap, MyCanvas canvas) {
        long tamanho = heapObject.fields.get("tamanhoTabelaAnterior").longValue;
        TabelaHashComIncrementoPorHash<Object, Object> tabelaHashComIncrementoPorHash = new TabelaHashComIncrementoPorHash<>((int) tamanho);

        long numeroElementos = heapObject.fields.get("numeroElementos").longValue;
        System.out.println("Numero de elementos: " + numeroElementos);
        if (numeroElementos > 0) {
            Value tabelaValue = heapObject.fields.get("tabela");
            if (tabelaValue.type == Value.Type.REFERENCE) {
                HeapList tabelaList = (HeapList) heapMap.get(tabelaValue.reference);

                for (Value entryValue : tabelaList.items) {
                    if (entryValue != null && entryValue.type == Value.Type.REFERENCE) {
                        HeapObject entry = (HeapObject) heapMap.get(entryValue.reference);
                        if (entry != null && entry.fields.get("ativo").booleanValue) {
                            Value associacaoValue = entry.fields.get("associacao");
                            if (associacaoValue != null && associacaoValue.type == Value.Type.REFERENCE) {
                                HeapObject associacao = (HeapObject) heapMap.get(associacaoValue.reference);
                                Value chaveValue = associacao.fields.get("chave");
                                Value valorValue = associacao.fields.get("valor");

                                Object chave = extractActualValue(chaveValue, heapMap);
                                Object valor = extractActualValue(valorValue, heapMap);
                                System.out.println("Chave: " + chave + " ValorhashCode: " + valor.hashCode());




                                System.out.println("Chave: " + chave + " Valor: " + valor);

                                tabelaHashComIncrementoPorHash.inserir(chave, valor);
                            }
                        }
                    }
                }
            }
        }
        return tabelaHashComIncrementoPorHash;
    }


    private TabelaHashComIncrementoQuadratico<?, ?> convertHeapObjectToTabelaHashComIncrementoQuadratico(HeapObject heapObject, Map<Long, HeapEntity> heapMap, MyCanvas canvas) {
        long tamanho = heapObject.fields.get("numeroElementos").longValue;
        TabelaHashComIncrementoQuadratico<Object, Object> TabelaHashComIncrementoQuadratico = new TabelaHashComIncrementoQuadratico<>((int) tamanho+5);

        long numeroElementos = heapObject.fields.get("numeroElementos").longValue;
        System.out.println("Numero de elementos: " + numeroElementos);
        if (numeroElementos > 0) {
            Value tabelaValue = heapObject.fields.get("tabela");
            if (tabelaValue.type == Value.Type.REFERENCE) {
                HeapList tabelaList = (HeapList) heapMap.get(tabelaValue.reference);

                for (Value entryValue : tabelaList.items) {
                    if (entryValue != null && entryValue.type == Value.Type.REFERENCE) {
                        HeapObject entry = (HeapObject) heapMap.get(entryValue.reference);
                        if (entry != null && entry.fields.get("ativo").booleanValue) {
                            Value associacaoValue = entry.fields.get("associacao");
                            if (associacaoValue != null && associacaoValue.type == Value.Type.REFERENCE) {
                                HeapObject associacao = (HeapObject) heapMap.get(associacaoValue.reference);
                                Value chaveValue = associacao.fields.get("chave");
                                Value valorValue = associacao.fields.get("valor");

                                Object chave = extractActualValue(chaveValue, heapMap);
                                Object valor = extractActualValue(valorValue, heapMap);
                                System.out.println("Chave: " + chave + " ValorhashCode: " + valor.hashCode());




                                System.out.println("Chave: " + chave + " Valor: " + valor);

                                TabelaHashComIncrementoQuadratico.inserir(chave, valor);

                                System.out.println("Inserted into TabelaHashComIncrementoQuadratico: Chave=" + chave + ", Valor=" + valor);
                            }
                        }
                    }
                }
            }
        }
        return TabelaHashComIncrementoQuadratico;
    }
    private Object extractActualValue(Value value, Map<Long, HeapEntity> heapMap) {
        if (value == null) {
            return null;
        }

        switch (value.type) {
            case LONG:
                return value.longValue;
            case STRING:
                return value.stringValue;
            case REFERENCE:
                HeapEntity entity = heapMap.get(value.reference);
                if (entity instanceof HeapObject) {
                    HeapObject heapObject = (HeapObject) entity;
                    System.out.println("HeapObject fields: " + heapObject.fields);
                    // Attempting to get an actual value field within the HeapObject
                    Value actualValue = heapObject.fields.get("valor");
                    if (actualValue != null) {
                        System.out.println("Extracted nested actual value: " + actualValue);
                        return extractActualValue(actualValue, heapMap);
                    }
                    return heapObject; // return the entire object if no specific value is found
                }
                return entity;
            default:
                return null;
        }
    }


    private Comparacao<?> getComparatorFromString(String criterioName) {
        Class<? extends Comparacao<?>> comparatorClass = ComparatorRegistry.getComparatorClass(criterioName);

        if (comparatorClass != null) {
            try {
                return comparatorClass.getEnumConstants()[0]; // Assuming the comparator is an enum with a single instance
            } catch (Exception e) {
                System.out.println("Error occurred while instantiating comparator: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("Class not found for criterioName: " + criterioName);
            return null;
        }
    }

    private void createListRepresentation(HeapList heapList, Map<Long, HeapEntity> heapMap, MyCanvas canvas) {
        System.out.println("Processing a HeapList which might be a list of lists.");

        if (heapList.items.isEmpty()) {
            System.out.println("HeapList is empty.");
            return;
        }

        Value firstValue = heapList.items.get(0);
        System.out.println("First value in the HeapList: " + firstValue.reference + " type" + firstValue.type);
        Class<?> componentType = determineComponentType(firstValue, heapMap);

        if (componentType != null) {
            int length = heapList.items.size();
            Object array = Array.newInstance(componentType, length);

            for (int i = 0; i < length; i++) {
                Value value = heapList.items.get(i);
                Object element = getElement(value, componentType, heapMap);
                Array.set(array, i, element);
            }
            ArrayRepresentation arrayRepresentation = new ArrayRepresentation(new Point(0, 0), array, componentType.getSimpleName(), canvas);
            canvas.add(array, arrayRepresentation);

            for (int i = 0; i < length; i++) {
                Value value = heapList.items.get(i);
                if (value.type == Value.Type.REFERENCE) {
                    long targetId = value.reference;
                    RepresentationWithInConnectors<?> targetRepresentation = representationWithInConnectorsByOwner.get(targetId);
                    if (targetRepresentation != null) {
                        ArrayReference arrayReference = new ArrayReference(array, i, Location.CENTER, true);
                        arrayRepresentation.add(arrayReference.getOutConnector(), targetRepresentation);
                    } else {
                        System.err.println("Target representation not found for ID: " + targetId);
                    }
                }
            }


            refreshCanvas(canvas);
            System.out.println("Added ArrayRepresentation to canvas.");
        } else {
            System.out.println("Component type is null. Unable to create array.");
        }
    }

    public RepresentationWithInConnectors<?> wrapObjectIfNeeded(Object targetObject) {
        if (targetObject instanceof HeapObject) {
            RepresentationWithInConnectors<?> representation = representationWithInConnectorsByOwner.get(targetObject);
            if (representation == null) {
                representation = new ProjectEntityRepresentation<>(new Point(0, 0), targetObject, this);
                add(targetObject, representation);
                representationWithInConnectorsByOwner.put(targetObject, representation);
            }
            return representation;
        } else if (targetObject instanceof HeapList) {
           // createListRepresentation((HeapList) targetObject, buildHeapMap(trace), this);
            return representationWithInConnectorsByOwner.get(targetObject);
        }
        return null;
    }

    private Class<?> determineComponentType(Value value, Map<Long, HeapEntity> heapMap) {
        switch (value.type) {
            case LONG:
                return Long.class;
            case STRING:
                return String.class;
            case REFERENCE:
                HeapEntity entity = heapMap.get(value.reference);
                if (entity instanceof HeapObject) {
                    return getHeapObjectClass((HeapObject) entity);
                }
                break;
            default:
                return Object.class;
        }
        return null;
    }

    private Class<?> getHeapObjectClass(HeapObject heapObject) {
        // Here you need to determine how to get the actual class of the HeapObject
        // This is a placeholder implementation

        // Add other cases as needed
        return Object.class;
    }

    private Object getElement(Value value, Class<?> componentType, Map<Long, HeapEntity> heapMap) {
        switch (value.type) {
            case LONG:
                return value.longValue; // Assuming LONG should map to Integer
            case STRING:
                return value.stringValue;
            case REFERENCE:
                HeapEntity entity = heapMap.get(value.reference);
                if (entity instanceof HeapObject) {
                    return entity;
                }
                break;
            default:
                return null;
        }
        return null;
    }

    private void addIteratorRepresentation(Value currentIndex) {
        this.currentIndex = currentIndex;
        System.out.println("Current Index: " + currentIndex);
        Point originalPosition = nodePositions.get(currentIndex.longValue);

        // Initial position for the iterator square
        this.iteratorSquarePosition  = new Point(originalPosition.x-50, originalPosition.y-50);

        if (currentIndex.longValue >= 0 && currentIndex.longValue <= 10) {
            this.iteratorTargetPosition  = new Point(originalPosition.x , originalPosition.y);


        }
        refreshCanvas(this);
        repaint();
    }

    private void drawIteratorWithLabel(Graphics2D g2, Point squarePosition, Point targetPosition, String label) {
        // Draw the square
        drawIteratorSquare(g2, squarePosition, label);

        // Draw the arrow
        drawArrow(g2, squarePosition, targetPosition);
    }

    public String getVariableNameForHeapObject(ExecutionTrace trace, long heapObjectId) {
        for (Frame frame : trace.frames) {
            for (Map.Entry<String, Value> entry : frame.locals.entrySet()) {
                Value value = entry.getValue();
                if (value.type == Value.Type.REFERENCE && value.reference == heapObjectId) {
                    return entry.getKey();
                }
            }
        }
        return null; // Return null if no variable name is found for the HeapObject
    }

    private void drawArrow(Graphics2D g2,Point from, Point to) {
        g2.setColor(Color.RED);
        if (g2 == null) return; // Ensure g2 is not null

        try {
            int x1= from.x;
            int y1 = from.y;
            int x2 = to.x;
            int y2 = to.y;

            g2.drawLine(x1, y1, x2, y2);

        } finally {
            g2.dispose(); // Properly dispose to avoid graphics context issues
            repaint(); // Ensure repaint is called to update the canvas
        }
    }

    private void drawIteratorSquare(Graphics2D g2,Point position,String iteratorLabel) {

        g2.setColor(Color.BLACK);   // Set iterator square color
        g2.fillRect(position.x - 5, position.y - 5, 10, 10);  // Draw a small square centered at the given position
        // Set text color
        g2.drawString(iteratorLabel, position.x - 5, position.y - 5);  // Draw the iterator label
        refreshCanvas(this);
    }

    public static boolean isSortedSimpleList(HeapObject heapObject, Map<Long, HeapEntity> heapMap) {
        if (heapObject.fields.containsKey("base") && heapObject.fields.containsKey("criterio")) {
            HeapObject baseNode = (HeapObject) heapMap.get(heapObject.fields.get("base").reference);
            // It has 'anterior' field, which might suggest it is a double list (ListaDuplaOrdenada)
            return baseNode == null || !baseNode.fields.containsKey("anterior");
            // If it doesn't have 'anterior', it's a simple sorted list (ListaSimplesOrdenada)
        }
        // If it doesn't have 'base' or 'criterio', it's not a sorted list
        return false;
    }

    public static boolean isSortedDoubleList(HeapObject heapObject, Map<Long, HeapEntity> heapMap) {
        if (heapObject.fields.containsKey("base") && heapObject.fields.containsKey("criterio")) {
            HeapObject baseNode = (HeapObject) heapMap.get(heapObject.fields.get("base").reference);
            // It has 'anterior' field, which might suggest it is a double list (ListaDuplaOrdenada)
            return baseNode != null && baseNode.fields.containsKey("anterior");
            // If it doesn't have 'anterior', it's a simple sorted list (ListaSimplesOrdenada)
        }
        // If it doesn't have 'base' or 'criterio', it's not a sorted list
        return false;
    }
    private static Map<Long, HeapEntity> buildHeapMap(ExecutionTrace trace) {
        Map<Long, HeapEntity> map = new HashMap<>();
        for (HeapEntity heapEntity : trace.heap.values()) {
            map.put(heapEntity.id, heapEntity);  // Correctly using HeapEntity
        }
        return map;
    }
    private static Point calculatePositionForListItem(int index) {
        int x = START_X + index * HORIZONTAL_SPACING;
        int y = START_Y + VERTICAL_SPACING;
        return new Point(x, y);
    }
    private static void refreshCanvas(MyCanvas canvas) {
        canvas.invalidate();
        canvas.revalidate();
        canvas.repaint();
    }

    private void addSimpleListRepresentation(ListaSimplesNaoOrdenada<?> simpleList, MyCanvas canvas) {
        int index = 0;
        for (Object item : simpleList) {
            Point position = calculatePositionForListItem(index);
           System.out.println("O que esta a passar item"+item);
           //System.out.println("O que esta a passar"+(item instanceof ColecaoIteravel<?>));
            if (item instanceof ListaSimplesNaoOrdenada<?>) {
                System.out.println("Estou aqui dentro 1\n\n");

                RepresentationWithInConnectors existingRepresentation = findRepresentationForList((ListaSimplesNaoOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    UnsortedCircularSimpleLinkedListWithBaseRepresentation nestedListRepresentation =
                            new UnsortedCircularSimpleLinkedListWithBaseRepresentation(position, (ListaSimplesNaoOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }

            }
            if (item instanceof ListaDuplaNaoOrdenada<?>) {
                System.out.println("Estou aqui dentro 1\n\n");

                RepresentationWithInConnectors existingRepresentation = findRepresentationForDoubleList((ListaDuplaNaoOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    UnsortedCircularDoubleLinkedListWithBaseRepresentation nestedListRepresentation =
                            new UnsortedCircularDoubleLinkedListWithBaseRepresentation(position, (ListaDuplaNaoOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }

            }
            if(item instanceof ListaSimplesOrdenada<?>){
                RepresentationWithInConnectors existingRepresentation = findRepresentationForSortedList((ListaSimplesOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation nestedListRepresentation =
                            new SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation(position, (ListaSimplesOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }
            }
            if(item instanceof ListaDuplaOrdenada<?>){
                RepresentationWithInConnectors existingRepresentation = findRepresentationForSortedDoubleList((ListaDuplaOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation nestedListRepresentation =
                            new SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation(position, (ListaDuplaOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }
            }else if(isPrimitiveOrEnum(item)){
               PrimitiveOrEnumRepresentation itemRepresentation = new PrimitiveOrEnumRepresentation(position, item, canvas);
               canvas.add(item, itemRepresentation);
               refreshCanvas(canvas);

            }

            index++;
        }

        System.out.println("Estou aqui dentro 2\n\n");
        UnsortedCircularSimpleLinkedListWithBaseRepresentation representation =
                new UnsortedCircularSimpleLinkedListWithBaseRepresentation(new Point(START_X, START_Y), simpleList, canvas);
        canvas.add(simpleList, representation);
        //IteradorIteravel<?> var = simpleList.iterador();
        //var.corrente();


        calculateNodePositions(simpleList);
        System.out.println("Lista valores:" + simpleList);
        existingRepresentations.put(simpleList, representation);
        canvas.representationWithInConnectorsByOwner.put(simpleList, representation);
        refreshCanvas(canvas);

    }

    private boolean isPrimitiveOrEnum(Object item) {
        return item instanceof Integer || item instanceof Long || item instanceof Double || item instanceof Float || item instanceof String || item instanceof Enum;
    }

    public RepresentationWithInConnectors getRepresentationByContent(HeapObject obj) {
        HeapObjectKey key = new HeapObjectKey(obj);
        System.out.println("Retrieving representation for key: " + key);
        return contentRepresentationMap.get(key);
    }

    public void addRepresentationByContent(HeapObject obj, RepresentationWithInConnectors representation) {
        HeapObjectKey key = new HeapObjectKey(obj);
        System.out.println("Adding representation for key: " + key + " Value: " + representation);
        contentRepresentationMap.put(key, representation);
    }


    private RepresentationWithInConnectors findRepresentationForList(ListaSimplesNaoOrdenada<?> list) {
        for (Map.Entry<Object, RepresentationWithInConnectors> entry : existingRepresentations.entrySet()) {
            if (entry.getKey() instanceof ListaSimplesNaoOrdenada<?> && list.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private RepresentationWithInConnectors findRepresentationForDoubleList(ListaDuplaNaoOrdenada<?> list) {
        for (Map.Entry<Object, RepresentationWithInConnectors> entry : existingRepresentations.entrySet()) {
            if (entry.getKey() instanceof ListaDuplaNaoOrdenada<?> && list.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private RepresentationWithInConnectors findRepresentationForSortedList(ListaSimplesOrdenada<?> list) {
        for (Map.Entry<Object, RepresentationWithInConnectors> entry : existingRepresentations.entrySet()) {
            System.out.println("O if esta a dar:  " + (entry.getKey() instanceof ListaSimplesOrdenada<?> && list.equals(entry.getKey())));
            if (entry.getKey() instanceof ListaSimplesOrdenada<?> && list.equals(entry.getKey())) {
                System.out.println("O resultado da procura: " + entry.getValue());
                return entry.getValue();
            }
        }
        return null;
    }

    private RepresentationWithInConnectors findRepresentationForSortedDoubleList(ListaDuplaOrdenada<?> list) {
        for (Map.Entry<Object, RepresentationWithInConnectors> entry : existingRepresentations.entrySet()) {
            if (entry.getKey() instanceof ListaDuplaOrdenada<?> && list.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private RepresentationWithInConnectors findRepresentationForListDoubleUnsorted(ListaDuplaNaoOrdenada<?> list) {
        for (Map.Entry<Object, RepresentationWithInConnectors> entry : existingRepresentations.entrySet()) {
            System.out.println("O if esta a dar:  " + (entry.getKey() instanceof ListaDuplaNaoOrdenada<?> && list.equals(entry.getKey())));
            if (entry.getKey() instanceof ListaDuplaNaoOrdenada<?> && list.equals(entry.getKey())) {
                System.out.println("O resultado da procura: " + entry.getValue());
                return entry.getValue();
            }
        }
        System.out.println("Este resultado é null ");
        return null;
    }


    private void addDoubleListRepresentation(ListaDuplaNaoOrdenada<?> doubleList, MyCanvas canvas) {
        // Logic to create a visual representation for the double list and add it to the canvas
        int index = 0;
        for (Object item : doubleList) {
            Point position = calculatePositionForListItem(index);

            if (item instanceof ListaDuplaNaoOrdenada<?>) {
                RepresentationWithInConnectors existingRepresentation = findRepresentationForListDoubleUnsorted((ListaDuplaNaoOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    System.out.println("Encontrei uma representacao\n\n");
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    UnsortedCircularDoubleLinkedListWithBaseRepresentation nestedListRepresentation =
                            new UnsortedCircularDoubleLinkedListWithBaseRepresentation(position, (ListaDuplaNaoOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }
            }if (item instanceof ListaSimplesNaoOrdenada<?>) {
                System.out.println("Estou aqui dentro 1\n\n");

                RepresentationWithInConnectors existingRepresentation = findRepresentationForList((ListaSimplesNaoOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    UnsortedCircularSimpleLinkedListWithBaseRepresentation nestedListRepresentation =
                            new UnsortedCircularSimpleLinkedListWithBaseRepresentation(position, (ListaSimplesNaoOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }

            }

            if(item instanceof ListaDuplaOrdenada<?>) {
                RepresentationWithInConnectors existingRepresentation = findRepresentationForSortedDoubleList((ListaDuplaOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation nestedListRepresentation =
                            new SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation(position, (ListaDuplaOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }
            }



            if(item instanceof ListaSimplesOrdenada<?>){
                RepresentationWithInConnectors existingRepresentation = findRepresentationForSortedList((ListaSimplesOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation nestedListRepresentation =
                            new SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation(position, (ListaSimplesOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }
            }
            else if(isPrimitiveOrEnum(item)){
                System.out.println("Estou so  a ver aqui ListaDouble\n\n");
                PrimitiveOrEnumRepresentation itemRepresentation = new PrimitiveOrEnumRepresentation(position, item, canvas);
                canvas.add(item, itemRepresentation);
                refreshCanvas(canvas);

                index++;
            }

        }
        UnsortedCircularDoubleLinkedListWithBaseRepresentation representation =
                new UnsortedCircularDoubleLinkedListWithBaseRepresentation(new Point(START_X, START_Y), doubleList, canvas);
        canvas.add(doubleList, representation);
        calculateNodePositions(doubleList);

       System.out.println("nodePositions:" + nodePositions);
        representation.update();
        existingRepresentations.put(doubleList, representation);
        canvas.representationWithInConnectorsByOwner.put(doubleList, representation);
        refreshCanvas(canvas);

    }

    private void calculateNodePositions(ListaDuplaNaoOrdenada<?> doubleList) {
        Point var =representationWithInConnectorsByOwner.get(doubleList).getPosition();
        System.out.println("Numero elementos: "+doubleList.getNumeroElementos());
        int offset = 54;
        for (i=0; i<=doubleList.getNumeroElementos(); i++){
            Point position = new Point(var.x + i * offset+55, var.y+28);
            nodePositions.put((long) i, position);
        }
    }
    private void calculateNodePositions(ListaDuplaOrdenada<?> doubleList) {
        Point var = representationWithInConnectorsByOwner.get(doubleList).getPosition();
        System.out.println("Numero elementos: " + doubleList.getNumeroElementos());
        int offset = 54;
        for (i = 0; i <= doubleList.getNumeroElementos(); i++) {
            Point position = new Point(var.x + i * offset + 55, var.y + 28);
            System.out.println("Position: " + position);
            nodePositions.put((long) i, position);
        }
    }
    private void calculateNodePositions(ListaSimplesNaoOrdenada<?> simpleList) {
        Point var = representationWithInConnectorsByOwner.get(simpleList).getPosition();
        System.out.println("Numero elementos: " + simpleList.getNumeroElementos());
        int offset = 42;

        for (int i = 0; i <= simpleList.getNumeroElementos(); i++) {
            Point position = new Point(var.x + i * offset+45, var.y+28); // Apply the offset incrementally
            nodePositions.put((long) i, position);
        }

        System.out.println("nodePositions: " + nodePositions);
    }




    private void addProjectEntityRepresentation(HeapObject entity, MyCanvas canvas) {
        // Determine a posição inicial para a representação do objeto
        System.out.println("Dentro da funcao addProjectEntity o objeto: " + entity);


        System.out.println("entity: " + entity);
        System.out.println("entity getClass: " + entity.getClass());

        ProjectEntityRepresentation<Object> entityRepresentation = new ProjectEntityRepresentation<>(new Point(0,0), entity, canvas);

        HeapObject heapObject = (HeapObject) entity;
        long entityId = heapObject.id;
        System.out.println("entityId: " + entityId);
        System.out.println("entity: " + entity);
        System.out.println("heapObject: " + heapObject);
        entityMap.put(entityId, heapObject);
        System.out.println("entityMap: " + entityMap);
        representationMap.put(entityId, entityRepresentation);
        representationWithInConnectorsByOwner.put(entity, entityRepresentation);


        canvas.add(entity, entityRepresentation);
        entityRepresentation.update();
        System.out.println("entityRepresentation: " + entity + " added to " + entityRepresentation);


        canvas.addRepresentationByContent((HeapObject) entity, entityRepresentation);
        System.out.println("entityRepresentation: " + entity+"added to" + entityRepresentation);
        existingRepresentations.put(entity, entityRepresentation);



        refreshCanvas(canvas);

    }

    public HeapObject getHeapObjectById(long id) {
        return entityMap.get(id);
    }

    private void calculateNodePositions(ListaSimplesOrdenada<?> simpleList) {
        Point var = representationWithInConnectorsByOwner.get(simpleList).getPosition();
        System.out.println("Numero elementos: " + simpleList.getNumeroElementos());
        int offset = 42;

        for (int i = 0; i <= simpleList.getNumeroElementos(); i++) {
            Point position = new Point(var.x + i * offset+45, var.y+28); // Apply the offset incrementally
            nodePositions.put((long) i, position);
        }

        System.out.println("nodePositions: " + nodePositions);
    }




    private void addSortedSimpleListRepresentation(ListaSimplesOrdenada<?> simpleList, MyCanvas canvas) {
        int index = 0;
        for (Object item : simpleList) {
            Point position = calculatePositionForListItem(index);
            System.out.println("Lista ordenada fora do if" + item);

            if (item instanceof ListaDuplaNaoOrdenada<?>) {
                System.out.println("Estou aqui dentro 1\n\n");
                RepresentationWithInConnectors existingRepresentation = findRepresentationForDoubleList((ListaDuplaNaoOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    UnsortedCircularDoubleLinkedListWithBaseRepresentation nestedListRepresentation =
                            new UnsortedCircularDoubleLinkedListWithBaseRepresentation(position, (ListaDuplaNaoOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }
            }
            if(item instanceof ListaSimplesOrdenada<?>){
                System.out.println("Estou aqui dentro 2\n\n");
                RepresentationWithInConnectors existingRepresentation = findRepresentationForSortedList((ListaSimplesOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation nestedListRepresentation =
                            new SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation(position, (ListaSimplesOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }
            }
            if(item instanceof ListaDuplaOrdenada<?>){
                RepresentationWithInConnectors existingRepresentation = findRepresentationForSortedDoubleList((ListaDuplaOrdenada<?>) item);
                if (existingRepresentation != null) {
                    // Connect to existing representation
                    canvas.add(item, existingRepresentation);
                    refreshCanvas(canvas);
                } else {
                    System.out.println("Existing Representation is null\n\n");
                    // Create new representation
                    SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation nestedListRepresentation =
                            new SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation(position, (ListaDuplaOrdenada<?>) item, canvas);
                    //nestedListRepresentation.update();
                    canvas.add(item, nestedListRepresentation);
                    representationWithInConnectorsByOwner.put(item, nestedListRepresentation);
                    existingRepresentations.put(item, nestedListRepresentation);
                    refreshCanvas(canvas);
                }
            }
            else if (isPrimitiveOrEnum(item)) {
                PrimitiveOrEnumRepresentation itemRepresentation = new PrimitiveOrEnumRepresentation(position, item, canvas);
                canvas.add(item, itemRepresentation);
            }
            index++;
        }

        Comparacao<Object> comparador = (Comparacao<Object>) simpleList.getComparador();
        System.out.println("Comparador: " + comparador);

        ComparatorRepresentation<Comparacao<Object>> comparatorRepresentation =
                new ComparatorRepresentation<>(new Point(START_X, START_Y - 50), comparador, canvas);
        canvas.add(comparador, comparatorRepresentation);

        SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation sortedSingleList =
                new SortedCircularSimpleLinkedListWithBaseMaxOrderRepresentation(new Point(START_X, START_Y), simpleList, canvas); // Adjust the position as needed
        canvas.add(simpleList, sortedSingleList);


System.out.println("representationwithinconnectors:" + representationWithInConnectorsByOwner);

        calculateNodePositions(simpleList);






        System.out.println("Lista valores ordenada:" + simpleList);
        existingRepresentations.put(simpleList, sortedSingleList);
        canvas.representationWithInConnectorsByOwner.put(simpleList, sortedSingleList);
        refreshCanvas(canvas);
    }



    private void addSortedDoubleListRepresentation(ListaDuplaOrdenada<?> doubleList, MyCanvas canvas) {
        int index = 0;
        for (Object item : doubleList) {
            Point position = calculatePositionForListItem(index);
            if (isPrimitiveOrEnum(item)){
                PrimitiveOrEnumRepresentation itemRepresentation = new PrimitiveOrEnumRepresentation(position, item, canvas);
                canvas.add(item, itemRepresentation);
            }
            index++;
        }
        Comparacao<Object> comparador = (Comparacao<Object>) doubleList.getComparador();
        System.out.println("Comparador: " + comparador);

        ComparatorRepresentation<Comparacao<Object>> comparatorRepresentation =
                new ComparatorRepresentation<>(new Point(START_X, START_Y - 50), comparador, canvas);
        canvas.add(comparador, comparatorRepresentation);


        SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation sortedDoubleList =
                new SortedCircularDoubleLinkedListWithBaseMaxOrderRepresentation(new Point(START_X, START_Y), doubleList, canvas); // Adjust the position as needed
        canvas.add(doubleList, sortedDoubleList);



        sortedDoubleList.update();
        calculateNodePositions(doubleList);
        System.out.println("Lista valores:" + doubleList);
        existingRepresentations.put(doubleList, sortedDoubleList);
        canvas.representationWithInConnectorsByOwner.put(doubleList, sortedDoubleList);
        refreshCanvas(canvas);
    }



    private ListaSimplesOrdenada<Object> convertHeapObjectToSortedSimpleList(HeapObject heapObject, Map<Long, HeapEntity> heapMap, Comparacao<Object> comparator) {
        Value comparatorValue = heapObject.fields.get("criterio");

        if (comparatorValue == null) {
            throw new IllegalArgumentException("Comparator value is null.");
        }

        if (comparatorValue.type != Value.Type.REFERENCE) {
            throw new IllegalArgumentException("Comparator value is not a reference.");
        }

        // Get the comparator object from the heap
        HeapObject comparatorObject = (HeapObject) heapMap.get(comparatorValue.reference);
        if (comparatorObject == null) {
            throw new IllegalArgumentException("Comparator object is null.");
        }



        // Create the sorted list with the comparator
        ListaSimplesOrdenada<Object> sortedSingleList = new ListaSimplesOrdenada<>(comparator);

        Long baseRef = heapObject.fields.get("base").reference;
        HeapObject baseNode = (HeapObject) heapMap.get(baseRef);

        if (baseNode == null) {
            System.out.println("Base node is null.");
            return sortedSingleList;
        }

        Long currentNodeRef = baseNode.fields.get("seguinte").reference;

        Set<Long> visitedNodeIds = new HashSet<>(); // To detect cycles

        while (currentNodeRef != null && !currentNodeRef.equals(baseRef)) {
            if (visitedNodeIds.contains(currentNodeRef)) {
                System.out.println("Cycle detected or reached base node again. Terminating.");
                break;
            }
            visitedNodeIds.add(currentNodeRef);

            HeapObject currentNode = (HeapObject) heapMap.get(currentNodeRef);
            if (currentNode == null) {
                System.out.println("Current node is null.");
                break;
            }

            Value elementValue = currentNode.fields.get("elemento");
            System.out.println("Element value: " + elementValue.type);
            if (elementValue != null && elementValue.type == Value.Type.REFERENCE) {
                HeapObject customObject = (HeapObject) heapMap.get(elementValue.reference);
                System.out.println("O que esta a passar dentro do custom Object: " + customObject);
                //Object actualData = extractActualData(customObject);
                if (customObject != null) {
                    sortedSingleList.inserir(customObject);
                }
                 else {
                    System.out.println("Actual data is null for node with ID: " + currentNode.id);
                }
            }
            else {
                Object element = elementValue.getActualValue();
                if (element != null) {
                    sortedSingleList.inserir(element);

                }
            }


            currentNodeRef = currentNode.fields.get("seguinte").reference;
        }

        System.out.println("Final sorted list: " + sortedSingleList);
        return sortedSingleList;
    }


  private ListaDuplaOrdenada<Object> convertHeapObjectToSortedDoubleList(HeapObject heapObject, Map<Long, HeapEntity> heapMap, Comparacao<Object> comparator) {
      Value comparatorValue = heapObject.fields.get("criterio");

      if (comparatorValue == null) {
          throw new IllegalArgumentException("Comparator value is null.");
      }

      if (comparatorValue.type != Value.Type.REFERENCE) {
          throw new IllegalArgumentException("Comparator value is not a reference.");
      }

      // Get the comparator object from the heap
      HeapObject comparatorObject = (HeapObject) heapMap.get(comparatorValue.reference);
      if (comparatorObject == null) {
          throw new IllegalArgumentException("Comparator object is null.");
      }

      ListaDuplaOrdenada<Object> sortedDoubleList = new ListaDuplaOrdenada<>(comparator);
      Long baseRef = heapObject.fields.get("base").reference;
      HeapObject baseNode = (HeapObject) heapMap.get(baseRef);

      if (baseNode == null) {
          System.out.println("Base node is null.");
          return sortedDoubleList;
      }

      Long currentNodeRef = baseNode.fields.get("seguinte").reference;

      Set<Long> visitedNodeIds = new HashSet<>(); // To detect cycles

      while (currentNodeRef != null && !currentNodeRef.equals(baseRef)) {
          if (visitedNodeIds.contains(currentNodeRef)) {
              System.out.println("Cycle detected or reached base node again. Terminating.");
              break;
          }
          visitedNodeIds.add(currentNodeRef);

          HeapObject currentNode = (HeapObject) heapMap.get(currentNodeRef);
          if (currentNode == null) {
              System.out.println("Current node is null.");
              break;
          }

          Value elementValue = currentNode.fields.get("elemento");
          System.out.println("Element value: " + elementValue.type);
          if (elementValue != null && elementValue.type == Value.Type.REFERENCE) {
              HeapObject customObject = (HeapObject) heapMap.get(elementValue.reference);
              System.out.println("O que esta a passar dentro do custom Object: " + customObject);
              //Object actualData = extractActualData(customObject);
              if (customObject != null) {
                  sortedDoubleList.inserir(customObject);
              }
              else {
                  System.out.println("Actual data is null for node with ID: " + currentNode.id);
              }
          }
          else {
              Object element = elementValue.getActualValue();
              if (element != null) {
                  sortedDoubleList.inserir(element);

              }
          }


          currentNodeRef = currentNode.fields.get("seguinte").reference;
      }

      System.out.println("Final sorted list: " + sortedDoubleList);
      return sortedDoubleList;
  }



    private ListaDuplaNaoOrdenada<?> convertHeapObjectToDoubleList(HeapObject heapObject, Map<Long, HeapEntity> heapMap, MyCanvas canvas,Comparacao<Object> comparator) {
        ListaDuplaNaoOrdenada<Object> doubleList = new ListaDuplaNaoOrdenada<>();
        Value headValue = heapObject.fields.get("base");
        headValue = ((HeapObject)heapMap.get(headValue.reference)).fields.get("seguinte");

        Set<Long> visitedNodeIds = new HashSet<>();


        while (headValue != null && headValue.type == Value.Type.REFERENCE && headValue.reference != 0) {
            if (visitedNodeIds.contains(headValue.reference)) {
                break; // Detect a cycle or the traversal has reached the base node again
            }
            visitedNodeIds.add(headValue.reference); // Track the visited nodes

            HeapObject currentNode = (HeapObject) heapMap.get(headValue.reference);
            Value dataValue = currentNode.fields.get("elemento");
            // Check if the dataValue is a reference to another list ( another HeapObject)
            if (dataValue != null && dataValue.type == Value.Type.REFERENCE) {
                HeapObject innerListObject = (HeapObject) heapMap.get(dataValue.reference);
                System.out.println("Esta a entrar no primeiro if: " + isSimpleList(innerListObject));
                System.out.println("Esta a entrar no segundo if: "+ innerListObject.label + "||||||||" + innerListObject.label.contains("ListaDuplaNaoOrdenada"));
                if (isSimpleList(innerListObject)) {
                    // It's a simple list, convert it to a simple list and add it to the current list
                    ListaSimplesNaoOrdenada<?> innerList = convertHeapObjectToSimpleList(innerListObject, heapMap);
                    doubleList.inserir(innerList);
                } else if (innerListObject.label.contains("ListaDuplaNaoOrdenada")) {
                    ListaDuplaNaoOrdenada<?> innerList = convertHeapObjectToDoubleList(innerListObject, heapMap,canvas,comparator);
                    doubleList.inserir(innerList);
                    //addDoubleListRepresentation(innerList, canvas);
                } else if (innerListObject.label.contains("ListaSimplesOrdenada")) {
                    ListaSimplesOrdenada<?> innerList = convertHeapObjectToSortedSimpleList(innerListObject, heapMap,comparator);
                    doubleList.inserir(innerList);
                }else if (innerListObject.label.contains("ListaDuplaOrdenada")) {
                   ListaDuplaOrdenada<?> innerList = convertHeapObjectToSortedDoubleList(innerListObject, heapMap,comparator);
                   doubleList.inserir(innerList);
                }else {
                    // Attempt to retrieve the actual data for custom objects
                    HeapObject customObject = (HeapObject) heapMap.get(dataValue.reference);
                    System.out.println("O que esta a passar dentro do custom Object: " + customObject);
                    //Object actualData = extractActualData(customObject);
                    if (customObject != null) {
                        doubleList.inserir(customObject);
                    }
                }
            } else if (dataValue != null) {
                // It's a direct value, add it to the current list
                Object actualData = dataValue.getActualValue();
                if (actualData != null) {
                    doubleList.inserir(actualData);
                }
            }

            headValue = currentNode.fields.get("seguinte"); // Move to the next node
        }
        return doubleList;
    }



    private ListaSimplesNaoOrdenada<Object> convertHeapObjectToSimpleList(HeapObject heapObject, Map<Long, HeapEntity> heapMap) {
        ListaSimplesNaoOrdenada<Object> simpleList = new ListaSimplesNaoOrdenada<>();
        Value headValue = heapObject.fields.get("base"); // Start from the 'base', not 'noFinal'
        Set<Long> visitedNodeIds = new HashSet<>(); // To detect cycles

        // The base node itself should not be added, so move to the first actual element
        headValue = ((HeapObject)heapMap.get(headValue.reference)).fields.get("seguinte");

        while (headValue != null && headValue.type == Value.Type.REFERENCE && headValue.reference != 0) {
            if (visitedNodeIds.contains(headValue.reference)) {
                System.out.println("Cycle detected or reached base node again. Terminating.");
                break; // Detect a cycle or the traversal has reached the base node again
            }
            visitedNodeIds.add(headValue.reference); // Track the visited nodes

            HeapEntity entity = heapMap.get(headValue.reference);
            if (!(entity instanceof HeapObject)) {
                break;
            }
            HeapObject currentNode = (HeapObject) entity;

            if (currentNode.fields.get("elemento") != null) { // Make sure it's not the base sentinel node
                Value dataValue = currentNode.fields.get("elemento");
                if (dataValue != null) {
                    // Retrieve the HeapObject directly
                    if (dataValue.type == Value.Type.REFERENCE) {
                        HeapObject innerObject = (HeapObject) heapMap.get(dataValue.reference);
                        if (innerObject != null) {
                            simpleList.inserir(innerObject);
                            System.out.println("Inserted HeapObject: " + innerObject);
                        } else {
                            System.out.println("Inner Object is null for reference: " + dataValue.reference);
                        }
                    } else {
                        Object actualData = dataValue.getActualValue();
                        if (actualData != null) {
                            simpleList.inserir(actualData);
                            System.out.println("Inserted Actual Data: " + actualData);
                        } else {
                            System.out.println("Actual Data is null for node with ID: " + currentNode.id);
                        }
                    }
                } else {
                    System.out.println("Data Value is null for node with ID: " + currentNode.id);
                }
            }

            headValue = currentNode.fields.get("seguinte"); // Move to the next node
        }
        return simpleList;
    }
    private ListaSimplesNaoOrdenada<Object> convertHeapObjectToListofLists(HeapObject heapObject, Map<Long, HeapEntity> heapMap, MyCanvas canvas,Comparacao<Object> comparator) {
        ListaSimplesNaoOrdenada<Object> list = new ListaSimplesNaoOrdenada<>();
        Value headValue = heapObject.fields.get("base"); // Start from the 'base'
        headValue = ((HeapObject)heapMap.get(headValue.reference)).fields.get("seguinte"); // Skip the sentinel node

        Set<Long> visitedNodeIds = new HashSet<>(); // To detect cycles

        while (headValue != null && headValue.type == Value.Type.REFERENCE && headValue.reference != 0) {
            if (visitedNodeIds.contains(headValue.reference)) {
                break; // Detect a cycle or the traversal has reached the base node again
            }
            visitedNodeIds.add(headValue.reference); // Track the visited nodes

            HeapObject currentNode = (HeapObject) heapMap.get(headValue.reference);
            Value dataValue = currentNode.fields.get("elemento");
            // Check if the dataValue is a reference to another list ( another HeapObject)
            if (dataValue != null && dataValue.type == Value.Type.REFERENCE) {
                HeapObject innerListObject = (HeapObject) heapMap.get(dataValue.reference);
                System.out.println("Esta a entrar no primeiro if: " + isSimpleList(innerListObject));
                System.out.println("Esta a entrar no segundo if: "+ innerListObject.label + "||||||||" + innerListObject.label.contains("ListaDuplaNaoOrdenada"));
                if (isSimpleList(innerListObject)) {
                    // It's a simple list, convert it to a simple list and add it to the current list
                    ListaSimplesNaoOrdenada<?> innerList = convertHeapObjectToSimpleList(innerListObject, heapMap);
                    System.out.println("Lista valores:" + innerList+innerList);
                    list.inserir(innerList);
                    System.out.println("Inserted Simple List: " + innerList);
                } else if (innerListObject.label.contains("ListaDuplaNaoOrdenada")) {
                    ListaDuplaNaoOrdenada<?> innerList = convertHeapObjectToDoubleList(innerListObject, heapMap,canvas,comparator);
                    list.inserir(innerList);
                    //addDoubleListRepresentation(innerList, canvas);
                } else if (innerListObject.label.contains("ListaSimplesOrdenada")) {
                    ListaSimplesOrdenada<?> innerList = convertHeapObjectToSortedSimpleList(innerListObject, heapMap,comparator);
                    list.inserir(innerList);
                }else if (innerListObject.label.contains("ListaDuplaOrdenada")) {
                    ListaDuplaOrdenada<?> innerList = convertHeapObjectToSortedDoubleList(innerListObject, heapMap,comparator);
                    list.inserir(innerList);
                }else {
                    // Attempt to retrieve the actual data for custom objects
                    HeapObject customObject = (HeapObject) heapMap.get(dataValue.reference);
                    System.out.println("O que esta a passar dentro do custom Object: " + customObject);
                    //Object actualData = extractActualData(customObject);
                    if (customObject != null) {
                        list.inserir(customObject);
                        System.out.println("Inserted Custom Object: " + customObject);
                    }
                }
            } else if (dataValue != null) {
                // It's a direct value, add it to the current list
                Object actualData = dataValue.getActualValue();
                if (actualData != null) {
                    list.inserir(actualData);
                    System.out.println("Inserted Actual Data: " + actualData);
                }
            }

            headValue = currentNode.fields.get("seguinte"); // Move to the next node
        }
        return list;
    }



    private void removeAllRepresentations() {
        representationWithInConnectorsByOwner.clear();
        connectionByOutConnector.clear();
        positionalGraphicElements.clear();
    }

    private Object extractActualData(HeapObject customObject) {
        if (customObject == null) {
            return null;
        }

        // Assuming customObject has a field that holds the actual data
        // Modify this according to your actual object structure
        Field[] fields = customObject.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(customObject);
                if (value != null) {
                    return value;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform transform = new AffineTransform(g2.getTransform());
        transform.scale(zoom, zoom);
        g2.setTransform(transform);

        for (RepresentationWithInConnectors representationWithInConnectors : representationWithInConnectorsByOwner.values()) {
            //System.out.println("Representation: " + representationWithInConnectors + " Position: " + representationWithInConnectors.getPosition());
            representationWithInConnectors.paint(g);
        }
        for (Connection connection : connectionByOutConnector.values()) {
            connection.paint(g);
        }
        for (PositionalGraphicElement positionalGraphicElement : positionalGraphicElements) {
            positionalGraphicElement.paint(g);
        }

        if (showIterator && iteratorSquarePosition != null && iteratorTargetPosition != null) {
            drawIteratorWithLabel(g2,iteratorSquarePosition,iteratorTargetPosition,IteratorLabel);
        }
//        g.drawString( mousePosition.x + "," + mousePosition.y + "   ZOOM=" + zoom + "   SIZE=" + getSize().width + "x" + getSize().height, 10, 10);
//        g.drawString( mousePosition.x + "," + mousePosition.y + "   ZOOM=" + zoom + "   SIZE=" + getSize().width + "x" + getSize().height, 10, 10);
//        g.drawString("ActualFilename=->" + actualFilename + "<-" + mousePosition.x + "," + mousePosition.y + "   ZOOM=" + zoom + "   SIZE=" + getSize().width + "x" + getSize().height, 10, 10);
//        g.drawString("showVar=" + showVar, 10, 10);


    }

    public void remove(Connection connection) {
        final RepresentationWithInConnectors representationWithInConnectors = connection.getTarget();
        connectionByOutConnector.remove(connection.getSource());
        representationWithInConnectors.decrementReferenceCount();
    }

//    public WrapperWithValue getWrapperWithValue(Object value) {
////        for (Wrapper wrapper : representationWithInConnectorsByOwner.keySet()) {
//        for (Wrapper wrapper : wrapperById.values()) {
//            if (wrapper instanceof WrapperWithValue && ((WrapperWithValue<?>) wrapper).getValue() == value) {
//                return ((WrapperWithValue<?>) wrapper);
//            }
//        }
//        return null; //nunca deve acontecer
//    }


    public static Object getIDSToolWindow() {
        return IDSToolWindow;
    }

    /*public String getFirstReferenceTo(Representation representation) {
       String firstVariableReference = getFirstVariableReferenceTo(representation);
       if (firstVariableReference != null) {
           return firstVariableReference;
       }
       //identificar todas as conexões cujo destino seja esta representação
       for (Connection connection : connectionByOutConnector.values()) {
           if (connection.getTarget() == representation) {
               //se o ponto de partida for um outConnector de uma ArrayReference
               Reference reference = connection.getSource().getReference();
               if (reference instanceof ArrayReference arrayReference) {
                   //guardar o index
                   int index = arrayReference.getIndex();
                   //chamar este método para a representação do array + [index]
                   return getFirstReferenceTo(getRepresentationWithInConnectors(
                           arrayReference.getArray())) + "[" + index + "]";
               } else {
                   //se o ponto de partida for um outConnector de uma FieldReference
                   FieldReference fieldReference = (FieldReference) reference;
                   //se for um nó de uma lista
                   WrapperWithValue<?> fieldOwnerObjectOfWrapper = fieldReference.getFieldOwnerObjectOfWrapper();
                   if (fieldOwnerObjectOfWrapper instanceof LinkedListNodeWrapper) {
                       LinkedListRepresentation linkedListRepresentation = (LinkedListRepresentation) getRepresentationWithInConnectors(((LinkedListNodeWrapper<?>) fieldOwnerObjectOfWrapper).getLinkedListOrSortedHashTableWrapper());
                       //chamar este método para a representação da lista + .get(i)
                       return getFirstReferenceTo(linkedListRepresentation)
                               + ".consultarPorIndice(" + (fieldOwnerObjectOfWrapper).getIndex() + ")";
                   }

                   Point sourceRepresentationPosition = getRepresentationWithInConnectors(fieldOwnerObjectOfWrapper).getPosition();
                   Representation movableRepresentation = getMovableRepresentation(sourceRepresentationPosition.x, sourceRepresentationPosition.y);

                   if (fieldOwnerObjectOfWrapper instanceof AssociationWrapper) {
                       //se for uma association de uma hashtable
                       //chamar este método para a representação da hashtable + .get(chave)
                       RepresentationWithInConnectors representationWithInConnectors = getRepresentationWithInConnectors(((AssociationWrapper) fieldOwnerObjectOfWrapper).getGeneralHashTableWrapper());
                       if (representationWithInConnectors instanceof HashTableRepresentation hashTableRepresentation) {

                           WrapperWithValue keyWrapper = (WrapperWithValue) ((Associacao) ((AssociationWrapper) fieldOwnerObjectOfWrapper).getValue()).getChave();
                           String firstReferenceToKeyWrapper = getFirstReferenceTo(keyWrapper);
                           String keyCode = firstReferenceToKeyWrapper.equals(Constants.UNKOWN_REF) ?
                                   Utils.getCode(keyWrapper.getValue()) :
                                   firstReferenceToKeyWrapper;

                           return getFirstReferenceTo(hashTableRepresentation)
                                   + ".consultar(" + keyCode + ")";
                       } else {
                           SortedHashTableRepresentation sortedHashTableRepresentation = (SortedHashTableRepresentation) representationWithInConnectors;
                           WrapperWithValue keyWrapper = (WrapperWithValue) ((Associacao) ((AssociationWrapper) fieldOwnerObjectOfWrapper).getValue()).getChave();
                           String firstReferenceToKeyWrapper = getFirstReferenceTo(keyWrapper);
                           String keyCode = firstReferenceToKeyWrapper.equals(Constants.UNKOWN_REF) ?
                                   Utils.getCode(keyWrapper.getValue()) :
                                   firstReferenceToKeyWrapper;

                           return getFirstReferenceTo(sortedHashTableRepresentation)
                                   + ".consultar(" + keyCode + ")";
                       }
                   }
                   //se for de uma ProjectEntity
                  //chamar este método para a representação da projectentity + .getXXXXX()
                        Point sourceRepresentationPosition = getRepresentationWithInConnectors(fieldOwnerObjectOfWrapper).getPosition();
                  Representation movableRepresentation = getRepresentationWithInConnectors(fieldOwnerObjectOfWrapper);
                    Field field = fieldReference.getField();
                    StringBuilder methodName = new StringBuilder(field.getName());
                    String initialCharUpperCase = String.valueOf(methodName.charAt(0)).toUpperCase();
                    methodName.setCharAt(0, initialCharUpperCase.charAt(0));
                   return getFirstReferenceTo(movableRepresentation)
                           + Utils.getGetterCall(fieldReference.getField());

               }
           }
       }
       return "[unkown_ref]";
   }

   /*public String getFirstReferenceTo(Wrapper wrapper) {
        return getFirstReferenceTo(representationWithInConnectorsByOwner.get(wrapper));
       RepresentationWithInConnectors representation = representationWithInConnectorsByOwner.get(wrapper.getId());
       return representation == null ? Constants.UNKOWN_REF : getFirstReferenceTo(representation);
   }*/

    public RepresentationWithInConnectors getRepresentationWithInConnectors(Object value) {
        System.out.println("Fetching representation for value: " + value);
        RepresentationWithInConnectors representation = representationWithInConnectorsByOwner.get(value);
        if (representation == null) {
            System.out.println("Representation not found for value: " + value);
        }
        return representation;
    }


    public void add(Object owner, RepresentationWithInConnectors representationWithInConnectors) {
        Point storedPosition = representationPositions.get(representationWithInConnectors);
        if (storedPosition != null) {
            representationWithInConnectors.setPosition(storedPosition);
        }
        representationWithInConnectorsByOwner.put(owner, representationWithInConnectors);
        System.out.println("Added: Key (identityHashCode: " + System.identityHashCode(owner) + ") -> Value: " + representationWithInConnectors);
        refresh();
    }



    public void refresh() {
        doSpatialDistribution();
        repaint();
    }
    @Override
    public void mousePressed(MouseEvent e) {
        for (RepresentationWithInConnectors rep : representationWithInConnectorsByOwner.values()) {
            if (rep.getContainer().contains(e.getPoint())) {
                draggedRepresentation = rep;
                Point repPosition = rep.getPosition();
                dragOffset = new Point(e.getX() - repPosition.x, e.getY() - repPosition.y);
                break;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggedRepresentation != null) {
            Point newPosition = new Point(e.getX() - dragOffset.x, e.getY() - dragOffset.y);
            draggedRepresentation.setPosition(newPosition);
            representationPositions.put(draggedRepresentation, newPosition); // Update position
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (draggedRepresentation != null) {
            representationPositions.put(draggedRepresentation, draggedRepresentation.getPosition()); // Store final position
        }
        draggedRepresentation = null;
        dragOffset = null;
    }
    public void updateMousePosition(MouseEvent e) {
        mousePosition.setLocation(e.getX(), e.getY());
        repaint();
    }

    public void doSpatialDistribution() {
        ArrayList<Representation> allRepresentations = new ArrayList<>(representationWithInConnectorsByOwner.values());
        boolean separated;
        do {
            separated = true;

            for (Representation representation : allRepresentations) {
                Vetor2D speed = new Vetor2D();
                Point position = representation.getPosition();
                //para não sair do canvas
                forceInsideCanvas(position);
                representation.setPosition(position);
                Point centerPosition = representation.getCenterPosition();
                Vetor2D centerVector = new Vetor2D(centerPosition.x, centerPosition.y);

                for (Representation representation2 : allRepresentations) {
                    if (representation == representation2 ||
                            representation.getContainer().contains(representation2.getContainer()) ||
                            representation2.getContainer().contains(representation.getContainer()) ||
                            !representation.intersects(representation2)) {
                        continue;
                    }

                    Point centerPosition2 = representation2.getCenterPosition();
                    int forcedDiffDelta = centerPosition.getX() == centerPosition2.getX() && centerPosition.getY() == centerPosition2.getY() ? 1 : 0;
                    Vetor2D centerVector2 = new Vetor2D(centerPosition2.x + forcedDiffDelta, centerPosition2.y + forcedDiffDelta);
                    Vetor2D diffVector = new Vetor2D(centerVector.getX() - centerVector2.getX(), centerVector.getY() - centerVector2.getY());
                    double sqDiff = diffVector.lengthSquared();

                    if (sqDiff > 0d) {
                        final double coefficientWeakeningRepulse = 1.0d;
                        double scale = coefficientWeakeningRepulse / sqDiff;
                        diffVector.normalize();
                        diffVector.scale(scale);
                        speed.add(diffVector);
                    }


                }

                if (speed.lengthSquared() > 0d) {
                    separated = false;
                    speed.normalize();
                    speed.scale(30d);

                    position.translate((int) speed.getX(), (int) speed.getY());
                    //para não sair do canvas
                    forceInsideCanvas(position);
                    representation.setPosition(position);
                }
            }
        } while (!separated);
    }

    private void forceInsideCanvas(Point position) {
        //para não sair do canvas
        if (position.x < 0) {
            position.x = 0;
        }
        if (position.y < 0) {
            position.y = 0;
        }
    }

    public boolean isCompactMode() {
        return compactMode;
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}







}
