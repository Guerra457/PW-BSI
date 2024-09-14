package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.controller.Cadastro;
import br.edu.ifg.luziania.model.dto.UsuarioDTO;
import br.edu.ifg.luziania.model.entity.TipoUsuario;
import br.edu.ifg.luziania.model.entity.Usuario;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class UsuarioDAO {

    private static final Logger LOG = Logger.getLogger(UsuarioDAO.class);
    @Inject
    private EntityManager em;

    @Transactional
    public void salvar(Usuario usuario) {
        try {
            if (usuario.getIdTipoUsuario() == null) {
                TipoUsuario tipoPadrao = buscarTipoUsuarioPadrao();
                usuario.setIdTipoUsuario(tipoPadrao);
            }
            em.persist(usuario);
        } catch (Exception e) {
            LOG.error("Erro ao salvar usuário", e);
            throw e;
        }
    }
    @Transactional
    public void atualizar(Usuario usuario) {
        em.merge(usuario);
    }

    @Transactional
    public void excluir(int idUsuario) {
        try {
            Usuario usuario = em.find(Usuario.class, idUsuario);
            if (usuario != null) {
                em.remove(usuario);
            }
        }catch (Exception e) {
            LOG.error("Erro ao excluir usuário", e);
            throw e;
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
    }

    @Transactional
    // Método para buscar um usuário por email
    public Usuario buscarPorEmail(String email) {
        TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        }catch (NoResultException e) {
            return null;
        }

        /*Usuario usuario = query.getSingleResult();

        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getNome(),
                usuario.getIdTipoUsuario().getNomeTipo()
        );*/
    }

    @Transactional
    public TipoUsuario buscarTipoUsuarioPadrao() {
        String tipoPadrao = "Usuário";  // ou você pode passar isso como parâmetro, se necessário
        return em.createQuery("SELECT t FROM TipoUsuario t WHERE t.nomeTipo = :nomeTipo", TipoUsuario.class)
                .setParameter("nomeTipo", tipoPadrao)
                .getSingleResult();
    }
}
