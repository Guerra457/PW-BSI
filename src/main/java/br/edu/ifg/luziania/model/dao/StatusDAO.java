package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.entity.Status;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class StatusDAO {

    @PersistenceContext
    public EntityManager em;

    @Transactional
    public Status buscarPorNome(String nomeStatus) {
        try {
            return em.createQuery("SELECT s FROM Status s WHERE s.nomeStatus = :nomeStatus", Status.class)
                    .setParameter("nomeStatus", nomeStatus)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
