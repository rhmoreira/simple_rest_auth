package br.com.auth.ejb;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.auth.entity.BaseDomain;

public class BaseService<T extends BaseDomain<?>> {

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
	
	public T salvar(T t){
		if (t.getId() == null)
			em.persist(t);
		else
			t = em.merge(t);
		
		return t;
	}
	
	public void remover(Serializable pk){
		T reference = em.getReference(getParametrizedClass(), pk);
		em.remove(reference);
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> getParametrizedClass(){
		Type genericBaseEntity = getClass().getGenericSuperclass();
		return (Class<T>) ((ParameterizedType)genericBaseEntity).getActualTypeArguments()[0];
	}
	
	public String generateHash(String... values){
		try{
			if (values != null){
				StringBuilder sb = new StringBuilder();
				Arrays.stream(values)
					  .forEach(value -> sb.append(value));
				
				MessageDigest messageDigest = MessageDigest.getInstance("MD5");
				messageDigest.update(sb.toString().getBytes());
				
				return new String(Base64.getEncoder().encode(messageDigest.digest()));
			}
		}catch (Exception e) { /*Nao ha perigo desse algoritimo nao existir */ }
		return null;
	}
}
