package org.mkh.frm.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;
import org.hibernate.validator.constraints.Email;
import org.mkh.frm.domain.BaseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "core_user", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
@FilterDefs({
        @FilterDef(name = "defaultFilter", parameters = {@ParamDef(name = "userId", type = "integer")}),
        @FilterDef(name = "userAuthorize", parameters = {@ParamDef(name = "userId", type = "integer")})
})
@Filter(name = "defaultFilter", condition = "( -1!=:userId  ) ")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity<Integer> implements UserDetails {

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private String passwordPlain;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "cellphone")
    private String cellphone;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "avatar_Image_code")
    private String avatarImageCode;

    @Column(name = "is_male")
    private Boolean gender;// jensiyat 1 mard 0 zan

    @Column(name = "is_enabled", nullable = false)
    private boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "core_user_group",
            joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "group_id", nullable = false, updatable = false)})
    private Set<Group> groups = new HashSet<Group>();

    @Transient
    private Collection<GrantedAuthority> authorities;

    public User(Integer id) {
        super.setId(id);
    }

    public User(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, Collection<GrantedAuthority> authorities) {
        super();
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public User(Integer id, String username, Collection<GrantedAuthority> authorities) {
        super();
        super.setId(id);
        this.username = username;
        this.authorities = authorities;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getFullName() {
        return (getFirstName() != null ? getFirstName() : "") + " " + (getLastName() != null ? getLastName() : "");
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return !(user.getId() == null || getId() == null) && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
