package com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas;

import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.ColecaoIteravel;
import com.aegamesi.java_visualizer.aed.colecoes.iteraveis.IteradorIteravel;
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
public abstract class TabelaHash<C, V> implements ColecaoIteravelAssociativa<C, V> {

    private static final long serialVersionUID = 1L;

    protected Entrada<C, V>[] tabela;  // Parametrização devido a vetor desse tipo
    protected int incremento;
    private int numeroElementos;
    protected int numeroElementosInativos;

    public TabelaHash(int tamanho) {
        tabela = new Entrada[proximoPrimo(tamanho)];
        incremento = 0;
        numeroElementosInativos = numeroElementos = 0;
    }

    public static int proximoPrimo(int n) {
        if (n < 2) {
            return 2;
        }
        if (n % 2 == 0) {
            ++n;
        }

        do {
            for (int i = 3; n % i != 0; i += 2) {
                if (i * i > n) {
                    return n;
                }
            }

            n += 2;
        } while (true);
    }

    protected abstract void iniciarIncremento(C chave);

    protected abstract void calcularProximoIncremento();

    private int getProximoIndice(int i, int inicial) {
        int indice = (i + incremento) % tabela.length;
        calcularProximoIncremento();
        if (indice == inicial) {
            throw new RuntimeException(
                    "Sondagem circular de toda a tabela");
        }
        return indice;
    }

    // Devolve o índice do primeiro null  ou  índice da chave  ou  o índice do primeiro inativo
    protected int getIndiceTabela(C chave) {
        int i = Math.abs(chave.hashCode()) % tabela.length, inativo, inicial = i;
        iniciarIncremento(chave);

        do {
            if (tabela[i] == null) {
                return i; // Devolve o índice do primeiro null
            }

            if (tabela[i].getChave().equals(chave)) {
                return i; // Devolve índice da chave
            }

            if (!tabela[i].isAtivo()) {
                inativo = i; // Encontrou o primeiro inativo no índice i
                break;
            }

            i = getProximoIndice(i, inicial);
        } while (true);

        do {
            i = getProximoIndice(i, inicial);

            if (tabela[i] == null) {
                return inativo; // Devolve o índice do primeiro inativo
            }

            if (tabela[i].getChave().equals(chave)) {
                return i; // Devolve o índice da chave
            }
        } while (true);
    }

    @Override
    public void inserir(C chave, V valor) {
        if (valor == null) {
            throw new IllegalArgumentException();
        }
        int i = getIndiceTabela(chave);
        if (tabela[i] != null) {
            if (tabela[i].isAtivo()) { // implica tabela[i].getChave().equals(chave)
                throw new IllegalArgumentException("Chave " + chave + " duplicada");
            }
            numeroElementosInativos--;
        }
        tabela[i] = new Entrada(chave, valor);
        numeroElementos++;
        if (fatorCarga() >= 0.5) {
            reHash();
        }
    }

    private float fatorCarga() {
        return (numeroElementos + numeroElementosInativos) / (float) tabela.length;
    }

    private void reHash() {
        int tam = proximoPrimo(tabela.length * 2);
        Entrada<C, V>[] tabelaAntiga = tabela;
        tabela = new Entrada[tam];
        numeroElementos = numeroElementosInativos = 0;
        for (int i = 0; i < tabelaAntiga.length; i++) {
            if (tabelaAntiga[i] != null && tabelaAntiga[i].isAtivo()) {
                inserir(tabelaAntiga[i].getChave(), tabelaAntiga[i].getValor());
            }
        }
    }

    @Override
    public V remover(C chave) {
        int i = getIndiceTabela(chave);
        if (tabela[i] == null || !tabela[i].isAtivo()) {
            return null;
        }
        tabela[i].desativar();
        numeroElementosInativos++;
        numeroElementos--;
        return tabela[i].getValor();
    }

    public void removerTodos() {
        for (int i = 0; i < tabela.length; i++) {
            tabela[i] = null;
        }
        numeroElementos = numeroElementosInativos = 0;
    }

    @Override
    public V consultar(C chave) {
        int i = getIndiceTabela(chave);
        return tabela[i] != null && tabela[i].isAtivo() ?
                tabela[i].getValor() : null;
    }

    @Override
    public int getNumeroElementos() {
        return this.numeroElementos;
    }

    @Override
    public boolean contem(C chave) {
        return consultar(chave) != null;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Tabela de tamanho ");
        s.append(tabela.length);
        s.append(" = {\n");
        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] != null) {
                s.append("Tabela[" + i + "]==> ");
                s.append(tabela[i]);
                s.append("\n");
            }
        }
        s.append("}\n");
        return s.toString();
    }

    @Override
    public IteradorIteravel<Associacao<C, V>> iterador() {
        return new Iterador();
    }

    @Override
    public IteradorIteravel<C> iteradorChaves() {
        return new IteradorChaves();
    }

    @Override
    public IteradorIteravel<V> iteradorValores() {
        return new IteradorValores();
    }


    public class Entrada<C, V> implements Serializable {

        private static final long serialVersionUID = 1L;

        protected Associacao<C, V> associacao;
        protected boolean ativo;

        protected Entrada(C chave, V valor) {
            associacao = new Associacao<>(chave, valor);
            ativo = true;
        }

        public C getChave() {
            return associacao.getChave();
        }

        public V getValor() {
            return associacao.getValor();
        }

        public void setValor(V valor) {
            associacao.setValor(valor);
        }

        public Associacao<C, V> getAssociacao() {
            return associacao;
        }

        public boolean isAtivo() {
            return ativo;
        }

        public void desativar() {
            ativo = false;
        }

        @Override
        public String toString() {
            String sb = (isAtivo() ? "" : "X ") +
                    associacao;
            return sb;
        }
    }


    public class Iterador implements IteradorIteravel<Associacao<C, V>> {

        protected int corrente;
        protected int seguinte;

        protected Iterador() {
            reiniciar();
        }

        @Override
        public void reiniciar() {
            corrente = tabela.length;
            if (isVazia()) {
                seguinte = corrente;
            } else {
                seguinte = -1;
                seguinte = seguinteAtivo();
            }
        }

        @Override
        public Associacao<C, V> corrente() {
            if (corrente == tabela.length) {
                throw new NoSuchElementException();
            }
            return tabela[corrente].getAssociacao();
        }

        private int seguinteAtivo() {
            if (seguinte == tabela.length) {
                return seguinte;
            }
            while (++seguinte != tabela.length && (tabela[seguinte] == null || !tabela[seguinte].isAtivo())) {
                // Empty
            }
            return seguinte;
        }

        @Override
        public boolean podeAvancar() {
            return seguinte != tabela.length;
        }

        @Override
        public Associacao<C, V> avancar() {
            if (!podeAvancar()) {
                throw new NoSuchElementException();
            }
            corrente = seguinte;
            seguinte = seguinteAtivo();
            return tabela[corrente].getAssociacao();
        }

        @Override
        public ColecaoIteravel<Associacao<C, V>> getList() {
            return null;
        }
    }


    public abstract class IteradorParcial<C_ou_V> implements IteradorIteravel<C_ou_V> {

        protected IteradorIteravel<Associacao<C, V>> iterador;

        protected IteradorParcial() {
            iterador = iterador();
        }

        @Override
        public void reiniciar() {
            iterador.reiniciar();
        }

        @Override
        public boolean podeAvancar() {
            return iterador.podeAvancar();
        }

        protected abstract C_ou_V getChaveOuValor(Associacao<C, V> a);

        @Override
        public C_ou_V avancar() {
            return getChaveOuValor(iterador.avancar());
        }

        @Override
        public C_ou_V corrente() {
            return getChaveOuValor(iterador.corrente());
        }
    }


    public class IteradorChaves extends IteradorParcial<C> {

        @Override
        protected C getChaveOuValor(Associacao<C, V> a) {
            return a.getChave();
        }

        @Override
        public ColecaoIteravel<C> getList() {
            return null;
        }
    }


    public class IteradorValores extends IteradorParcial<V> {

        @Override
        protected V getChaveOuValor(Associacao<C, V> a) {
            return a.getValor();
        }

        @Override
        public ColecaoIteravel<V> getList() {
            return null;
        }
    }


}
