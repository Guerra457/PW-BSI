package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.entity.TipoUsuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class TipoUsuarioDAO {

    @PersistenceContext
    public EntityManager em;

    public TipoUsuario buscarPorNome(String nomeTipo) {
        return em.createQuery("SELECT t FROM TipoUsuario t WHERE t.nomeTipo = :nome", TipoUsuario.class)
                .setParameter("nome", nomeTipo)
                .getSingleResult();
    }
}
