package br.edu.ifg.luziania.model.dto;

public class UsuarioDTO {
    private int idUsuario;
    private String cpf;
    private String email;
    private String senha;
    private String nome;
    private String tipoUsuario; // Nome do tipo de usuário, como 'Administrador', 'Atendente', etc.

    // Construtores
    public UsuarioDTO() {}

    public UsuarioDTO(int idUsuario, String cpf, String email, String senha, String nome, String tipoUsuario) {
        this.idUsuario = idUsuario;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters e Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
