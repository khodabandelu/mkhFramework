package org.mkh.frm.config.security;

import org.mkh.frm.security.AuthoritiesConstants;
import org.mkh.frm.security.CustomUserDetailsService;
import org.mkh.frm.security.jwt.JWTConfigurer;
import org.mkh.frm.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@Import(SecurityProblemSupport.class)
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Inject
    private CustomUserDetailsService userDetailsService;

    private final TokenProvider tokenProvider;

    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(TokenProvider tokenProvider, SecurityProblemSupport problemSupport) {
        this.tokenProvider = tokenProvider;
        this.problemSupport = problemSupport;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/bower_components/**")
                .antMatchers("/dist/**")
                .antMatchers("/app/**")
                .antMatchers("/content/**")
                .antMatchers("/index.html*")
                .antMatchers("/j-captcha.jpg")
                .antMatchers("/favicon.png")
                .antMatchers("/swagger-ui/index.html")
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable();
        http
                .exceptionHandling()
//                .authenticationEntryPoint(problemSupport)
//                .accessDeniedHandler(problemSupport)
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/social/**", "/connect/**", "/signin/**", "/**/public/**").permitAll()
                .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/configuration/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/ws/**").permitAll()
                .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/").permitAll()
                .antMatchers("/**").fullyAuthenticated()
                .and()
                .apply(securityConfigurerAdapter());
//        .and()
//            .sessionManagement()
//            .sessionFixation().changeSessionId()
//            .maximumSessions(1)
//            .maxSessionsPreventsLogin(true)
//            .sessionRegistry(sessionRegistry());
    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }

    @Bean
    public static HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    //@Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl r = new RoleHierarchyImpl();
        r.setHierarchy("ROLE_ADMIN > ROLE_STAFF and ROLE_ADMIN > ROLE_DEVELOPER and ROLE_STAFF > ROLE_USER and ROLE_DEVELOPER > ROLE_USER");
        return r;
    }
}
