package com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas;

import com.aegamesi.java_visualizer.aed.Comparacao;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.IteradorIteravelDuplo;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.ColecaoIteravelAssociativa;

import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * @author Actual code:
 * Carlos Urbano<carlos.urbano@ipleiria.pt>
 * Catarina Reis<catarina.reis@ipleiria.pt>
 * Marco Ferreira<marco.ferreira@ipleiria.pt>
 * João Ramos<joao.f.ramos@ipleiria.pt>
 * Original code: José Magno<jose.magno@ipleiria.pt>
 */
public class TabelaHashOrdenada<C, V> implements ColecaoIteravelAssociativa<C, V> {
    private static final long serialVersionUID = 1L;

    protected Base base;
    protected Comparacao<C> criterio;

    protected TabelaHashComIncrementoPorHash<C, No> noPorChave;

    public TabelaHashOrdenada(Comparacao<C> cp, int tamanho) {
        criterio = cp;
        base = new Base();
        noPorChave = new TabelaHashComIncrementoPorHash<>(tamanho);
    }

//    public SortedCircularDoubleLinkedListWithHashTable(Comparator<K> cp, int size, SortedLinearIterableCollection<K> collection) {
//        // todo redo
//        this(cp, size);
//
//        if (cp.equals(collection.getComparatorByOrder())) {
//            for (K chave : collection) {
//                new NodeWithElement(chave, base);
//            }
//
//            numberOfElements = collection.getNumberOfElements();
//        } else {
//            addAll(collection);
//        }
//    }
//
//    public SortedCircularDoubleLinkedListWithHashTable(Comparator<K> cp, int size, IterableCollection<K> collection) {
//        this(cp, size);
//
//        addAll(collection);
//    }

    public void removerTodos() {
        base.seguinte = base.anterior = base;
        noPorChave.removerTodos();
    }

    private No getNoPorOrdem(C chave) {
        No cor = noPorChave.consultar(chave);
        if (cor != null) {
            return cor;
        }

        if (noPorChave.getNumeroElementos() == 0 ||
                base.anterior.compararElemento(chave) < 0) {
            return base;
        }

        cor = base.seguinte;
        while (cor.compararElemento(chave) < 0) {
            cor = cor.seguinte;
        }

        return cor;
    }

    private No getNoPorIndice(int indice) {
        int numeroElementos = noPorChave.getNumeroElementos();
        if (indice < 0 || indice >= numeroElementos) {
            throw new IndexOutOfBoundsException();
        }

        No corrente;
        if (indice < numeroElementos / 2) {
            corrente = base.seguinte;

            while (indice-- > 0) {
                corrente = corrente.seguinte;
            }
        } else {
            corrente = base.anterior;

            while (++indice < numeroElementos) {
                corrente = corrente.anterior;
            }
        }

        return corrente;
    }

//    private void addAll(IterableCollection<K> collection) {
//        for (K element : collection) {
//            add(element);
//        }
//    }

    @Override
    public void inserir(C chave, V valor) {
        if (noPorChave.contem(chave)) {
            throw new IllegalArgumentException("Chave " + chave + " duplicada");
        }
        noPorChave.inserir(chave, new NoComElemento(new Associacao<>(chave, valor), getNoPorOrdem(chave)));
    }

    @Override
    public V remover(C chave) {
        No no = noPorChave.remover(chave);
        if (no == null) {
            return null;
        }
        no.anterior.seguinte = no.seguinte;
        no.seguinte.anterior = no.anterior;

        return no.elemento.getValor();
    }


    public V removerPorIndice(int indice) {
        return remover(getNoPorIndice(indice).elemento.getChave());
    }

    @Override
    public V consultar(C chave) {
        No no = noPorChave.consultar(chave);
        return no != null ? no.elemento.getValor() : null;
    }

    public V consultarPorIndice(int indice) {
        return consultar(getNoPorIndice(indice).elemento.getChave());
    }

    public IteradorIteravelDuplo<Associacao<C, V>> consultar(C chaveInicial, C chaveFinal) {
        return new Iterador(chaveInicial, chaveFinal);
    }

    public IteradorIteravelDuplo<C> consultarChaves(C chaveInicial, C chaveFinal) {
        return new IteradorChaves(chaveInicial, chaveFinal);
    }

    public IteradorIteravelDuplo<V> consultarValores(C chaveInicial, C chaveFinal) {
        return new IteradorValores(chaveInicial, chaveFinal);
    }

    @Override
    public int getNumeroElementos() {
        return noPorChave.getNumeroElementos();
    }

    @Override
    public boolean contem(C chave) {
        return noPorChave.contem(chave);
    }

    public boolean contemOrdem(C chave) {
        return contem(chave);
    }

    public boolean contemReferencia(C chave) {
        return contem(chave);
    }

    public Comparacao<C> getComparador() {
        return criterio;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Tabela de Hash Ordenada por ");
        s.append(criterio.getClass().getSimpleName()).append(" = {\n");
        No aux = base.seguinte;
        while (aux != base) {
            s.append(aux.elemento).append("\n");
            aux = aux.seguinte;
        }
        s.append("}\n");
        return s.toString();
    }

    @Override
    public IteradorIteravelDuplo<Associacao<C, V>> iterador() {
        return new Iterador();
    }

    @Override
    public IteradorIteravelDuplo<C> iteradorChaves() {
        return new IteradorChaves();
    }

    @Override
    public IteradorIteravelDuplo<V> iteradorValores() {
        return new IteradorValores();
    }


    public abstract class No implements Serializable {
        private static final long serialVersionUID = 1L;
        protected Associacao<C, V> elemento;
        protected No anterior;
        protected No seguinte;

        protected abstract int compararElemento(C chave);
    }

    protected class Base extends No {
        protected Base() {
            elemento = null;
            seguinte = anterior = this;
        }

        protected int compararElemento(C chave) {
            return 1;
        }
    }

    protected class NoComElemento extends No {
        protected NoComElemento(Associacao<C, V> elem, No seg) {
            if (elem == null) {
                throw new IllegalArgumentException();
            }
            elemento = elem;
            anterior = seg.anterior;
            seguinte = seg;
            anterior.seguinte = seguinte.anterior = this;
        }

        protected int compararElemento(C chave) {
            return criterio.comparar(elemento.getChave(), chave);
        }
    }

    public class Iterador implements IteradorIteravelDuplo<Associacao<C, V>> {
        protected No anteriorAoPrimeiro;
        protected No anterior;
        protected No corrente;
        protected No seguinteAoUltimo;

        protected Iterador() {
            seguinteAoUltimo = anteriorAoPrimeiro = base;
            reiniciar();
        }

        protected Iterador(C chaveInicial, C chaveFinal) {
            if (criterio.comparar(chaveInicial, chaveFinal) > 0) {
                throw new IllegalArgumentException();
            }
            No no = noPorChave.consultar(chaveInicial);
            anteriorAoPrimeiro = no != null ? no.anterior : getNoPorOrdem(chaveInicial).anterior;
            if (base.anterior.compararElemento(chaveFinal) <= 0) {
                seguinteAoUltimo = base;
            } else {
                no = noPorChave.consultar(chaveFinal);
                if (no != null) {
                    seguinteAoUltimo = no.seguinte;
                } else {
                    seguinteAoUltimo = anteriorAoPrimeiro.seguinte;

                    while (seguinteAoUltimo != base && seguinteAoUltimo.compararElemento(chaveFinal) <= 0) {
                        seguinteAoUltimo = seguinteAoUltimo.seguinte;
                    }
                }
            }
            reiniciar();
        }

        @Override
        public void reiniciar() {
            anterior = seguinteAoUltimo.anterior;
            corrente = anteriorAoPrimeiro;
        }

        @Override
        public Associacao<C, V> corrente() {
            if (corrente == anteriorAoPrimeiro) {
                throw new NoSuchElementException();
            }
            return corrente.elemento;
        }

        @Override
        public boolean podeAvancar() {
            return corrente.seguinte != seguinteAoUltimo;
        }

        @Override
        public Associacao<C, V> avancar() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            anterior = corrente;
            corrente = corrente.seguinte;
            return corrente.elemento;
        }

        @Override
        public boolean podeRecuar() {
            return anterior != anteriorAoPrimeiro;
        }

        @Override
        public Associacao<C, V> recuar() {
            if (!podeRecuar()) {
                throw new NoSuchElementException();
            }

            corrente = anterior;
            anterior = anterior.anterior;
            return corrente.elemento;
        }
    }

    protected abstract class IteradorParcial<C_ou_V> implements IteradorIteravelDuplo<C_ou_V> {

        protected IteradorIteravelDuplo<Associacao<C, V>> iterator;

        public IteradorParcial() {
            iterator = new Iterador();
        }

        public IteradorParcial(C chaveInicial, C chaveFinal) {
            iterator = new Iterador(chaveInicial, chaveFinal);
        }

        @Override
        public void reiniciar() {
            iterator.reiniciar();
        }

        @Override
        public boolean podeAvancar() {
            return iterator.podeAvancar();
        }

        public abstract C_ou_V getChaveOuValor(Associacao<C, V> associacao);

        @Override
        public C_ou_V avancar() {
            return getChaveOuValor(iterator.avancar());
        }

        @Override
        public C_ou_V corrente() {
            return getChaveOuValor(iterator.corrente());
        }

        @Override
        public boolean podeRecuar() {
            return iterator.podeRecuar();
        }

        @Override
        public C_ou_V recuar() {
            return getChaveOuValor(iterator.recuar());
        }
    }

    protected class IteradorChaves extends IteradorParcial<C> {
        public IteradorChaves() {
        }

        public IteradorChaves(C chaveinicial, C chaveFinal) {
            super(chaveinicial, chaveFinal);
        }

        @Override
        public C getChaveOuValor(Associacao<C, V> associacao) {
            return associacao.getChave();
        }
    }

    protected class IteradorValores extends IteradorParcial<V> {
        public IteradorValores() {
        }

        public IteradorValores(C chaveinicial, C chaveFinal) {
            super(chaveinicial, chaveFinal);
        }

        @Override
        public V getChaveOuValor(Associacao<C, V> associacao) {
            return associacao.getValor();
        }
    }

}