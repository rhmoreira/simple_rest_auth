package br.com.ecosistemas.auth.web.rest.interceptor;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.auth.dist.classes.Message;
import br.com.ecosistemas.auth.web.AuthFacade;


public class RestrictedInterceptor implements ContainerRequestFilter, ContainerResponseFilter{

	private static AuthFacade authFacade = new AuthFacade();

	@Override
	public void filter(ContainerRequestContext ctx) throws IOException, WebApplicationException {
		String authToken = getAuthToken(ctx);
		
		boolean validToken = authFacade.validateToken(authToken);

		if (!validToken)
			abort(ctx);
		else{
			boolean hasAccess = authFacade.checkAccess(authToken, ctx.getUriInfo().getPath(), ctx.getMethod());
			if (!hasAccess)
				abort(ctx);	
		}
	}
	
	private void abort(ContainerRequestContext ctx){
		ctx.abortWith(Response.status(Status.UNAUTHORIZED).entity(Message.newMessage("Acesso Negado")).build());
	}

	private String getAuthToken(ContainerRequestContext reqCtx){
		String authHeader = reqCtx.getHeaderString("Authorization");
		authHeader = authHeader == null ? reqCtx.getHeaderString("authorization"): authHeader;
		if (authHeader == null || authHeader.trim().length() <= 6)
			return null;
		String authToken = authHeader.substring("Basic ".length()).trim();
		return authToken;		
	}

	@Override
	public void filter(ContainerRequestContext reqCtx, ContainerResponseContext resCtx) throws IOException {
	}
}