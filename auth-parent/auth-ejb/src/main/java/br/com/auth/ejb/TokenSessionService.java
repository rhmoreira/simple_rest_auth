package br.com.auth.ejb;

import java.time.LocalDateTime;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import br.com.auth.dist.classes.AuthException;
import br.com.auth.entity.TokenSession;
import br.com.auth.entity.Usuario;
import br.com.auth.query.TokenSessionQuery;

@Stateless
@LocalBean
public class TokenSessionService extends BaseService<TokenSession> {

	@Inject
	private TokenSessionQuery tokenQuery;
	
	public TokenSession startTokenSession(Usuario usuario, LocalDateTime expireDate, String token) {
		
		invalidateSessions(usuario);
		
		TokenSession session = new TokenSession();
		session.setUsuario(usuario);
		session.setToken(token);
		session.setDataExpiracao(expireDate);
		session.setAtivo(Boolean.TRUE);
		
		return salvar(session);
	}
	
	public String validate(String token) throws AuthException {
		String validToken = tokenQuery.findValidToken(token);
		if (validToken == null)
			throw new AuthException(HttpsURLConnection.HTTP_NOT_ACCEPTABLE, "Token inválido ou expirado");
		return token;
	}
	
	public void refreshToken(String token, String senha) throws AuthException{
		if (senha == null)
			throw new AuthException(HttpsURLConnection.HTTP_UNAUTHORIZED, "Informe senha para atualizar o token");
		if (token == null)
			throw new AuthException(HttpsURLConnection.HTTP_UNAUTHORIZED, "Informe o token");
		
		String hash = generateHash(senha);
		
		String jpql = "Update TokenSession ts SET ts.dataExpiracao = :novaData " +
					  "Where ts.ativo = :ativoParam And ts.token = :token " +
					  "And ts.usuario = (Select u From Usuario u Where u.senha = :senha And u = ts.usuario)";
		int updated = em.createQuery(jpql)
						  .setParameter("novaData", LocalDateTime.now().plusHours(2l))
						  .setParameter("ativoParam", Boolean.TRUE)
						  .setParameter("token", token)
						  .setParameter("senha", hash)
						  .executeUpdate();
		if (updated == 0)
			throw new AuthException(HttpsURLConnection.HTTP_NOT_ACCEPTABLE, "Token invalido ou expirado");
	}
	
	public void invalidateSessions(Usuario usuario){
		String jpql = "Update TokenSession ts SET ts.ativo = :ativoParam Where ts.usuario = :usuario";
		em.createQuery(jpql)
		  .setParameter("ativoParam", Boolean.FALSE)
		  .setParameter("usuario", usuario)
		  .executeUpdate();
	}
	
	public void invalidateSession(String token){
		String jpql = "Update TokenSession ts SET ts.ativo = :ativoParam Where ts.token = :token";
		int updated = em.createQuery(jpql)
						.setParameter("ativoParam", Boolean.FALSE)
						.setParameter("token", token)
						.executeUpdate();
		if (updated == 0)
			System.out.println("Tentativa de invalidar um token expirado ou inexistente");
	}

}
