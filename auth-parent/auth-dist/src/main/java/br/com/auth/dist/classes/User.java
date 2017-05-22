package br.com.auth.dist.classes;

public class User{

	private String login;
	private Profile profile;
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	public boolean hasAccess(String accessName){
		return profile.hasAccess(accessName);
	}
	public boolean hasAccess(String path, String method){
		return profile.hasAccess(path, method);
	}
}