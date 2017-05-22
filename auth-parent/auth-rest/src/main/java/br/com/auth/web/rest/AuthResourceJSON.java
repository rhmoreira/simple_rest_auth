package br.com.auth.web.rest;

public class AuthResourceJSON {

	private String login;
	private String senha;
	private String token;
	private String accessPath;
	private String accessMethod;
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAccessPath() {
		return accessPath;
	}
	public void setAccessPath(String accessPath) {
		this.accessPath = accessPath;
	}
	public String getAccessMethod() {
		return accessMethod;
	}
	public void setAccessMethod(String accessMethod) {
		this.accessMethod = accessMethod;
	}
}
