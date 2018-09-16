package org.mkh.frm.web.viewModel.security;

import lombok.Getter;
import lombok.Setter;
import org.mkh.frm.web.viewModel.BaseEntityViewModel;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserViewModel extends BaseEntityViewModel<Integer> {
	
	private String fullName;
	private String username;
	//@JsonIgnore
	private String password;
	
	private String firstName;
	private String lastName;
	private String birthDate;
	private String cellphone;
	private String avatarImageCode;
	private Boolean gender;// jensiyat 1 mard 0 zan
	//@JsonIgnore
	private String activationKey;
	//@JsonIgnore
	private String resetKey;
	
	private String email;
	private int visitedCount;
	private Date lastVisitDate;
	private String orgTitle;
	private Integer orgId;
	private boolean isEnabled;
	
	
	private Set<GroupViewModel> groups = new HashSet<>();
	
	private Set<String> authorities;

}
