package br.com.certifai.enums;

public enum Categorias {
    MULTIPLA_ESCOLHA("MÚLTIPLA ESCOLHA"),
    DISSERTATIVA("DISSERTATIVA"),
    VERDADEIRO_FALSO("VERDADEIRO OU FALSO");

    private final String descricao;

    Categorias(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
