package org.mkh.frm.security;

import org.mkh.frm.config.FrmProperties;
import org.mkh.frm.domain.security.User;
import org.mkh.frm.service.security.IUserService;
import org.mkh.frm.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SecurityUtility {

    public static final int OTHER_ERROR = -1;
    public static final int USERNAME_OR_PASSWORD_IS_WRONG = 0;
    public static final int IP_NOT_ALLOWD = 1;
    public static final int BROWSER_NOT_ALLOWD = 2;
    public static final int OS_NOT_ALLOWD = 3;
    public static final int EXPIRE_PASSWORD_CREDIT = 4;
    public static final int BLOCK_USER = 5;
    public static final int CAPTCHA_IS_WRONG = 6;
    public static final int ROLE_ACCESS_DENIED = 7;
    public static final int USER_ALREADY_LOGIN = 8;
    public static final int EXPIRE_USER_CREDIT = 9;

    public static final String FORCE_TO_CHANGE_PASS_SESSION_ATTRIBUTE = "isForceToChangePass";
    public static final String RE_AUTINTICATE_SESSION_ATTRIBUTE = "reAutinticate";
    public static final String REFERER_SESSION_ATTRIBUTE = "j_referer";

    public static final Boolean IS_CHECK_FORCE_USER_CHANGE_PASSWORD = false;


    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    FrmProperties frmProperties;


    @Autowired
    private IUserService userService;

    /**
     * get Authentication personel
     *
     * @return PersonelId
     */
    public static Integer getAuthenticatedUserId() {
        try{
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user != null)
                return user.getId();
        }
        return 1;
        }finally {
            return 1;
        }
    }


    public static User getAuthenticatedUser() {
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception ex) {
            return new User(1);
        }
    }


    public static String getRequestedIp() {
        if (RequestContextHolder.getRequestAttributes() != null)
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRemoteHost();
        else
            return "127";
    }

    /**
     * return localhost:8081/app
     * or vahed98.com:80/app
     *
     * @return
     */
    public static String getServerURI() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        } else
            return "localhost:8080";
    }

    public static boolean isInRole(String role) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (GrantedAuthority userAuthoritie : user.getAuthorities()) {
            if (userAuthoritie.getAuthority().equalsIgnoreCase(role))
                return true;
        }
        return false;
    }


    public static void logoffUser(HttpServletRequest request, HttpServletResponse response) {
        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        cookieClearingLogoutHandler.logout(request, response, null);
        securityContextLogoutHandler.logout(request, response, null);
    }


    public static String marshallingUserToJSON(User user) {
        return Utility.getJsonByObject(user);
    }

    public static User unMarshallingUserToJSON(String userJSON) {
        return (User) Utility.getObjectByJson(userJSON, User.class);
    }

    private Cookie getSocialAuthenticationCookie(String token) {
        Cookie socialAuthCookie = new Cookie("jh-social-jwt", token);
        socialAuthCookie.setPath("/");
        socialAuthCookie.setMaxAge(10);
        return socialAuthCookie;
    }


}
