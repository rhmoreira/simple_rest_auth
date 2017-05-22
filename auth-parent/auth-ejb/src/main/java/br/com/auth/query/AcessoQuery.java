package br.com.auth.query;

import java.time.LocalDateTime;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.auth.entity.Acesso;
import br.com.auth.entity.Perfil;
import br.com.auth.entity.TokenSession;
import br.com.auth.entity.Usuario;

@Named
@Dependent
public class AcessoQuery extends BaseQuery<Acesso>{

	public Long findAcesso(String token, String path, String method) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		
		Root<TokenSession> from = query.from(TokenSession.class);
		Join<Perfil, Acesso> joinAcesso = 
				from.<TokenSession, Usuario>join("usuario")
					.<Usuario, Perfil>join("perfil")
					.<Perfil, Acesso>join("acessos");
		
		joinAcesso.alias("a");
		
		Predicate clause = 
			cb.and(
				cb.equal(from.<String>get("ativo"), Boolean.TRUE),
				cb.equal(from.<String>get("token"), token),
				cb.greaterThan(from.<LocalDateTime>get("dataExpiracao"), LocalDateTime.now()),
				cb.equal(joinAcesso.<String>get("path"), path),
				cb.equal(joinAcesso.<String>get("method"), method)
			);
		query.where(clause)
			 .select(joinAcesso.<Long>get("id"));
		
		Long acessoId = getSingleResult(
				em.createQuery(query)
				  .setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.USE)
				  .setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH)
			);
		
		return acessoId;
	}
}
