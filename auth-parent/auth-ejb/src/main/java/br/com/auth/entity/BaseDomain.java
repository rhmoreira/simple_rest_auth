package br.com.auth.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class BaseDomain<PK extends Serializable> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DEFAULT_SEQ_GEN")
//	@SequenceGenerator(name="DEFAULT_SEQ_GEN", sequenceName="SEQ_AUTH", initialValue=1, allocationSize=1)
	private PK id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	public PK getId() {
		return id;
	}
	public void setId(PK id) {
		this.id = id;
	}
	public Date getData() {
		return data;
	}
	
	@PrePersist
	private void updateData(){
		this.data = new Date();
	}

}
