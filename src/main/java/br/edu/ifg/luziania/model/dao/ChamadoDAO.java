package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.entity.Chamado;
import br.edu.ifg.luziania.model.entity.Status;
import br.edu.ifg.luziania.model.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class ChamadoDAO {
    private static final Logger LOG = Logger.getLogger(ChamadoDAO.class);

    @Inject
    private EntityManager em;

    @Transactional
    public void salvar(Chamado chamado) {
        try {
            if (chamado.getSolicitante() == null || chamado.getStatus() == null) {
                throw new IllegalArgumentException("Chamado deve ter um solicitante e status definidos");
            }
            em.persist(chamado);
            LOG.info("Chamado salvo com sucesso: " + chamado.getTitulo());
        } catch (Exception e) {
            LOG.error("Erro ao salvar chamado", e);
            throw e;
        }
    }

    @Transactional
    public void atualizar(Chamado chamado, Usuario usuario) {
        if (chamado == null) {
            LOG.error("Chamado para atualização não pode ser nulo");
            throw new IllegalArgumentException("Chamado não pode ser nulo");
        }

        try {
            Chamado chamadoExistente = em.find(Chamado.class, chamado.getIdChamado());

            if (chamadoExistente != null) {
                // Atualiza o status e o atendente do chamado existente
                chamadoExistente.setStatus(chamado.getStatus());
                chamadoExistente.setAtendente(usuario);

                em.merge(chamadoExistente);
                LOG.info("Chamado atualizado com sucesso: " + chamadoExistente.getTitulo());
            } else {
                LOG.error("Chamado para atualização não encontrado");
                throw new NoResultException("Chamado não encontrado para ID: " + chamado.getIdChamado());
            }
        } catch (Exception e) {
            LOG.error("Erro ao atualizar chamado", e);
            throw e;
        }
    }


    @Transactional
    public List<Chamado> buscarPorSolicitante(int idUsuario) {
        return em.createQuery("SELECT c FROM Chamado c WHERE c.solicitante.idUsuario = :idUsuario", Chamado.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    @Transactional
    public List<Chamado> buscarPorAtendente(int idUsuario) {
        return em.createQuery("SELECT c FROM Chamado c WHERE c.atendente.idUsuario = :idUsuario", Chamado.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    @Transactional
    public Status buscarStatusPorId(Long id) {
        return em.find(Status.class, id);
    }

    @Transactional
    public Status buscarStatusPadrao() {
        String statusPadrao = "Aberto";  // Ou você pode passar isso como parâmetro, se necessário
        return em.createQuery("SELECT s FROM Status s WHERE s.nomeStatus = :nomeStatus", Status.class)
                .setParameter("nomeStatus", statusPadrao)
                .getSingleResult();
    }

    @Transactional
    public List<Chamado> listarTodos() {
        return em.createQuery("SELECT c FROM Chamado c", Chamado.class).getResultList();
    }

    @Transactional
    public Chamado buscarPorId(int idChamado) {
        LOG.info("Buscando chamado na base de dados com id: " + idChamado);
        return em.find(Chamado.class, idChamado);
    }
}
