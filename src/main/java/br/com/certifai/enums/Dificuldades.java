package br.com.certifai.enums;

public enum Dificuldades {
    BASICO("BÁSICO"),
    INTERMEDIARIO("INTERMEDIÁRIO"),
    AVANCADO("AVANÇADO");

    private final String descricao;

    Dificuldades(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
