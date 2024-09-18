package br.edu.ifg.luziania.model.dto;

import jakarta.json.bind.annotation.JsonbProperty;

public class ChamadoDTO {
    private int idChamado;
    private int idAtendente;
    private int idSolicitante;
    private int idStatus;
    private String nomeSolicitante;
    private String nomeAtendente;
    private String nomeStatus;
    @JsonbProperty("titulo")
    private String titulo;
    @JsonbProperty("descricao")
    private String descricao;
    private String comentario;

    public ChamadoDTO() {

    }
    public ChamadoDTO(int idChamado,
                      int idAtendente,
                      int idSolicitante,
                      int idStatus,
                      String nomeSolicitante,
                      String nomeAtendente,
                      String nomeStatus,
                      String titulo,
                      String descricao,
                      String comentario) {
        this.idChamado = idChamado;
        this.idAtendente = idAtendente;
        this.idSolicitante = idSolicitante;
        this.idStatus = idStatus;
        this.nomeSolicitante = nomeSolicitante;
        this.nomeAtendente = nomeAtendente;
        this.nomeStatus = nomeStatus;
        this.titulo = titulo;
        this.descricao = descricao;
        this.comentario = comentario;
    }

    public int getIdChamado() {
        return idChamado;
    }

    public void setIdChamado(int idChamado) {
        this.idChamado = idChamado;
    }

    public int getIdAtendente() {
        return idAtendente;
    }

    public void setIdAtendente(int idAtendente) {
        this.idAtendente = idAtendente;
    }

    public int getIdSolicitante() {
        return idSolicitante;
    }

    public void setIdSolicitante(int idSolicitante) {
        this.idSolicitante = idSolicitante;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public String getNomeSolicitante() {
        return nomeSolicitante;
    }

    public void setNomeSolicitante(String nomeSolicitante) {
        this.nomeSolicitante = nomeSolicitante;
    }

    public String getNomeAtendente() {
        return nomeAtendente;
    }

    public void setNomeAtendente(String nomeAtendente) {
        this.nomeAtendente = nomeAtendente;
    }

    public String getNomeStatus() {
        return nomeStatus;
    }

    public void setNomeStatus(String nomeStatus) {
        this.nomeStatus = nomeStatus;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
