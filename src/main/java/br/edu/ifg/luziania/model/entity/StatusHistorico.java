package br.edu.ifg.luziania.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_STATUS_HISTORICO")
public class StatusHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idStatusHistorico;

    // Relacionamento com o Chamado
    @ManyToOne
    @JoinColumn(name = "idChamado", nullable = false)  // mapeia a coluna id_chamado da tabela CHAMADO
    private Chamado chamado;

    // Relacionamento com o Status
    @ManyToOne
    @JoinColumn(name = "idStatus", nullable = false)  // mapeia a coluna id_status da tabela STATUS
    private Status status;

    // Relacionamento com o Atendente (Usuário que mudou o status)
    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)  // mapeia a coluna id_atendente da tabela USERS
    private Usuario atendente;

    @Column(name = "data_mudanca", nullable = false)
    private LocalDateTime dataMudanca;

    // Construtor padrão
    public StatusHistorico() {
        this.dataMudanca = LocalDateTime.now();  // Define o valor padrão para a data de mudança
    }

    public int getIdStatusHistorico() {
        return idStatusHistorico;
    }

    public void setIdStatusHistorico(int idStatusHistorico) {
        this.idStatusHistorico = idStatusHistorico;
    }

    public Chamado getChamado() {
        return chamado;
    }

    public void setChamado(Chamado chamado) {
        this.chamado = chamado;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Usuario getAtendente() {
        return atendente;
    }

    public void setAtendente(Usuario atendente) {
        this.atendente = atendente;
    }

    public LocalDateTime getDataMudanca() {
        return dataMudanca;
    }

    public void setDataMudanca(LocalDateTime dataMudanca) {
        this.dataMudanca = dataMudanca;
    }
}
