package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.bo.ChamadoBO;
import br.edu.ifg.luziania.model.dao.ChamadoDAO;
import br.edu.ifg.luziania.model.dao.UsuarioDAO;
import br.edu.ifg.luziania.model.dto.ChamadoDTO;
import br.edu.ifg.luziania.model.entity.Chamado;
import br.edu.ifg.luziania.model.entity.Status;
import br.edu.ifg.luziania.model.entity.Usuario;
import br.edu.ifg.luziania.model.util.Sessao;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

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
    private ChamadoBO chamadoBO;


    @Inject
    private Sessao sessao;

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cadastrarChamado(ChamadoDTO chamadoDTO) {
        try {
            System.out.println("ChamadoDTO recebido " + chamadoDTO.getTitulo());
            System.out.println("ChamadoDTO recebido " + chamadoDTO.getDescricao());

            if (chamadoDTO.getTitulo() == null || chamadoDTO.getDescricao() == null) {
                LOG.error("Título ou Descrição está nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity("Título ou Descrição não podem ser nulos").build();
            }

            Usuario usuarioLogado = sessao.getUsuario();

            System.out.println("Nome do usuário logado: " + usuarioLogado);
            if (usuarioLogado == null) {
                LOG.error("Usuário Logado não encontrado");
                return Response.status(Response.Status.BAD_REQUEST).entity("Usuário logado não encontrado").build();
            }

            Chamado chamado = chamadoBO.cadastrarChamado(chamadoDTO, usuarioLogado);

            LOG.info("Chamado criado com sucesso: " + chamado.getTitulo());

            return Response.status(Response.Status.CREATED).entity(chamado).build();
        } catch (Exception e) {
            LOG.error("Erro ao criar chamado", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao criar chamado").build();
        }
    }

    @GET
    @Path("/lista-chamados")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarChamados(){
        try {
            List<ChamadoDTO> chamados = chamadoBO.listarChamados();
            return Response.ok(chamados).build();
        } catch (Exception e) {
            LOG.error("Erro ao listar chamados", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao listar chamados").build();
        }
    }
}