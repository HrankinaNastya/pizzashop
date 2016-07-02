package com.hrankina.pizzashop.util;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * creation date 25.06.2016
 *
 * @author A.Hrankina
 */
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String key;

        if (exception.getClass().isAssignableFrom(UsernameNotFoundException.class) || exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
            key = "notfound";
        } else if (exception.getClass().isAssignableFrom(DisabledException.class)) {
            key = "disabled";
        } else {
            key = "other";
        }

        getRedirectStrategy().sendRedirect(request, response, "/login.html?" + key);
    }
}
