package com.aegamesi.java_visualizer.aed.colecoes.iteraveis.associativas.estruturas;

/**
 * @author Actual code:
 * Carlos Urbano<carlos.urbano@ipleiria.pt>
 * Catarina Reis<catarina.reis@ipleiria.pt>
 * Marco Ferreira<marco.ferreira@ipleiria.pt>
 * João Ramos<joao.f.ramos@ipleiria.pt>
 * Original code: José Magno<jose.magno@ipleiria.pt>
 */
@Deprecated
public class TabelaHashComIncrementoLinear<C, V> extends TabelaHash<C, V> {

    private static final long serialVersionUID = 1L;

    public TabelaHashComIncrementoLinear(int tamanho) {
        super(tamanho);
    }

    @Override
    protected void iniciarIncremento(C chave) {
        incremento = 1;
    }

    @Override
    protected void calcularProximoIncremento() {
        // Empty
    }

}
