package br.edu.ifg.luziania.model.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
public class Sessao implements Serializable {
    private static final long serialVersionUID = 1L;

    private String usuario;
    private List<String> permissoes;

    public Sessao() {
        this.usuario = "";
        this.permissoes = new ArrayList<>();
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public List<String> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<String> permissoes) {
        this.permissoes = permissoes;
    }

    public void clearSession(){
        this.usuario = null;
        this.permissoes.clear();
    }
}
