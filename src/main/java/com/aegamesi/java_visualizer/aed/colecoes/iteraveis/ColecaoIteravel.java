package com.aegamesi.java_visualizer.aed.colecoes.iteraveis;

import com.aegamesi.java_visualizer.aed.colecoes.Colecao;

import java.util.Iterator;

/**
 * @author Actual code:
 * Carlos Urbano<carlos.urbano@ipleiria.pt>
 * Catarina Reis<catarina.reis@ipleiria.pt>
 * Marco Ferreira<marco.ferreira@ipleiria.pt>
 * João Ramos<joao.f.ramos@ipleiria.pt>
 * Original code: José Magno<jose.magno@ipleiria.pt>
 */
public interface ColecaoIteravel<T> extends Colecao<T>, Iterable<T> {
    IteradorIteravel<T> iterador();

    @Override
    default Iterator<T> iterator() {
        return iterador();
    }

    int getNumeroElementos();

    default boolean isVazia() {
        return getNumeroElementos() == 0;
    }
}
