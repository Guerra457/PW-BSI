package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.entity.Chamado;
import br.edu.ifg.luziania.model.entity.Status;
import br.edu.ifg.luziania.model.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
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
    public Usuario buscarUsuarioPorId(Long id) {
        return em.find(Usuario.class, id);
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
}
