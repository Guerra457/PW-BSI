package br.edu.ifg.luziania.model.dto;


import java.time.LocalDateTime;

public class StatusHistoricoDTO {
    private int idStatusHistorico;
    private int idChamado;
    private int idStatus;
    private int idUsuario;
    private LocalDateTime dataMudanca;

    public StatusHistoricoDTO() {

    }
    public StatusHistoricoDTO(int idStatusHistorico,
                              int idChamado,
                              int idStatus,
                              int idUsuario,
                              LocalDateTime dataMudanca) {
        this.idStatusHistorico = idStatusHistorico;
        this.idChamado = idChamado;
        this.idStatus = idStatus;
        this.idUsuario = idUsuario;
        this.dataMudanca = dataMudanca;
    }

    public int getIdStatusHistorico() {
        return idStatusHistorico;
    }

    public void setIdStatusHistorico(int idStatusHistorico) {
        this.idStatusHistorico = idStatusHistorico;
    }

    public int getIdChamado() {
        return idChamado;
    }

    public void setIdChamado(int idChamado) {
        this.idChamado = idChamado;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getDataMudanca() {
        return dataMudanca;
    }

    public void setDataMudanca(LocalDateTime dataMudanca) {
        this.dataMudanca = dataMudanca;
    }
}
