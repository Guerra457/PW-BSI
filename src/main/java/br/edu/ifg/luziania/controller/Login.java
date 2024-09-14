package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.dao.UsuarioDAO;
import br.edu.ifg.luziania.model.dto.UsuarioDTO;
import br.edu.ifg.luziania.model.entity.Usuario;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@Path("/login")
public class Login {

    private final Template login;

    public Login(Template login) {
        this.login = requireNonNull(login, "page is required");
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(@QueryParam("name") String name) {
        return login.data("name", name);
    }

    @Inject
    private UsuarioDAO usuarioDAO;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response autenticarUsuario(UsuarioDTO usuarioDTO) {
        try {
            Usuario usuario = usuarioDAO.buscarPorEmail(usuarioDTO.getEmail());

            if (usuario == null) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(Map.of("success", false, "message", "Usuário não encontrado")).build();
            }
            if (BCrypt.checkpw(usuarioDTO.getSenha(), usuario.getSenha())) {
                // Verifica o tipo de usuário e retorna o endpoint correspondente
                String redirectUrl;
                switch (usuario.getIdTipoUsuario().getNomeTipo()) {
                    case "Administrador":
                        redirectUrl = "/adminPanel";
                        break;
                    case "Atendente":
                        redirectUrl = "/chamados";
                        break;
                    case "Usuário":
                        redirectUrl = "/home";
                        break;
                    default:
                        return Response.status(Response.Status.UNAUTHORIZED).entity(Map.of("success", false, "message", "Tipo de usuário inválido")).build();
                }

                return Response.ok(Map.of("success", true, "redirectUrl", redirectUrl)).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity(Map.of("success", false, "message", "Senha incorreta")).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("success", false, "message", "Erro no servidor")).build();
        }
    }
}