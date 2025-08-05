package br.com.certifai.enums;

public enum Status {
    PENDENTE("NÃO INICIADA"),
    ANDAMENTO("EM ANDAMENTO"),
    CONCLUIDA("CONCLUÍDA");

    private final String descricao;

    Status(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
