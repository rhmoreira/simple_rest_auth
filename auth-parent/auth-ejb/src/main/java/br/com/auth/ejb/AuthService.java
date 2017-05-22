package br.com.auth.ejb;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import br.com.auth.dist.classes.AuthException;
import br.com.auth.entity.Acesso;
import br.com.auth.entity.Perfil;
import br.com.auth.entity.TokenSession;
import br.com.auth.entity.Usuario;
import br.com.auth.query.AcessoQuery;
import br.com.auth.query.UsuarioQuery;

@Stateless
@LocalBean
public class AuthService extends BaseService<Usuario>{
	
	@Inject
	private UsuarioQuery usuarioQuery;
	
	@Inject
	private AcessoQuery acessoQuery;
	
	@EJB
	private TokenSessionService tokenService;
	
	
	public TokenSession doLogin(String login, String pass) throws AuthException{
		Usuario usuario = usuarioQuery.findByLoginAndSenha(login, generateHash(pass));
		
		if (usuario == null)
			throw new AuthException(HttpsURLConnection.HTTP_NOT_FOUND, "Usuário ou senha incorreta");
		else{
			LocalDateTime expireDate = LocalDateTime.now().plusHours(2l);
			String token = generateHash(usuario.getLogin(), usuario.getSenha(), expireDate.toString());
			
			return tokenService.startTokenSession(usuario, expireDate, token);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario getUserByToken(String token) throws AuthException{
		Usuario usuario = usuarioQuery.findByValidToken(token);
		if(usuario == null)
			throw new AuthException(HttpsURLConnection.HTTP_NOT_ACCEPTABLE, "Token inválido ou expirado");
		
		return usuario;
	}
	
	public Long getAcesso(String token, String acessoPath, String acessoMetodo){
		return acessoQuery.findAcesso(token, acessoPath, acessoMetodo);		
	}
	
	@PostConstruct
	private void preLoadData(){
		
		criarPerfis();
		
		criarAcessos();
		
		criarUsuarios();
	}

	private void criarPerfis() {
		Perfil perfilAdmin = new Perfil();
		perfilAdmin.setNome("ADMIN");
		
		em.persist(perfilAdmin);
		
		Perfil perfilAtend = new Perfil();
		perfilAdmin.setNome("ATENDENTE");
		
		em.persist(perfilAtend);
		
		em.flush();
	}
	
	private void criarAcessos() {

		Perfil perfilAdmin = em.getReference(Perfil.class, 1L);
		Perfil perfilAtendente = em.getReference(Perfil.class, 2L);
		
		Acesso acesso = new Acesso();
		acesso = new Acesso();
		acesso.setNome("SISTEMA_GET");
		acesso.setPath("/sistema");
		acesso.setMethod("GET");
		acesso.setPerfil(perfilAdmin);
		
		em.persist(acesso);

		acesso = new Acesso();
		acesso.setNome("SISTEMA_POST");
		acesso.setPath("/sistema");
		acesso.setMethod("POST");
		acesso.setPerfil(perfilAdmin);
		
		em.persist(acesso);
		
		acesso = new Acesso();
		acesso.setNome("PACIENTE_CPF_GET");
		acesso.setPath("/paciente/cpf");
		acesso.setMethod("GET");
		acesso.setPerfil(perfilAdmin);
		
		em.persist(acesso);
		
		acesso = new Acesso();
		acesso.setNome("PACIENTE_CPF_CNS_GET");
		acesso.setPath("/paciente/cpf/cns");
		acesso.setMethod("GET");
		acesso.setPerfil(perfilAdmin);
		
		em.persist(acesso);
		
		acesso = new Acesso();
		acesso.setNome("PACIENTE_GET");
		acesso.setPath("/paciente");
		acesso.setMethod("GET");
		acesso.setPerfil(perfilAdmin);
		
		em.persist(acesso);
		
		acesso = new Acesso();
		acesso.setNome("PACIENTE_POST");
		acesso.setPath("/paciente");
		acesso.setMethod("POST");
		acesso.setPerfil(perfilAdmin);
		
		em.persist(acesso);
		
		acesso = new Acesso();
		acesso.setNome("PACIENTE_JOIN_POST");
		acesso.setPath("/paciente/join");
		acesso.setMethod("POST");
		acesso.setPerfil(perfilAdmin);
		
		em.persist(acesso);
		
		acesso = new Acesso();
		acesso.setNome("HISTORICO_PACIENTE_POST");
		acesso.setPath("/paciente/historico");
		acesso.setMethod("POST");
		acesso.setPerfil(perfilAdmin);
		
		em.persist(acesso);
		
		acesso = new Acesso();
		acesso.setNome("HISTORICO_PACIENTE_PUT");
		acesso.setPath("/paciente/historico");
		acesso.setMethod("PUT");
		acesso.setPerfil(perfilAdmin);
		
		em.persist(acesso);
	}
	
	private void criarUsuarios(){
		Perfil perfilAdmin = em.getReference(Perfil.class, 1L);
		Perfil perfilAtendente = em.getReference(Perfil.class, 2L);
		
		Usuario administrador = new Usuario();
		administrador.setLogin("admin");
		administrador.setSenha(generateHash("1234"));
		administrador.setPerfil(perfilAdmin);
		
		em.persist(administrador);
		
		Usuario atendente = new Usuario();
		atendente.setLogin("atendente");
		atendente.setSenha(generateHash("1234"));
		atendente.setPerfil(perfilAtendente);
		
		em.persist(atendente);
	}
}
