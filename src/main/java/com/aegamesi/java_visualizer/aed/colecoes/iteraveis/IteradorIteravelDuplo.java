package com.aegamesi.java_visualizer.aed.colecoes.iteraveis;

/**
 * @author Actual code:
 * Carlos Urbano<carlos.urbano@ipleiria.pt>
 * Catarina Reis<catarina.reis@ipleiria.pt>
 * Marco Ferreira<marco.ferreira@ipleiria.pt>
 * João Ramos<joao.f.ramos@ipleiria.pt>
 * Original code: José Magno<jose.magno@ipleiria.pt>
 */
public interface IteradorIteravelDuplo<T> extends IteradorIteravel<T> {
    /**
     * Verifica se pode recuar para o elemento anterior.
     *
     * @return true caso ainda existam elementos a percorrer;
     * false caso contrário
     */
    boolean podeRecuar();

    /**
     * Devolve o elemento anterior.
     * Caso não exista lança a excepção ELEMENTO_INVALIDO
     *
     * @return o elemento anterior
     */
    T recuar();
}
