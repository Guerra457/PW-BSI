package br.edu.ifg.luziania.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/")

public class RootResource {
    @GET
    public Response redirecionamentoLogin(){
        return Response.status(Response.Status.FOUND).header("Location", "/login").build();
    }
}
