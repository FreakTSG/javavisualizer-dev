package com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ordenadas.estruturas;

import com.aegamesi.java_visualizer.aed.Comparacao;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.ColecaoIteravel;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.ordenadas.ColecaoIteravelLinearOrdenada;

public class ListaDuplaOrdenadaOrdemDistinta<T>
        extends ListaDuplaOrdenada<T> {
    private static final long serialVersionUID = 1L;

    public ListaDuplaOrdenadaOrdemDistinta(Comparacao<T> cp) {
        super(cp);
    }

    public ListaDuplaOrdenadaOrdemDistinta(Comparacao<T> cp, ColecaoIteravelLinearOrdenada<T> colecao) {
        super(cp, colecao);
    }

    public ListaDuplaOrdenadaOrdemDistinta(Comparacao<T> cp, ColecaoIteravel<T> colecao) {
        super(cp, colecao);
    }

    @Override
    public void inserir(T elem) {
        No no = getNoPorOrdem(elem);
        if (no.compararElemento(elem) == 0) {
            throw new IllegalArgumentException("Elemento duplicado!");
        }

        new NoComElemento(elem, no);
        numeroElementos++;
    }

    public T consultarDistinto(T elem) {
        No no = getNoPorOrdem(elem);
        return no.compararElemento(elem) == 0 ? no.elemento : null;
    }
}
