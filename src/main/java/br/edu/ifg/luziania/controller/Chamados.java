package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.bo.ChamadoBO;
import br.edu.ifg.luziania.model.dao.ChamadoDAO;
import br.edu.ifg.luziania.model.dao.StatusDAO;
import br.edu.ifg.luziania.model.dao.UsuarioDAO;
import br.edu.ifg.luziania.model.dto.ChamadoDTO;
import br.edu.ifg.luziania.model.dto.ResponseDTO;
import br.edu.ifg.luziania.model.dto.StatusDTO;
import br.edu.ifg.luziania.model.entity.Chamado;
import br.edu.ifg.luziania.model.entity.Status;
import br.edu.ifg.luziania.model.entity.Usuario;
import br.edu.ifg.luziania.model.util.Sessao;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
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
    private ChamadoDAO chamadoDAO;

    @Inject
    private Sessao sessao;

    @Inject
    private StatusDAO statusDAO;

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

    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizarChamado(@PathParam("id") int idChamado, ChamadoDTO chamadoDTO, @Context Sessao sessao) {
        LOG.info("Atualizando chamado com id: " + idChamado);

        Chamado chamado = chamadoDAO.buscarPorId(idChamado);

        if (chamado == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Status status = statusDAO.buscarPorNome(chamadoDTO.getNomeStatus());
        LOG.info("Buscando status com nome:" + chamadoDTO.getNomeStatus());
        if (status == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Status não encontrado")
                    .build();
        }

        chamado.setStatus(status);

        Usuario usuarioAtendente = sessao.getUsuario();
        if (usuarioAtendente != null) {
            chamado.setAtendente(usuarioAtendente);
        } else {
            LOG.error("Usuário atendente não encontrado.");
            return Response.status(Response.Status.BAD_REQUEST).entity("Usuário atendente não encontrado.").build();
        }

        chamadoDAO.atualizar(chamado, usuarioAtendente);

        LOG.info("Chamado atualizado: " + chamado);
        return Response.ok(toDTO(chamado)).build();
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

    @GET
    @Path("/meus-chamados")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarMeusChamados(){
        try {
            List<ChamadoDTO> chamados = chamadoBO.listarChamadosPorUsuario();
            return Response.ok(chamados).build();
        } catch (Exception e) {
            LOG.error("Erro ao listar chamados do usuário", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao listar chamados").build();
        }
    }

    @PATCH
    @Path("/{id}/atualizar-status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizarStatusChamado(@PathParam("id") int idChamado, StatusDTO statusDTO) {
        try {
            Usuario usuarioLogado = sessao.getUsuario();
            if (usuarioLogado == null) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Usuário não está autenticado.").build();
            }

            // Atualiza o status do chamado
            chamadoBO.atualizarStatus(idChamado, statusDTO.getNomeStatus(), usuarioLogado);
            return Response.ok(new ResponseDTO("Status atualizado com sucesso")).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Chamado não encontrado.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao atualizar o status do chamado.").build();
        }
    }

    private ChamadoDTO toDTO(Chamado chamado) {
        ChamadoDTO dto = new ChamadoDTO();
        dto.setIdChamado(chamado.getIdChamado());
        dto.setTitulo(chamado.getTitulo());
        dto.setDescricao(chamado.getDescricao());

        if (chamado.getAtendente() != null) {
            dto.setNomeAtendente(chamado.getAtendente().getNome());
            dto.setIdAtendente(chamado.getAtendente().getIdUsuario());
        } else {
            dto.setNomeAtendente("Não atribuído");
        }

        dto.setNomeSolicitante(chamado.getSolicitante().getNome());
        dto.setIdSolicitante(chamado.getSolicitante().getIdUsuario());
        dto.setNomeStatus(chamado.getStatus().getNomeStatus());

        return dto;
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarChamadosPorId(@PathParam("id") int id) {
        LOG.info("Buscando chamado com Id: " + id);

        Chamado chamado = chamadoDAO.buscarPorId(id);
        if (chamado != null) {
            return Response.ok(toDTO(chamado)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}