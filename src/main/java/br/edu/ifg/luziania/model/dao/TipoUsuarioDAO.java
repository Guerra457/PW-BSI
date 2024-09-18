package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.entity.TipoUsuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class TipoUsuarioDAO {

    @PersistenceContext
    public EntityManager em;

    public TipoUsuario buscarPorNome(String nomeTipo) {
        try {
            return em.createQuery("SELECT t FROM TipoUsuario t WHERE t.nomeTipo = :nomeTipo", TipoUsuario.class)
                    .setParameter("nomeTipo", nomeTipo)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Tratar caso em que não há resultado
            return null;
        }
    }
}
