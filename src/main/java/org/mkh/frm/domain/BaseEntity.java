package org.mkh.frm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mkh.frm.domain.security.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties({ "createdBy", "updatedBy" })
@MappedSuperclass
@EntityListeners(BaseEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public abstract class BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 4295229462159851306L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private T id;

//	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdby", updatable = false)
	@JsonIgnore
	private User createdBy;

//	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updatedby")
	@JsonIgnore
	private User updatedBy;

	@Column(name = "createddate", updatable = false)
	private Date createdDate = new Date();

	@Column(name = "updateddate")
	private Date updatedDate = new Date();

	@Column(name = "ip")
	private String ip = "127.0.0.1";
	

	public BaseEntity(T id) {
		super();
		this.id = id;
	}

}
