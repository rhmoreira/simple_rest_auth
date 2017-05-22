package br.com.auth.web.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.auth.dist.classes.AuthException;
import br.com.auth.dist.classes.Message;
import br.com.auth.dist.classes.Token;
import br.com.auth.dist.classes.User;
import br.com.auth.ejb.AuthService;
import br.com.auth.ejb.TokenSessionService;
import br.com.auth.entity.TokenSession;
import br.com.auth.entity.Usuario;
import br.com.auth.util.RestEntityConverter;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource extends BaseResource {

	@Inject
	private AuthService authService;
	
	@Inject
	private TokenSessionService tokenService;
	
	@Path("/login")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public Response login(AuthResourceJSON json) throws AuthException{
		TokenSession tokenSession = authService.doLogin(json.getLogin(), json.getSenha());
		RestEntityConverter converter = new RestEntityConverter();
		Token token = converter.convertToken(tokenSession);
		return ok(token);
	}
	
	@Path("/user")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUser(AuthResourceJSON json) throws AuthException{
		Usuario usuario = authService.getUserByToken(json.getToken());
		RestEntityConverter converter = new RestEntityConverter();
		User user  = converter.convertUser(usuario);
		
		return ok(user);
	}
	
	@Path("/token/validate")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	public Response validate(AuthResourceJSON json) throws AuthException{
		tokenService.validate(json.getToken());
		return ok();
	}
	
	@Path("/token/refresh")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	public Response refresh(AuthResourceJSON json) throws AuthException{
		tokenService.refreshToken(json.getToken(), json.getSenha());
		return ok();
	}
	
	@Path("/access")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	public Response checkAccess(AuthResourceJSON json){
		Long acessoId = authService.getAcesso(json.getToken(), json.getAccessPath(), json.getAccessMethod());
		if (acessoId == null)
			return respond(Status.UNAUTHORIZED, Message.newMessage("Acesso negado"));
		else
			return ok(json.getToken());
	}
	
	@Path("/token/invalidate")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	public Response invalidateToken(AuthResourceJSON json){
		tokenService.invalidateSession(json.getToken());
		return ok();
	}
}
