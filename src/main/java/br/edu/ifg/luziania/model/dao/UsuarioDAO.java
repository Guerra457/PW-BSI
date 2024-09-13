package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.dto.UsuarioDTO;
import br.edu.ifg.luziania.model.entity.TipoUsuario;
import br.edu.ifg.luziania.model.entity.Usuario;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class UsuarioDAO {
    @Inject
    private EntityManager em;

    @Transactional
    public void salvar(Usuario usuario) {
        /*EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(usuario);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }*/
        em.persist(usuario);
    }
    @Transactional

    public void atualizar(Usuario usuario) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(usuario);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Transactional
    public void excluir(int idUsuario) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Usuario usuario = em.find(Usuario.class, idUsuario);
            if (usuario != null) {
                em.remove(usuario);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Transactional
    public Usuario buscarPorCPF(String cpf) {
        return em.find(Usuario.class, cpf);
    }

    @Transactional
    // Método para listar todos os usuários
    public List<Usuario> listarTodos() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        /*TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);
        return query.getResultList();*/
    }

    @Transactional
    // Método para buscar um usuário por email
    public UsuarioDTO buscarPorEmail(String email) {
        TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
        query.setParameter("email", email);

        Usuario usuario = query.getSingleResult();

        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getNome(),
                usuario.getIdTipoUsuario().getNomeTipo()
        );
    }

    @Transactional
    public TipoUsuario buscarTipoUsuarioPadrao() {
        String tipoPadrao = "Usuário";  // ou você pode passar isso como parâmetro, se necessário
        return em.createQuery("SELECT t FROM TipoUsuario t WHERE t.nomeTipo = :nomeTipo", TipoUsuario.class)
                .setParameter("nomeTipo", tipoPadrao)
                .getSingleResult();
    }
}
