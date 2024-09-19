package br.edu.ifg.luziania.model.bo;

import br.edu.ifg.luziania.model.dao.ChamadoDAO;
import br.edu.ifg.luziania.model.dao.UsuarioDAO;
import br.edu.ifg.luziania.model.dto.ChamadoDTO;
import br.edu.ifg.luziania.model.entity.Chamado;
import br.edu.ifg.luziania.model.entity.Status;
import br.edu.ifg.luziania.model.entity.Usuario;
import br.edu.ifg.luziania.model.util.Sessao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ChamadoBO {

    private static final Logger LOG = Logger.getLogger(ChamadoBO.class);
    @Inject
    private ChamadoDAO chamadoDAO;

    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    private Sessao sessao;

    public Chamado cadastrarChamado(ChamadoDTO chamadoDTO, Usuario usuarioLogado) throws Exception {

        System.out.println("Título recebido: " + chamadoDTO.getTitulo());
        System.out.println("Descrição recebida: " + chamadoDTO.getDescricao());
        System.out.println("Usuário logado: " + (usuarioLogado != null ? usuarioLogado.getNome() : "Nenhum usuário"));

        Chamado chamado = new Chamado();
        chamado.setTitulo(chamadoDTO.getTitulo());
        chamado.setDescricao(chamadoDTO.getDescricao());

        if (usuarioLogado == null) {
            throw new Exception("Usuário logado não encontrado");
        }

        Status statusPadrao = chamadoDAO.buscarStatusPadrao();
        if (statusPadrao == null) {
            throw new Exception("Status padrão não encontrado");
        }

        chamado.setSolicitante(usuarioLogado);
        chamado.setStatus(statusPadrao);

        System.out.println("Chamado antes de salvar: " + chamado);

        chamadoDAO.salvar(chamado);

        System.out.println("Chamado após salvar:" + chamado);

        return chamado;
    }

    public List<ChamadoDTO> listarChamados() {
        LOG.info("Chamando o método listarChamados");

        List<Chamado> chamados = chamadoDAO.listarTodos();
        return chamados.stream().map(this::toDTO).toList();
    }

    public List<ChamadoDTO> listarChamadosPorUsuario() {
        LOG.info("Chamando o método ListarChamadosPorUsuario");

        Usuario usuarioLogado = sessao.getUsuario();
        if (usuarioLogado == null) {
            throw new IllegalStateException("Usuário não está logado");
        }

        int idUsuario = usuarioLogado.getIdUsuario();
        List<Chamado> chamadosSolicitante = chamadoDAO.buscarPorSolicitante(idUsuario);


        List<Chamado> todosChamados = new ArrayList<>();
        todosChamados.addAll(chamadosSolicitante);

        List<ChamadoDTO> chamadosDTO = todosChamados.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return chamadosDTO;
    }

    public void atualizarStatus(int idChamado, String novoStatus, Usuario usuario) {
        Chamado chamado = chamadoDAO.buscarPorId(idChamado);

        if (chamado == null) {
            throw new NotFoundException("Chamado não encontrado.");
        }

        Status status = chamadoDAO.buscarStatusPorNome(novoStatus);
        if (status == null) {
            throw new IllegalArgumentException("Status inválido "+ novoStatus);
        }

        // Atualiza o status do chamado
        chamado.setStatus(status);
        chamadoDAO.atualizar(chamado, usuario);
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
}
