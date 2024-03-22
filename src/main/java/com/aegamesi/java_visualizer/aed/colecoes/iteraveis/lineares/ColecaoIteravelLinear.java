package com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.ColecaoIteravel;

/**
 * @author Actual code:
 * Carlos Urbano<carlos.urbano@ipleiria.pt>
 * Catarina Reis<catarina.reis@ipleiria.pt>
 * Marco Ferreira<marco.ferreira@ipleiria.pt>
 * João Ramos<joao.f.ramos@ipleiria.pt>
 * Original code: José Magno<jose.magno@ipleiria.pt>
 */
public interface ColecaoIteravelLinear<T> extends ColecaoIteravel<T> {
    void inserir(T elem);

    T remover(T elem);

    T removerPorReferencia(T elem);

    T removerPorIndice(int indice);

    T consultarPorIndice(int indice);

    boolean contem(T elem);

    boolean contemReferencia(T elem);
}
