package br.edu.ifg.luziania.model.dto;

public class PermissaoDTO {

    private Long idPermissao;
    private String nomePermissao; // Ex: "ADMIN", "USER", etc.
    private String descricaoPermissao;

    // Construtor vazio
    public PermissaoDTO() {
    }

    // Construtor com parâmetros
    public PermissaoDTO(Long idPermissao, String nomePermissao, String descricaoPermissao) {
        this.idPermissao = idPermissao;
        this.nomePermissao = nomePermissao;
        this.descricaoPermissao = descricaoPermissao;
    }

    // Getters e Setters
    public Long getIdPermissao() {
        return idPermissao;
    }

    public void setIdPermissao(Long idPermissao) {
        this.idPermissao = idPermissao;
    }

    public String getNomePermissao() {
        return nomePermissao;
    }

    public void setNomePermissao(String nomePermissao) {
        this.nomePermissao = nomePermissao;
    }

    public String getDescricaoPermissao() {
        return descricaoPermissao;
    }

    public void setDescricaoPermissao(String descricaoPermissao) {
        this.descricaoPermissao = descricaoPermissao;
    }
}
