package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.dao.ChamadoDAO;
import br.edu.ifg.luziania.model.dao.UsuarioDAO;
import br.edu.ifg.luziania.model.dto.ChamadoDTO;
import br.edu.ifg.luziania.model.entity.Chamado;
import br.edu.ifg.luziania.model.entity.Status;
import br.edu.ifg.luziania.model.entity.Usuario;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import static java.util.Objects.requireNonNull;

@Path("/chamados")
public class Chamados {
    private static final Logger LOG = Logger.getLogger(Chamados.class);

    private final Template chamados;

    public Chamados(Template chamados) {
        this.chamados = requireNonNull(chamados, "page is required");
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(@QueryParam("name") String name) {
        return chamados.data("name", name);
    }

    @Inject
    private ChamadoDAO chamadoDAO;

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cadastrarChamado(ChamadoDTO chamadoDTO) {
        try {
            Chamado chamado = new Chamado();
            chamado.setTitulo(chamadoDTO.getTitulo());
            chamado.setDescricao(chamadoDTO.getDescricao());

            Usuario usuarioLogado = obterUsuarioLogado();
            if (usuarioLogado == null) {
                LOG.error("Usuário logado não encontrado");
                return Response.status(Response.Status.BAD_REQUEST).entity("Usuário logado não encontrado").build();
            }

            Status statusPadrao = chamadoDAO.buscarStatusPadrao();
            if (statusPadrao == null) {
                LOG.error("Status padrão não encontrado");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Status padrão não encontrado").build();
            }

            chamado.setSolicitante(usuarioLogado);
            chamado.setStatus(statusPadrao);

            chamadoDAO.salvar(chamado);

            LOG.info("Chamado criado com sucesso: " + chamado.getTitulo());

            return Response.status(Response.Status.CREATED).entity(chamado).build();
        } catch (Exception e) {
            LOG.error("Erro ao criar chamado", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao criar chamado").build();
        }
    }

    private Usuario obterUsuarioLogado() {
        return new Usuario();
    }
}