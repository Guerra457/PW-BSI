package br.edu.ifg.luziania.model.dto;

public class ChamadoDTO {
    private int idChamado;
    private int idAtendente;
    private int idSolicitante;
    private int idStatus;
    private String titulo;
    private String descricao;
    private String comentario;

    public ChamadoDTO() {

    }
    public ChamadoDTO(int idChamado,
                      int idAtendente,
                      int idSolicitante,
                      int idStatus,
                      String titulo,
                      String descricao,
                      String comentario) {
        this.idChamado = idChamado;
        this.idAtendente = idAtendente;
        this.idSolicitante = idSolicitante;
        this.idStatus = idStatus;
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
