package br.edu.ifg.luziania.model.bo;

import br.edu.ifg.luziania.model.dao.UsuarioDAO;
import br.edu.ifg.luziania.model.dto.UsuarioDTO;
import br.edu.ifg.luziania.model.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.mindrot.jbcrypt.BCrypt;
@ApplicationScoped
public class UsuarioBO {
    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    private JsonWebToken jsonWebToken;

    public Response autenticarUsuario(String email, String senha) {
        // Buscar o usuário no banco de dados
        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario == null || !BCrypt.checkpw(senha, usuario.getSenha())) {
            // Autenticação falhou
            throw new NotAuthorizedException("Email ou senha inválidos");
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getNome(),
                usuario.getIdTipoUsuario().getNomeTipo()
        );

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

    public UsuarioDTO obterUsuarioDoToken(String token) {
        try {
            // Parse o token JWT usando o JsonWebToken
            JsonWebToken jwt = jsonWebToken;

            UsuarioDTO usuario = new UsuarioDTO();
            usuario.setEmail(jwt.getName()); // .getName() geralmente retorna o UPN (email) do token
            usuario.setNome(jwt.getClaim(Claims.full_name)); // Obtenha o nome do claim
            usuario.setTipoUsuario(jwt.getClaim(Claims.groups)); // Obtenha o tipo de usuário ou permissões do claim
            return usuario;
        } catch (Exception e) {
            throw new NotAuthorizedException("Token inválido");
        }
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
