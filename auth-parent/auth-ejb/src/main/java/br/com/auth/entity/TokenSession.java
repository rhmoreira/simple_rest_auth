package br.com.auth.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="TOKEN_SESSION")
public class TokenSession extends BaseDomain<Long>{

	private String token;
	
	@Column(name="DATA_EXPIRACAO")
	private LocalDateTime dataExpiracao;
	
	private Boolean ativo;
	
	@ManyToOne
	@JoinColumn(name="ID_USUARIO")
	private Usuario usuario;

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public LocalDateTime getDataExpiracao() {
		return dataExpiracao;
	}
	public void setDataExpiracao(LocalDateTime dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
