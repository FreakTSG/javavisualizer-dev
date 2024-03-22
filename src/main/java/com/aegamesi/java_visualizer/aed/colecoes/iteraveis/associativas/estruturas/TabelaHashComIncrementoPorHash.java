package com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas;

/**
 * @author Actual code:
 * Carlos Urbano<carlos.urbano@ipleiria.pt>
 * Catarina Reis<catarina.reis@ipleiria.pt>
 * Marco Ferreira<marco.ferreira@ipleiria.pt>
 * João Ramos<joao.f.ramos@ipleiria.pt>
 * Original code: José Magno<jose.magno@ipleiria.pt>
 */
public class TabelaHashComIncrementoPorHash<C, V> extends TabelaHash<C, V> {

    protected static final long serialVersionUID = 1L;
    private int primo, tamanhoTabelaAnterior;

    public TabelaHashComIncrementoPorHash(int tamanho) {
        super(tamanho);
        calcularPrimoAnterior();
    }

    private void calcularPrimoAnterior() {
        int n = tabela.length;
        do {
            primo = TabelaHash.proximoPrimo(n /= 2);
        } while (primo >= tabela.length);
        tamanhoTabelaAnterior = tabela.length;
    }

    @Override
    protected void iniciarIncremento(C chave) {
        if (tamanhoTabelaAnterior != tabela.length) {
            calcularPrimoAnterior();
        }
        incremento = primo - (Math.abs(chave.hashCode()) % primo);
    }

    @Override
    protected void calcularProximoIncremento() {
        // Empty
    }
}
