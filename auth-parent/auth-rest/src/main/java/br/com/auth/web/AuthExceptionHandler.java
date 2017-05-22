package br.com.auth.web;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.auth.dist.classes.AuthException;
import br.com.auth.dist.classes.Message;

@Provider
public class AuthExceptionHandler implements ExceptionMapper<AuthException> {

	@Override
	public Response toResponse(AuthException e) {
		Status statusCode = Status.fromStatusCode(e.getStatusCode());
		Response response = Response.status(statusCode)
									.entity(Message.newMessage(e.getMessage()))
									.build();
		return response;
	}

}
