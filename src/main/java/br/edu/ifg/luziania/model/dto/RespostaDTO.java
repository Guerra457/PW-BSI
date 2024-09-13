package br.edu.ifg.luziania.model.dto;

import java.time.LocalDateTime;

public class RespostaDTO {

    private Long idResposta;
    private Long idChamado;
    private Long idUsuario;  // Autor da resposta (atendente ou solicitante)
    private String conteudo; // Conteúdo da resposta
    private LocalDateTime dataResposta;

    // Construtor vazio
    public RespostaDTO() {
    }

    // Construtor com parâmetros
    public RespostaDTO(Long idResposta, Long idChamado, Long idUsuario, String conteudo, LocalDateTime dataResposta) {
        this.idResposta = idResposta;
        this.idChamado = idChamado;
        this.idUsuario = idUsuario;
        this.conteudo = conteudo;
        this.dataResposta = dataResposta;
    }

    // Getters e Setters
    public Long getIdResposta() {
        return idResposta;
    }

    public void setIdResposta(Long idResposta) {
        this.idResposta = idResposta;
    }

    public Long getIdChamado() {
        return idChamado;
    }

    public void setIdChamado(Long idChamado) {
        this.idChamado = idChamado;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDateTime getDataResposta() {
        return dataResposta;
    }

    public void setDataResposta(LocalDateTime dataResposta) {
        this.dataResposta = dataResposta;
    }
}
