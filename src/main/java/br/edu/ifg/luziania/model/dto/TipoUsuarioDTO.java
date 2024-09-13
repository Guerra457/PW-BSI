package br.edu.ifg.luziania.model.dto;

public class TipoUsuarioDTO {
    private int idTipoUsuario;
    private String nomeTipo;

    public TipoUsuarioDTO() {

    }

    public TipoUsuarioDTO(int idTipoUsuario, String nomeTipo) {
        this.idTipoUsuario = idTipoUsuario;
        this.nomeTipo = nomeTipo;
    }

    public int getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(int idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public String getNomeTipo() {
        return nomeTipo;
    }

    public void setNomeTipo(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }
}
