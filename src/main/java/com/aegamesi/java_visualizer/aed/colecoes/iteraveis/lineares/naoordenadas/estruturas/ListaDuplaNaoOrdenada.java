package com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.estruturas;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.ColecaoIteravel;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.IteradorIteravelDuplo;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.lineares.naoordenadas.ColecaoIteravelLinearNaoOrdenada;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author Actual code:
 * Carlos Urbano<carlos.urbano@ipleiria.pt>
 * Catarina Reis<catarina.reis@ipleiria.pt>
 * Marco Ferreira<marco.ferreira@ipleiria.pt>
 * João Ramos<joao.f.ramos@ipleiria.pt>
 * Original code: José Magno<jose.magno@ipleiria.pt>
 */
public class ListaDuplaNaoOrdenada<T> implements ColecaoIteravelLinearNaoOrdenada<T> {
    private static final long serialVersionUID = 1L;

    protected No base;

    protected No noFinal;
    protected int numeroElementos;

    public ListaDuplaNaoOrdenada() {
        noFinal = base = new No();
        numeroElementos = 0;
    }

    public ListaDuplaNaoOrdenada(ColecaoIteravel<T> colecao) {
        this();
        for (T elem : colecao) {
            noFinal= new No(elem, noFinal);
        }
        numeroElementos = colecao.getNumeroElementos();
    }

    protected No getNo(T elem) {
        No cor = base.seguinte;
        while (cor != base && !cor.elemento.equals(elem)) {
            cor = cor.seguinte;
        }
        return cor;
    }

    protected No getNoPorReferencia(T elem) {
        No cor = base.seguinte;
        while (cor != base && cor.elemento != elem) {
            cor = cor.seguinte;
        }

        return cor;
    }

    protected No getNoPorIndice(int indice) {
        if(indice < 0 || indice >= numeroElementos) {
            throw new IndexOutOfBoundsException();
        }

        No cor;
        if (indice < numeroElementos / 2) {
            cor = base.seguinte;

            while (indice-- > 0) {
                cor = cor.seguinte;
            }
        } else {
            cor = base.anterior;

            while (++indice < numeroElementos) {
                cor = cor.anterior;
            }
        }

        return cor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListaDuplaNaoOrdenada<?>)) return false;
        ListaDuplaNaoOrdenada<?> that = (ListaDuplaNaoOrdenada<?>) o;
        if (this.numeroElementos != that.numeroElementos) return false;

        No thisCurrent = this.base.seguinte;
        No thatCurrent = (No) that.base.seguinte;

        while (thisCurrent != base && thatCurrent != that.base) {
            if (!Objects.equals(thisCurrent.elemento, thatCurrent.elemento)) {
                return false;
            }
            thisCurrent = thisCurrent.seguinte;
            thatCurrent = thatCurrent.seguinte;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        No current = this.base.seguinte;
        while (current != base) {
            result = 31 * result + (current.elemento != null ? current.elemento.hashCode() : 0);
            current = current.seguinte;
        }
        return result;
    }

    @Override
    public void inserirNoInicio(T elem) {
        new No(elem, base.seguinte);
        numeroElementos++;
    }

    @Override
    public void inserirNoFim(T elem) {
        new No(elem, base);
        numeroElementos++;
    }

    @Override
    public void inserirPorIndice(int indice, T elem) {
        if (indice == numeroElementos) {
            inserirNoFim(elem);
        } else {
            new No(elem, getNoPorIndice(indice));
            numeroElementos++;
        }
    }

    private T removerNo(No no) {
        no.anterior.seguinte = no.seguinte;
        no.seguinte.anterior = no.anterior;
        numeroElementos--;

        return no.elemento;
    }

    @Override
    public T removerDoInicio() {
        return numeroElementos > 0 ? removerNo(base.seguinte) : null;
    }

    @Override
    public T removerDoFim() {
        return numeroElementos > 0 ? removerNo(base.anterior) : null;
    }

    @Override
    public T remover(T elem) {
        No no = getNo(elem);

        return no != base ? removerNo(no) : null;
    }

    @Override
    public T removerPorReferencia(T elem) {
        No no = getNoPorReferencia(elem);

        return no != base ? removerNo(no) : null;
    }

    @Override
    public T removerPorIndice(int indice) {
        return removerNo(getNoPorIndice(indice));
    }

    @Override
    public T consultarPorIndice(int indice) {
        return getNoPorIndice(indice).elemento;
    }

    @Override
    public boolean contem(T elem) {
        return getNo(elem) != base;
    }

    @Override
    public boolean contemReferencia(T elem) {
        return getNoPorReferencia(elem) != base;
    }

    @Override
    public int getNumeroElementos() {
        return numeroElementos;
    }

    @Override
    public IteradorIteravelDuplo<T> iterador() {
        return new Iterador();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Lista Dupla Não Ordenada = {\n");
        No aux = base.seguinte;
        while (aux != base) {
            s.append(aux.elemento).append("\n");
            aux = aux.seguinte;
        }
        s.append("}\n");
        return s.toString();
    }


    public class No  implements Serializable {
        private static final long serialVersionUID = 1L;

        protected T elemento;
        protected No anterior;
        protected No seguinte;


        // Criação do nó base
        protected No() {
            elemento = null;
            seguinte = anterior = this;
        }

        // Criação de nó com elemento elem e inserção antes do nó seg
        protected No(T elem, No seg) {
            elemento = elem;
            anterior = seg.anterior;
            seguinte = seg;
            anterior.seguinte = seguinte.anterior = this;
        }
    }


    public class Iterador implements IteradorIteravelDuplo<T> {
        protected No corrente;

        protected Iterador() {
            reiniciar();
        }

        @Override
        public void reiniciar() {
            corrente = base;
        }

        @Override
        public T corrente() {
            if (corrente == base) {
                throw new NoSuchElementException();
            }
            return corrente.elemento;
        }

        @Override
        public boolean podeAvancar() {
            return corrente.seguinte != base;
        }

        @Override
        public T avancar() {
            if (!podeAvancar()) {
                throw new NoSuchElementException();
            }
            corrente = corrente.seguinte;
            return corrente.elemento;
        }

        @Override
        public boolean podeRecuar() {
            return corrente.anterior != base;
        }

        @Override
        public T recuar() {
            if (!podeRecuar()) {
                throw new NoSuchElementException();
            }
            corrente = corrente.anterior;
            return corrente.elemento;
        }
    }
}
