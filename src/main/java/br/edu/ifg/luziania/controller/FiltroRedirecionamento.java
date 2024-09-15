package br.edu.ifg.luziania.controller;

import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@Priority(1)
public class FiltroRedirecionamento implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if ("/".equals(requestContext.getUriInfo().getPath())) {
            requestContext.abortWith(Response.status(Response.Status.FOUND)
                    .header("Location", "/login").build());
        }
    }
}
