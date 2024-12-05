package com.example.demo.filter;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.web.SecurityFilterChain;
import com.example.demo.handler.CustomLogoutHandler;
import com.example.demo.handler.CustomizeAuthenticationSuccessHandler;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import java.util.*;

/**
 * Class to configure AWS Cognito as an OAuth 2.0 authorizer with Spring Security.
 * In this configuration, we specify our OAuth Client.
 * We also declare that all requests must come from an authenticated user.
 * Finally, we configure our logout handler.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private final CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;
	

	  private String logoutUrl = "https://mydomain.auth.us-east-1.amazoncognito.com/logout";

	  private String logoutRedirectUrl = "https://mydomain/logout";

	  private String clientId = "q6017u916qs5tvvgg3070ct5p" ;


    public SecurityConfiguration(CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler) {

        this.customizeAuthenticationSuccessHandler = customizeAuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(request -> request.requestMatchers("/").permitAll()
                        .requestMatchers("/admin/*").hasRole("Admin")
                        .requestMatchers("/user/*").hasAnyRole("Admin", "User").anyRequest().authenticated())
                .oauth2Login(oauth -> oauth.redirectionEndpoint(endPoint -> endPoint.baseUri("/login/oauth2/code/cognito"))
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userAuthoritiesMapper(userAuthoritiesMapper()))
                        .successHandler(customizeAuthenticationSuccessHandler))
                .logout(httpSecurityLogoutConfigurer -> {
                    httpSecurityLogoutConfigurer.logoutSuccessHandler(
                        new CustomLogoutHandler(logoutUrl, logoutRedirectUrl, clientId));
                  });
        return http.build();
    }

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            try {
                OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) new ArrayList<>(authorities).get(0);

                mappedAuthorities = ((ArrayList<?>) oidcUserAuthority.getAttributes().get("cognito:groups")).stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toSet());
            } catch (Exception exception) {
                System.out.println("Not Authorized!");

                System.out.println(exception.getMessage());
            }

            return mappedAuthorities;
        };
    }
}