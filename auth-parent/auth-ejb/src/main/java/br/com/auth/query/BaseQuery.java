package br.com.auth.query;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.auth.entity.BaseDomain;

public class BaseQuery<T extends BaseDomain<?>> {

	@PersistenceContext(unitName="Auth-PU")
	protected EntityManager em;	

	
	protected <Y> Y getSingleResult(TypedQuery<Y> query){
		try{
			Y result = query.getSingleResult();
			return result;
		}catch (NoResultException e) {
			return null;
		}
	}
}
