package org.mkh.frm.web.viewModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public abstract class BaseEntityViewModel<T> implements Serializable  {
	
	private static final long serialVersionUID = 4295229462159851306L;

	private T id;

	@JsonIgnore
	private Integer version;

	@JsonIgnore
	private String ip="127.0.0.1";

	@JsonProperty
	private String captcha="captcha";
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date createdDate = new Date();
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Date updatedDate = new Date();
	
}
