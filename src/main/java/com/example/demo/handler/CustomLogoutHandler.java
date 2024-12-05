package com.example.demo.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;


//Class to handle Logout. IS NOT WORKING YET.
public class CustomLogoutHandler extends SimpleUrlLogoutSuccessHandler  {

	

	  private final String logoutUrl;
	  private final String logoutRedirectUrl;
	  private final String clientId;

	  public CustomLogoutHandler(String logoutUrl, String logoutRedirectUrl, String clientId) {
	    this.logoutUrl = logoutUrl;
	    this.logoutRedirectUrl = logoutRedirectUrl;
	    this.clientId = clientId;
	  }

	  @Override
	  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
	      Authentication authentication) {

	    return UriComponentsBuilder
	        .fromUri(URI.create(logoutUrl))
	        .queryParam("client_id", clientId)
	        .queryParam("logout_uri", logoutRedirectUrl)
	        .encode(StandardCharsets.UTF_8)
	        .build()
	        .toUriString();
	  }
//    /**
//     * The domain of your user pool.
//     */
//    private String domain = "https://us-east-2jys8roz5o.auth.us-east-2.amazoncognito.com";
//
//    /**
//     * An allowed callback URL.
//     */
//    private String logoutRedirectUrl = "<logout uri>";
//
//    /**
//     * The ID of your User Pool Client.
//     */
//    private String userPoolClientId = "q6017u916qs5tvvgg3070ct5p";
//
//    /**
//     * Here, we must implement the new logout URL request. We define what URL to send our request to, and set out client_id and logout_uri parameters.
//     */
//    @Override
//    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        return UriComponentsBuilder
//                .fromUri(URI.create(domain + "/logout"))
//                .queryParam("client_id", userPoolClientId)
//                .queryParam("logout_uri", logoutRedirectUrl)
//                .encode(StandardCharsets.UTF_8)
//                .build()
//                .toUriString();
//    }
}