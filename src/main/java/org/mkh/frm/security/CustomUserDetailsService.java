package org.mkh.frm.security;

import org.mkh.frm.domain.security.User;
import org.mkh.frm.service.security.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component("userDetailsService")
@Transactional(readOnly = true)
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private IUserService userService;

    private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userEntity = userService.findByUsername(username);
        if (userEntity != null) {
            log.debug("------------==" + userEntity.getUsername());
            log.debug("------------==" + userEntity.getPassword());
        } else if (userEntity == null)
            throw new UsernameNotFoundException("user not found");


        buildUserFromUserEntity(userEntity);

        return userEntity;
    }

    public void buildUserFromUserEntity(User userEntity) {

        Set<GrantedAuthority> authorities = new HashSet();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        userEntity.getGroups()
                .stream()
                .forEach(g -> authorities.add(new SimpleGrantedAuthority("ROLE_" + g.getCode()))
                );

        userEntity.setAuthorities(authorities);
    }
}
