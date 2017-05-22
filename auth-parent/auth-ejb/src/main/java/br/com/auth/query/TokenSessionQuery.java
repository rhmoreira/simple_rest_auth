package br.com.auth.query;

import java.time.LocalDateTime;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.auth.entity.TokenSession;

@Named
@Dependent
public class TokenSessionQuery extends BaseQuery<TokenSession>{

	public String findValidToken(String token) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> query = cb.createQuery(String.class);
		
		Root<TokenSession> from = query.from(TokenSession.class);
		
		Predicate clause = 
			cb.and(
				cb.equal(from.<String>get("ativo"), Boolean.TRUE),
				cb.equal(from.<String>get("token"), token),
				cb.greaterThan(from.<LocalDateTime>get("dataExpiracao"), LocalDateTime.now())
			);
		query.where(clause)
			 .select(from.<String>get("token"));
		
		token = getSingleResult(em.createQuery(query));
		return token;
	}
}
