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

    private static final Logger LOG = Logger.getLogger(UsuarioController.class);

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
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance cadastrarUsuario(@FormParam("nome") String nome,
                                     @FormParam("cpf") String cpf,
                                     @FormParam("email") String email,
                                     @FormParam("senha") String senha) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setCpf(cpf);
            usuario.setEmail(email);
            usuario.setSenha(BCrypt.hashpw(senha, BCrypt.gensalt()));
            // Converter DTO para entidade
            // Idealmente criptografar a senha aqui

            TipoUsuario tipoUsuario = usuarioDAO.buscarTipoUsuarioPadrao(); // Define o tipo padrão
            usuario.setIdTipoUsuario(tipoUsuario);

            usuarioDAO.salvar(usuario); // Salva o usuário no banco de dados

            return cadastro.data("success", true);
        } catch (Exception e) {
            LOG.error("Erro ao cadastrar usuário", e);
            //e.printStackTrace();
            return cadastro.data("success", false).data("error", e.getMessage());
        }
    }
}