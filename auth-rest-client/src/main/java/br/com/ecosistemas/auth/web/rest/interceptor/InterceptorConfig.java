package br.com.ecosistemas.auth.web.rest.interceptor;

import java.lang.reflect.Method;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
public class InterceptorConfig implements DynamicFeature{

	@Override
	public void configure(ResourceInfo rsrc, FeatureContext ctx) {
		Class<?> resourceClass = rsrc.getResourceClass();
		Method resourceMethod = rsrc.getResourceMethod();
		
		register(resourceClass, resourceMethod, ctx);
	}
	
	private void register(Class<?> rsrcClass, Method resourceMethod, FeatureContext ctx){
		Restrict restrictClass = rsrcClass.getAnnotation(Restrict.class);
		Restrict restrictMethod = resourceMethod.getAnnotation(Restrict.class);
		
		if (restrictClass != null && restrictClass.value() && restrictMethod == null)
			ctx.register(RestrictedInterceptor.class);
		else if (restrictMethod != null && restrictMethod.value())
			ctx.register(RestrictedInterceptor.class);
			
	}
}