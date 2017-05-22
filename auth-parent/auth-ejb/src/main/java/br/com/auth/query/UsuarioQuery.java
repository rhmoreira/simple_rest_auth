package br.com.auth.query;

import java.time.LocalDateTime;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.auth.entity.TokenSession;
import br.com.auth.entity.Usuario;

@Named
@Dependent
public class UsuarioQuery extends BaseQuery<Usuario>{

	public Usuario findByLoginAndSenha(String login, String senha){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Usuario> query = cb.createQuery(Usuario.class);
		
		Root<Usuario> from = query.from(Usuario.class);
		
		Predicate clause = 
			cb.and(
				cb.equal(from.<String>get("login"), login),
				cb.equal(from.<String>get("senha"), senha)
			);
		query.where(clause);
		
		Usuario usuario = getSingleResult(em.createQuery(query));
		return usuario;
	}
	
	public Usuario findByValidToken(String token){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Usuario> query = cb.createQuery(Usuario.class);
		
		Root<TokenSession> from = query.from(TokenSession.class);
		
		Predicate clause = 
			cb.and(
				cb.equal(from.<String>get("ativo"), Boolean.TRUE),
				cb.equal(from.<String>get("token"), token),
				cb.greaterThan(from.<LocalDateTime>get("dataExpiracao"), LocalDateTime.now())
			);
		query.where(clause)
			 .select(from.<Usuario>get("usuario"));
		
		Usuario usuario = getSingleResult(em.createQuery(query));
		return usuario;
	}
}
