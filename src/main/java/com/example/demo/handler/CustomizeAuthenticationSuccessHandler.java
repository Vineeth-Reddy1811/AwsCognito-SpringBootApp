package com.example.demo.handler;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


//Class to handling scenario when login is successfull.
@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		for (GrantedAuthority auth : authentication.getAuthorities()) {

            DefaultOidcUser defaultOidcUser = (DefaultOidcUser) authentication.getPrincipal();

            Map<String, Object> userAttributes = defaultOidcUser.getAttributes();

            System.out.println(userAttributes);


            for (GrantedAuthority auth1 : authentication.getAuthorities()) {
                System.out.println("Role: " + auth1.getAuthority());
                if ("ROLE_Admin".equals(auth1.getAuthority())) {
                    System.out.println(userAttributes.get("cognito:username") + " Is Admin!");
                    response.sendRedirect("/admin/greeting");
                    return;
                } else if ("ROLE_User".equals(auth1.getAuthority())) {
                    System.out.println(userAttributes.get("cognito:username") + " Is User!");
                    response.sendRedirect("/user/greeting");
                    return;
                }
            }

            // Default fallback
            response.sendRedirect("/");
        }
	}
}

