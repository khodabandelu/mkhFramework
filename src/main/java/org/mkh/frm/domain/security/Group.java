package org.mkh.frm.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.mkh.frm.domain.BaseEntity;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="core_group")
@FilterDef(name="groupAuthorize", parameters={
		@ParamDef( name="userId", type="integer" )
})
@Filters( {
  @Filter(name="groupAuthorize", condition="(id in (select ug.group_id from core_user_group ug where ug.user_id=:userId) )  ")
} )
@Getter
@Setter
@NoArgsConstructor
public class Group extends BaseEntity<Integer> {

	@Column(name = "code",unique=true)
	private String code;

	@Column(name = "group_name")
	private String groupName;

	@ManyToMany(mappedBy="groups")
	@JsonIgnore
	private Set<User> users = new HashSet<User>(0);

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "core_group_action",
				joinColumns = { @JoinColumn(name = "group_id", nullable = false, updatable = false) },
				inverseJoinColumns = { @JoinColumn(name = "action_id",nullable = false, updatable = false) })
	@JsonIgnore
	private Set<Action> actions = new HashSet<Action>(0);


	public Group(Integer id) {
		super();
		super.setId(id);
	}
}
