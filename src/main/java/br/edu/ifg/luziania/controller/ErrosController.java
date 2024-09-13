package br.edu.ifg.luziania.controller;

import br.edu.ifg.luziania.model.util.ErroTemplates;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("erro")
public class ErrosController {
    @GET
    @Path("acesso-negado")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance acessoNegado(){
        return ErroTemplates.acessoNegado();
    }
}
