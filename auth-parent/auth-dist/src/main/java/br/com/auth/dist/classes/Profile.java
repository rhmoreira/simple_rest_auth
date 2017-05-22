package br.com.auth.dist.classes;

import java.util.Collection;

public class Profile{

	private String name;
	private Collection<Access> accesses;

	public boolean hasAccess(String accessName){
		return 
				accesses.stream()
				.filter(a -> a.getName().equals(accessName))
				.findAny()
				.isPresent();
	}
	public boolean hasAccess(String path, String method){
		return 
				accesses.stream()
				.filter(a -> a.getPath().equals(path) && a.getMethod().equals(method))
				.findAny()
				.isPresent();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Collection<Access> getAccesses() {
		return accesses;
	}
	public void setAccesses(Collection<Access> accesses) {
		this.accesses = accesses;
	}
}
