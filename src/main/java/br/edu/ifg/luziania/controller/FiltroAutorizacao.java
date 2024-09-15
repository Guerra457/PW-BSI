package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.bo.UsuarioBO;
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
import java.security.Principal;
import java.util.Set;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class FiltroAutorizacao implements ContainerRequestFilter {
    @Inject
    private UsuarioBO usuarioBO;

    @Inject
    JsonWebToken jwt;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        // Verifique se o caminho corresponde às páginas públicas
        if (paginaPublica(path)) {
            // Se for uma página pública, não aplique o filtro de autenticação
            return;
        }

        String token = requestContext.getHeaderString("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // Remove "Bearer " do início do token
            System.out.println("Token recebido: " + token);
        } else {
            System.out.println("Token não encontrado ou inválido");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        UsuarioDTO usuario;
        try {
            usuario = usuarioBO.obterUsuarioDoToken(token);
        }catch (Exception e) {
            System.out.println("Token invçaido ou erro ao obter usuário: " + e.getMessage());
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        SecurityContext originalContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new Principal() {
                    @Override
                    public String getName() {
                        return usuario.getEmail();
                    }
                };
            }

            @Override
            public boolean isUserInRole(String role) {
                return usuario.getTipoUsuario().equals(role);
            }

            @Override
            public boolean isSecure() {
                return originalContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }
        });


        if (jwt == null || !tokenValido(token)) {
            requestContext.abortWith(Response.seeOther(java.net.URI.create("/login")).build());
            return;
        }

        if (!temAcesso(usuario, path)) {
            System.out.println("Usuário sem permissão para acessar a página: " + path);
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        }
    }

    private boolean paginaPublica(String path) {
        return path.equals("/login") || path.equals("/cadastro") || path.equals("/home") || path.equals("/chamados") || path.equals("/adminPanel") || path.equals("/usuarios");
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
            return jwt != null && jwt.getClaim("sub") != null;  // Verifica se existe um claim 'sub'
        } catch (Exception e) {
            return false;
        }
    }
}
