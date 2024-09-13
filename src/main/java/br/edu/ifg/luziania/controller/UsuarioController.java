package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.entity.TipoUsuario;
import br.edu.ifg.luziania.model.entity.Usuario;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import br.edu.ifg.luziania.model.dto.UsuarioDTO;
import br.edu.ifg.luziania.model.dao.UsuarioDAO;
import jakarta.inject.Inject;

@Path("/usuarios")
public class UsuarioController {

    private static final Logger LOG = Logger.getLogger(UsuarioController.class);

    @Inject
    private UsuarioDAO usuarioDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UsuarioDTO> listarUsuarios() {
        LOG.info("Chamando o método listarUsuarios");

        List<Usuario> usuarios = usuarioDAO.listarTodos();
        // Converter os usuários para DTO se necessário
        return usuarios.stream().map(this::toDTO).toList();
    }


    private UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setCpf(usuario.getCpf());
        dto.setEmail(usuario.getEmail());
        dto.setNome(usuario.getNome());
        dto.setTipoUsuario(usuario.getIdTipoUsuario().getNomeTipo()); // Supõe que você tenha um relacionamento aqui
        return dto;
    }
}
