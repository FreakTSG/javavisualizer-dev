package com.aegamesi.java_visualizer;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.MyHashTable;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.TabelaHash;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaDuplaNaoOrdenada;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas.ListaSimplesNaoOrdenada;
import com.aegamesi.java_visualizer.ui.ClassCreation;
import com.aegamesi.java_visualizer.ui.IDSToolWindow;
import com.aegamesi.java_visualizer.ui.MyCanvas;
import com.aegamesi.java_visualizer.ui.graphics.GraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.RectangularGraphicElement;
import com.aegamesi.java_visualizer.ui.graphics.representations.BaseRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.PrimitiveOrEnumRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.TextualRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.hashtables.HashTableRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.UnsortedCircularDoubleLinkedListWithBaseRepresentation;
import com.aegamesi.java_visualizer.ui.graphics.representations.linked_lists.UnsortedCircularSimpleLinkedListWithBaseRepresentation;

import javax.swing.*;
import java.awt.*;

import static com.aegamesi.java_visualizer.ui.MyCanvas.IDSToolWindow;


public class MainTestes {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Testes com representações");
        jFrame.setLayout(new BorderLayout());
        IDSToolWindow idsToolWindow = new IDSToolWindow();
        jFrame.add(idsToolWindow.getComponent(), BorderLayout.CENTER);
        MyCanvas canvas = IDSToolWindow.myCanvas;


        //objectos que temos em memória quando o debug está pausado
        ListaSimplesNaoOrdenada<Integer> lista = new ListaSimplesNaoOrdenada<>();

        lista.inserir(5);
        lista.inserir(-3);
        var iterator = lista.iterador();
        iterator.avancar();
        int currentIndex = ((ListaSimplesNaoOrdenada<Integer>.Iterador)iterator).getCurrentIndex();

        // Lista Dupla
        ListaDuplaNaoOrdenada<Integer> listaDupla = new ListaDuplaNaoOrdenada<>();
        listaDupla.inserir(6);
        listaDupla.inserir(4);
        listaDupla.inserir(2);
        listaDupla.inserir(3);





        //representação dos elementos da lista
        for (int i = 0; i < lista.getNumeroElementos(); i++) {
            Integer owner = lista.consultarPorIndice(i);
            canvas.add(owner, new PrimitiveOrEnumRepresentation(new Point(100 * (i + 1), 200), owner, canvas));
        }
        // Represent the elements of listaDupla
        for (int i = 0; i < listaDupla.getNumeroElementos(); i++) {
            Integer owner = listaDupla.consultarPorIndice(i);
            // Adjust position as needed for the double list representation
            canvas.add(owner, new PrimitiveOrEnumRepresentation(new Point(100 * (i + 1), 300), owner, canvas));
        }

        UnsortedCircularSimpleLinkedListWithBaseRepresentation listRep =
                new UnsortedCircularSimpleLinkedListWithBaseRepresentation(new Point(30, 30), lista, canvas);
        // Here you pass the current position of the iterator to the representation.
        listRep.updateIteratorPosition(currentIndex);
        canvas.add(lista, listRep);


        // Representation of listaDupla
        UnsortedCircularDoubleLinkedListWithBaseRepresentation doubleListRep =
                new UnsortedCircularDoubleLinkedListWithBaseRepresentation(new Point(30, 400), listaDupla, canvas); // Adjust the position as needed

        canvas.add(listaDupla, doubleListRep);

        // Hash Table

        TabelaHash<Integer, Integer> hashTable = new MyHashTable(10);

        HashTableRepresentation hashTableRep = new HashTableRepresentation(new Point(30, 500), hashTable, canvas);

        canvas.add(hashTable, hashTableRep);



// Update to reflect changes, if necessary
        doubleListRep.update();



        jFrame.setMinimumSize(new Dimension(1000, 600));
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}