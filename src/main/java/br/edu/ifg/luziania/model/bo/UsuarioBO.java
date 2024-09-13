package br.edu.ifg.luziania.model.bo;

import br.edu.ifg.luziania.model.dao.UsuarioDAO;
import br.edu.ifg.luziania.model.dto.UsuarioDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claims;
import org.mindrot.jbcrypt.BCrypt;
@ApplicationScoped
public class UsuarioBO {
    @Inject
    private UsuarioDAO usuarioDAO;

    public Response autenticarUsuario(String email, String senha) {
        // Buscar o usuário no banco de dados
        UsuarioDTO usuarioDTO = usuarioDAO.buscarPorEmail(email);

        if (usuarioDTO == null || !BCrypt.checkpw(senha, usuarioDTO.getSenha())) {
            // Autenticação falhou
            throw new NotAuthorizedException("Email ou senha inválidos");
        }

        // Gerar o token JWT se a autenticação for bem-sucedida
        String token = Jwt.issuer("your-issuer")
                .upn(usuarioDTO.getEmail())  // Usar o email do usuário como UPN
                .claim(Claims.full_name, usuarioDTO.getNome())
                .claim(Claims.groups, usuarioDTO.getTipoUsuario())  // Tipo de usuário ou permissões
                .sign();  // Assinar o token

        // Retornar o token na resposta
        return Response.ok()
                .header("Authorization", "Bearer " + token)
                .cookie(new NewCookie("token", token))  // Pode retornar como cookie também
                .build();
    }

    public void salvarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null || usuarioDTO.getEmail() == null || usuarioDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Usuário ou email não pode ser nulo ou vazio");
        }

        // Hash da senha
        String senhaHash = BCrypt.hashpw(usuarioDTO.getSenha(), BCrypt.gensalt());
        // Aqui você pode salvar o usuário no banco de dados usando o DAO
    }
}
