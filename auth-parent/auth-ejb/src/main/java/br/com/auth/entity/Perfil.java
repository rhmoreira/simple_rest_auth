package br.com.auth.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="PERFIL")
public class Perfil extends BaseDomain<Long>{

	private String nome;
	
	@OneToMany(mappedBy="perfil", fetch=FetchType.EAGER)
	private Set<Acesso> acessos;

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Set<Acesso> getAcessos() {
		return acessos;
	}
	public void setAcessos(Set<Acesso> acessos) {
		this.acessos = acessos;
	}
}
