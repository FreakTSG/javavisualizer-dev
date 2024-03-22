package com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.ColecaoIteravel;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.IteradorIteravel;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas.Associacao;

/**
 * @author Actual code:
 * Carlos Urbano<carlos.urbano@ipleiria.pt>
 * Catarina Reis<catarina.reis@ipleiria.pt>
 * Marco Ferreira<marco.ferreira@ipleiria.pt>
 * João Ramos<joao.f.ramos@ipleiria.pt>
 * Original code: José Magno<jose.magno@ipleiria.pt>
 */
public interface ColecaoIteravelAssociativa<C, V> extends ColecaoIteravel<Associacao<C, V>> {

    /**
     * Insere uma associação (chave, valor) sem duplicação
     */
    void inserir(C chave, V valor);

    /**
     * Remove a associação correspondente à chave e devolve o valor correspondente
     */
    V remover(C chave);

    /**
     * Devolve o valor correspondente à chave ou null
     */
    V consultar(C chave);

    /**
     * Verifica se chave pertence à tabela
     */
    boolean contem(C chave);

    /**
     * Devolve um iterador das chaves da tabela
     */
    IteradorIteravel<C> iteradorChaves();

    /**
     * Devolve um iterador dos valores da tabela
     */
    IteradorIteravel<V> iteradorValores();

}
