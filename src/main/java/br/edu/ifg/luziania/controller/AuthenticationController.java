package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.bo.UsuarioBO;
import br.edu.ifg.luziania.model.dto.AutenticacaoDTO;
import br.edu.ifg.luziania.model.dto.UsuarioDTO;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class AuthenticationController {

    @Inject
    UsuarioBO usuarioBO;

    @GET
    public Response redirecionamentoLogin(){
        return Response.status(Response.Status.FOUND).header("Location", "/login").build();
    }

    @POST
    @Path("autenticarUsuario")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response autenticarUsuario(AutenticacaoDTO autenticacaoDTO){
        return usuarioBO.autenticarUsuario(autenticacaoDTO);
    }

    @POST
    @Path("deslogar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deslogar() {
        return usuarioBO.deslogar();
    }
}
