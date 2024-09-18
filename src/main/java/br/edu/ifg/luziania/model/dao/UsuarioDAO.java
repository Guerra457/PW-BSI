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
            if (usuario.getTipoUsuario() == null) {
                TipoUsuario tipoPadrao = buscarTipoUsuarioPadrao();
                usuario.setTipoUsuario(tipoPadrao);
            }
            em.persist(usuario);
        } catch (Exception e) {
            LOG.error("Erro ao salvar usuário", e);
            throw e;
        }
    }
    @Transactional
    public void atualizar(Usuario usuario) {
        try {
            if (usuario != null) {
                Usuario usuarioExistente = em.find(Usuario.class, usuario.getIdUsuario());
                if (usuarioExistente != null) {
                    usuarioExistente.setNome(usuario.getNome());
                    usuarioExistente.setEmail(usuario.getEmail());
                    usuarioExistente.setCpf(usuario.getCpf());
                    usuarioExistente.setTipoUsuario(usuario.getTipoUsuario());

                    usuarioExistente = em.merge(usuarioExistente);
                    LOG.info("Usuário atualizado com sucesso: " + usuarioExistente.getNome());
                } else {
                    LOG.error("Usuário para atualização não encontrado");
                }
            } else {
                LOG.error("Usuário para atualização não pode ser nulo");
            }
        } catch (Exception e) {
            LOG.error("Erro ao atualizar usuário", e);
            throw e;
        }
    }


    @Transactional
    public void excluir(int idUsuario) {
        try {
            Usuario usuario = em.find(Usuario.class, idUsuario);
            if (usuario != null) {
                em.remove(usuario);
                LOG.info("Usuário excluído com sucesso: " + usuario.getNome());
            } else {
                LOG.warn("Usuário com ID " + idUsuario + " não encontrado para exclusão");
            }
        }catch (Exception e) {
            LOG.error("Erro ao excluir usuário", e);
            throw e;
        }

    }

    @Transactional
    public Usuario buscarPorCPF(String cpf) {
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.cpf = :cpf", Usuario.class)
                    .setParameter("cpf", cpf)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;  // Caso não encontre nenhum usuário com o CPF
        }
    }

    @Transactional
    public boolean existeCpf(String cpf, int idUsuario) {
        try {
            Long count = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.cpf = :cpf AND u.idUsuario <> :idUsuario", Long.class)
                    .setParameter("cpf", cpf)
                    .setParameter("idUsuario", idUsuario)
                    .getSingleResult();
            return count > 0;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Transactional
    public boolean existeEmail(String email, int idUsuario) {
        // Verifique se o email está em uso, mas exclua o usuário atual da verificação
        Long count = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.email = :email AND u.idUsuario <> :idUsuario", Long.class)
                .setParameter("email", email)
                .setParameter("idUsuario", idUsuario)
                .getSingleResult();
        return count > 0;
    }




    @Transactional
    public Usuario buscarPorId(int idUsuario) {
        return em.find(Usuario.class, idUsuario);
    }

    @Transactional
    // Método para listar todos os usuários
    public List<Usuario> listarTodos() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    @Transactional
    // Método para buscar um usuário por email
    public Usuario buscarPorEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    @Transactional
    public Usuario buscarPorEmailESenha(String email, String senha) {
        Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email and u.senha = :senha", Usuario.class);
        query.setParameter("email", email);
        query.setParameter("senha", senha);
        return (Usuario) query.getSingleResult();
    }

    @Transactional
    public TipoUsuario buscarTipoUsuarioPadrao() {
        String tipoPadrao = "Usuário";  // ou você pode passar isso como parâmetro, se necessário
        return em.createQuery("SELECT t FROM TipoUsuario t WHERE t.nomeTipo = :nomeTipo", TipoUsuario.class)
                .setParameter("nomeTipo", tipoPadrao)
                .getSingleResult();
    }
}
