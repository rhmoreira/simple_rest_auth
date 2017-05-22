package br.com.auth.util;

import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import br.com.auth.dist.classes.Access;
import br.com.auth.dist.classes.Profile;
import br.com.auth.dist.classes.Token;
import br.com.auth.dist.classes.User;
import br.com.auth.entity.Acesso;
import br.com.auth.entity.Perfil;
import br.com.auth.entity.TokenSession;
import br.com.auth.entity.Usuario;

public class RestEntityConverter {

	public Access convertAccess(Acesso acesso){
		Access a = new Access();
		a.setMethod(acesso.getMethod());
		a.setName(acesso.getNome());
		a.setPath(acesso.getPath());
		
		return a;
	}
	
	public Profile convertProfile(Perfil perfil){
		Profile f = new Profile();
		f.setName(perfil.getNome());
		if (perfil.getAcessos() != null && !perfil.getAcessos().isEmpty()){
			List<Access> accesses = 
					perfil.getAcessos()
							.stream()
							.map(a -> convertAccess(a))
							.collect(Collectors.toList());
			f.setAccesses(accesses);
		}
		return f;
	}
	
	public User convertUser(Usuario usuario){
		User u = new User();
		u.setLogin(usuario.getLogin());
		if (usuario.getPerfil() != null)
			u.setProfile(convertProfile(usuario.getPerfil()));
		return u;
	}
	
	public Token convertToken(TokenSession token){
		Token t = new Token();
		t.setToken(token.getToken());
		Instant instant = token.getDataExpiracao().atZone(ZoneId.systemDefault()).toInstant();
		t.setExpireDate(Date.from(instant));
		
		return t;
	}
}
