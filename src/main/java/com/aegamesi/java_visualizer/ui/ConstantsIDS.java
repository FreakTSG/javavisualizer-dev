package com.aegamesi.java_visualizer.ui;

import java.awt.*;

public class ConstantsIDS {
    public static final String ABOUT_MESSAGE = "<html><b>Intelligent Data Structures</b><br>" +
            "Tool to support the evaluation and choice of Data Structures. <br>" +
            "<br>" +
            "Version 2.0.8 (build Dez 2023) <br>" +
            "<br>" +
            "Authors: Carlos Urbano, José Magno" +
            "</html>";

    public static final int FONT_SIZE_INDEX = 10;
    public static final int FONT_SIZE_TEXT = 12;

    //    public static final String COMPARATOR_CLASSNAME = "java.util.Comparator";
    public static final String COMPARATOR_CLASSNAME = "aed.Comparacao";
    /**
     * DATA STRUCTURES FIELDS NAME
     */
    public static final String BASE = "base";
    public static final String HEAD_NODE = "noInicial";
    public static final String TAIL_NODE = "noFinal";
    public static final String NUMBER_OF_ELEMENTS = "numeroElementos";
    public static final String TABLE = "tabela";
    public static final String NO_POR_CHAVE = "noPorChave";
    public static final String PREVIOUS = "anterior";
    public static final String ELEMENT = "elemento";
    public static final String NEXT = "seguinte";
    public static final String COMPARATOR_BY_ORDER = "criterio";
    public static final String NUMBER_OF_INACTIVE_ELEMENTS = "numeroElementosInativos";
    public static final String ACTIVE = "ativo";
    public static final String ASSOCIATION = "associacao";
    public static final String KEY = "chave";
    public static final String VALUE = "valor";

    /**
     * ERROR MESSAGES
     */
    public static final String INVALID_VARIABLE_NAME_ERROR = "Identificador de variável inválido!";
    public static final String EMPTY_FIELDS_IN_ERROR = "Nenhum atributo selecionado!";
    public static final String INVALID_ARRAY_SIZE_ERROR = "Tamanho de array inválido!";
    public static final String INVALID_HASH_TABLE_SIZE_ERROR = "O tamanho deve ser superior a 1!";
    public static final String INVALID_RECORD_NAME_ERROR = "Identidicador de classe inválido!";
    public static final String INVALID_RECORD_COMPONENT_NAME_ERROR = "Identidicador de atributo inválido!";
    public static final String DUPLICATE_RECORD_COMPONENT_NAME_ERROR = "Identificador de atributo duplicado!";
    public static final String COMPILATION_ERROR = "Erros a compilar o projeto.\nPode ser apenas problemas temporários.\nVolte a recompilar o projeto.";
    public static final String COMPILATION_ERROR_TITLE = "Erro de compilação";
    public static final String DUPLICATE_VARIABLE_NAME_ERROR = "Identificador de variável duplicado!";
    public static final String DUPLICATE_RECORD_NAME_ERROR = "Identificador de classe duplicado!";
    public static final String INVALID_VALUE_ERROR = "Valor inválido!";

    /**
     * UNDO / REDO TEXT
     */
    public static final String INITIAL_STATE = "Estado Inicial";
    public static final String CLEAR_CANVAS = "Limpar Canvas";
    public static final String NEW = "Novo ";
    public static final String SET = "Atribuir ";
    public static final String REMOVE = "Remover ";
    public static final String FROM = " de ";
    public static final String UNSET = "Desatribuir ";
    public static final String DECLARE = "Declarar ";
    public static final String CREATE = "Criar ";
    public static final String DELETE = "Remover ";
    public static final String GENERATE = "Gerar ";
    public static final String ADD = "Adicionar ";
    public static final String TO = " para ";
    public static final Color LINKED_LIST_ELEMENTS_CONNECTIONS_COLOR = new Color(98, 178, 14);
    public static final Color HASH_TABLE_ELEMENTS_CONNECTIONS_COLOR = new Color(240, 167, 74);

    /**
     * Others
     */
    public static final String UNKOWN_REF = "[unkown_ref]";
    public static final String NON_VALID_MODEL_PACKAGE = "Package inválido! Deve ser uma pasta dentro do projeto.";
    public static final String NON_VALID_MODEL_PACKAGE_TITLE = "Package inválido";
}

