package com.aegamesi.java_visualizer.aed;

/**
 * @author Actual code:
 * Carlos Urbano<carlos.urbano@ipleiria.pt>
 * Catarina Reis<catarina.reis@ipleiria.pt>
 * Marco Ferreira<marco.ferreira@ipleiria.pt>
 * João Ramos<joao.f.ramos@ipleiria.pt>
 * Original code: José Magno<jose.magno@ipleiria.pt>
 */
public interface Comparacao<T> {
    /**
     * Este método compara dois elementos segundo uma ordem definida por um critério.
     *
     * @param o1 referência para o 1º elemento
     * @param o2 referência para o 2º elemento
     * @return valor negativo se a ordem de o1 for menor do que a ordem de o2
     * valor positivo se a ordem de o1 for maior do que a ordem de o2
     * zero se a ordem de o1 for igual à ordem de o2
     */
    int comparar(T o1, T o2);
}
