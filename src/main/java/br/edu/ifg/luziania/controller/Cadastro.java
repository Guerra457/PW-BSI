package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.dao.UsuarioDAO;
import br.edu.ifg.luziania.model.dto.UsuarioDTO;
import br.edu.ifg.luziania.model.entity.TipoUsuario;
import br.edu.ifg.luziania.model.entity.Usuario;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;

import static java.util.Objects.requireNonNull;

@Path("/cadastro")
public class Cadastro {

    private static final Logger LOG = Logger.getLogger(Cadastro.class);

    private final Template cadastro;

    public Cadastro(Template cadastro) {
        this.cadastro = requireNonNull(cadastro, "page is required");
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(@QueryParam("name") String name) {
        return cadastro.data("name", name);
    }

    @Inject
    private UsuarioDAO usuarioDAO;

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cadastrarUsuario(UsuarioDTO usuarioDTO) {
        try {
            if (usuarioDTO.getTipoUsuario() == null || usuarioDTO.getTipoUsuario().isEmpty()) {
                TipoUsuario tipoUsuario = usuarioDAO.buscarTipoUsuarioPadrao(); // Define o tipo padrão
                usuarioDTO.setTipoUsuario(tipoUsuario.getNomeTipo());
            }

            TipoUsuario tipoUsuario = usuarioDAO.buscarTipoUsuarioPadrao();

            Usuario usuario = new Usuario();
            usuario.setNome(usuarioDTO.getNome());
            usuario.setCpf(usuarioDTO.getCpf());
            usuario.setEmail(usuarioDTO.getEmail());
            usuario.setSenha(BCrypt.hashpw(usuarioDTO.getSenha(), BCrypt.gensalt()));
            usuario.setTipoUsuario(tipoUsuario);

            usuarioDAO.salvar(usuario); // Salva o usuário no banco de dados

            return Response.ok(Map.of("success", true, "message", "Usuário cadastrado com sucesso!")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("success", false, "error", e.getMessage())).build();
        }
    }
}