package br.edu.ifg.luziania.model.bo;

import br.edu.ifg.luziania.model.dao.TipoUsuarioDAO;
import br.edu.ifg.luziania.model.dao.UsuarioDAO;
import br.edu.ifg.luziania.model.dto.AutenticacaoDTO;
import br.edu.ifg.luziania.model.dto.RespostaStatusDTO;
import br.edu.ifg.luziania.model.dto.UsuarioDTO;
import br.edu.ifg.luziania.model.entity.TipoUsuario;
import br.edu.ifg.luziania.model.entity.Usuario;
import br.edu.ifg.luziania.model.util.Sessao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.mindrot.jbcrypt.BCrypt;

import static java.util.Objects.nonNull;
@ApplicationScoped
public class UsuarioBO {
    @Inject
    private Sessao sessao;
    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    private TipoUsuarioDAO tipoUsuarioDAO;

    @Inject
    private JsonWebToken jsonWebToken;

    public Response autenticarUsuario(AutenticacaoDTO autenticacaoDTO) {
        RespostaStatusDTO respostaStatusDTO = new RespostaStatusDTO();

        if (nonNull(autenticacaoDTO)){
            Usuario usuario = usuarioDAO.buscarPorEmail(autenticacaoDTO.getEmail());
            if (usuario != null && BCrypt.checkpw(autenticacaoDTO.getSenha(), usuario.getSenha())) {
                sessao.setUsuario(usuario);
                System.out.println("Sessão criada para o usuário: " + sessao.getUsuario().getNome());

                respostaStatusDTO.setMensagem("Bem vindo " + usuario.getNome() + "!");
                respostaStatusDTO.setStatusResposta(200);

                String path = obterPathDeAcesso(usuario.getTipoUsuario().getNomeTipo());

                respostaStatusDTO.setUrl(path);
                return Response.ok(respostaStatusDTO).build();
            } else {
                respostaStatusDTO.setMensagem("Usuário não encontrado!");
                respostaStatusDTO.setUrl("/acesso-negado");
                return Response.status(Response.Status.NOT_FOUND).entity(respostaStatusDTO).build();
            }
        } else {
            respostaStatusDTO.setMensagem("Dados obrigatórios não presentes!");
            respostaStatusDTO.setUrl("/acesso-negado");
            return Response.status(Response.Status.BAD_REQUEST).entity(respostaStatusDTO).build();
        }
    }

    public Response deslogar() {
        RespostaStatusDTO respostaStatusDTO = new RespostaStatusDTO();
        sessao.clearSession();
        System.out.println("Sessão após logout: " + sessao.getUsuario());
        if (!nonNull(sessao.getUsuario())) {
            respostaStatusDTO.setMensagem("Deslogado com sucesso");
            respostaStatusDTO.setStatusResposta(200);
            respostaStatusDTO.setUrl("/");
            return Response.ok(respostaStatusDTO).build();
        } else {
            respostaStatusDTO.setMensagem("Opss! Houve um erro no servidor, não foi possível deslogar, tente novamente mais tarde.");
            respostaStatusDTO.setStatusResposta(400);
            return Response.status(Response.Status.BAD_REQUEST).entity(respostaStatusDTO).build();
        }
    }

    public UsuarioDTO obterUsuarioLogado() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        if (sessao.getUsuario() != null) {
            usuarioDTO.setNome(sessao.getUsuario().getNome());
            usuarioDTO.setEmail(sessao.getUsuario().getEmail());
            usuarioDTO.setTipoUsuario(sessao.getUsuario().getTipoUsuario().getNomeTipo());
        } else {
            throw new NotAuthorizedException("Usuário não está logado");
        }
        return usuarioDTO;
    }

    public void salvarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null || usuarioDTO.getEmail() == null || usuarioDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Usuário ou email não pode ser nulo ou vazio");
        }
        // Hash da senha
        String senhaHash = BCrypt.hashpw(usuarioDTO.getSenha(), BCrypt.gensalt());

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setCpf(usuarioDTO.getCpf());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(senhaHash);

        TipoUsuario tipoUsuario = tipoUsuarioDAO.buscarPorNome(usuarioDTO.getTipoUsuario());
        usuario.setTipoUsuario(tipoUsuario);

        usuarioDAO.salvar(usuario);
    }

    private String obterPathDeAcesso(String tipoUsuario) {
        switch (tipoUsuario) {
            case "Administrador":
                return "/adminPanel";
            case "Atendente":
                return "/chamados";
            case "Usuário":
                return "/home";
            default:
                return "/acesso-negado";
        }
    }
}
