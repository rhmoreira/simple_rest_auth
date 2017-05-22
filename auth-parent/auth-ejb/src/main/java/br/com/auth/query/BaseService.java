package br.com.auth.query;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.auth.entity.BaseDomain;

public class BaseService<T extends BaseDomain<?>> {

	@PersistenceContext(unitName="Auth-PU")
	protected EntityManager em;	
	
}
