package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.dao.UsuarioDAO;
import br.edu.ifg.luziania.model.dto.UsuarioDTO;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class FiltroAutorizacao implements ContainerRequestFilter {
    @Inject
    private UsuarioDAO usuarioDAO;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        // Verifique se o caminho corresponde às páginas públicas
        if (paginaPublica(path)) {
            // Se for uma página pública, não aplique o filtro de autenticação
            return;
        }

        String token = requestContext.getHeaderString("Authorization");

        if (token == null || !tokenValido(token)) {
            requestContext.abortWith(Response.seeOther(java.net.URI.create("/login")).build());
            return;
        }

        UsuarioDTO usuario = getUsuarioDoToken(token);

        if (!temAcesso(usuario, requestContext.getUriInfo().getPath())) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        }
    }

    private boolean paginaPublica(String path) {
        return path.equals("/login") || path.equals("/cadastro");
    }

    private boolean temAcesso(UsuarioDTO usuario, String path) {
        switch (usuario.getTipoUsuario()) {
            case "Administrador":
                return path.startsWith("/adminPanel");
            case "Atendente":
                return path.startsWith("/chamados");
            case "Usuário":
                return path.startsWith("/home");
            default:
                return false;
        }
    }

    private boolean tokenValido(String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            JsonWebToken jwt = Jwt.issuer("your-issuer").decode(jwtToken);
            return jwt != null;
        } catch (Exception e) {
            return false;
        }
    }

    private UsuarioDTO getUsuarioDoToken(String token) {
        // Lógica para extrair o usuário do token
        return new UsuarioDTO();
    }
}
