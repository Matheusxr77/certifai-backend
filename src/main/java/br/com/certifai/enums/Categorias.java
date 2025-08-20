package br.com.certifai.enums;

public enum Categorias {
    NUVEM("COMPUTAÇÃO EM NUVEM"),
    DEV("DESENVOLVIMENTO DE SOFTWARE"),
    BANCO("BANCO DE DADOS"),
    INFRA("INFRAESTRUTURA");

    private final String descricao;

    Categorias(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
