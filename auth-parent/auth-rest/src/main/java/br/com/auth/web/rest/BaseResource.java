package br.com.auth.web.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import br.com.auth.entity.Usuario;

public class BaseResource {
	
	@Context
	private HttpServletRequest request;

	protected Response ok(Object entity){
		ResponseBuilder responseBuilder = Response.ok(entity);
		return responseBuilder.build();
	}
	
	protected Response ok(){
		return Response.ok().build();
	}
	
	protected Response resourceCreated(){
		return Response.status(Status.CREATED).build();
	}
	
	protected Response respond(Status status, Object entiy){
		return Response.status(status).entity(entiy).build();
	}
	
	protected Usuario getAuthToken(){
		Object object = request.getSession().getAttribute("auth_user");
		return (Usuario) object;
	}
}
