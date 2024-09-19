package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.bo.UsuarioBO;
import br.edu.ifg.luziania.model.dao.TipoUsuarioDAO;
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

    @Inject
    private UsuarioBO usuarioBO;

    @Inject
    private TipoUsuarioDAO tipoUsuarioDAO;

    @GET
    @Path("lista-usuarios")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UsuarioDTO> listarUsuarios() {
        LOG.info("Chamando o método listarUsuarios");

        List<Usuario> usuarios = usuarioDAO.listarTodos();
        // Converter os usuários para DTO se necessário
        return usuarios.stream().map(this::toDTO).toList();
    }


    private UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setCpf(usuario.getCpf());
        dto.setEmail(usuario.getEmail());
        dto.setNome(usuario.getNome());
        dto.setTipoUsuario(usuario.getTipoUsuario().getNomeTipo());

        return dto;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarUsuariosPorId(@PathParam("id") int id) {
        LOG.info("Buscando usuário com Id: " + id);

        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario != null) {
            return Response.ok(toDTO(usuario)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/logado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obterUsuarioLogado() {
        try {
            UsuarioDTO usuarioDTO = usuarioBO.obterUsuarioLogado();
            return Response.ok(usuarioDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizarUsuario(@PathParam("id") int idUsuario, UsuarioDTO usuarioDTO) {
        LOG.info("Atualizando usuário com id: " + idUsuario);


        Usuario usuario = usuarioDAO.buscarPorId(idUsuario);

        System.out.println("Teste de cpf do usuário" + usuario.getCpf());

        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        TipoUsuario tipoUsuario = tipoUsuarioDAO.buscarPorNome(usuarioDTO.getTipoUsuario());
        if (tipoUsuario == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Tipo de usuário não encontrado")
                    .build();
        }

        // Verificar se o CPF foi alterado e é único
        String cpfNovo = usuarioDTO.getCpf().replaceAll("[^0-9]", ""); // Remove caracteres não numéricos
        if (!cpfNovo.equals(usuario.getCpf()) && usuarioDAO.existeCpf(cpfNovo, idUsuario)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("CPF já está em uso por outro usuário")
                    .build();
        }

        // Verificar se o email foi alterado e é único
        String emailNovo = usuarioDTO.getEmail();
        if (!emailNovo.equals(usuario.getEmail()) && usuarioDAO.existeEmail(emailNovo, idUsuario)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Email já está em uso por outro usuário")
                    .build();
        }

        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(emailNovo);
        usuario.setCpf(cpfNovo);
        usuario.setTipoUsuario(tipoUsuario);

        usuarioDAO.atualizar(usuario);

        return Response.ok(toDTO(usuario)).build();
    }



    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletarUsuario(@PathParam("id") int idUsuario) {
        LOG.info("Deletando usuário com id: " + idUsuario);
        usuarioDAO.excluir(idUsuario);
        return Response.noContent().build();
    }
}
