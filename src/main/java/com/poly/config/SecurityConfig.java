package com.poly.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import com.poly.entity.Account;
import com.poly.entity.AccountRole;
import com.poly.entity.Role;
import com.poly.repository.AccountRepository;
import com.poly.repository.RoleRepository;
import com.poly.service.SessionService;


@Configuration
@EnableWebSecurity
  
public class SecurityConfig {

    @Autowired
    AccountRepository accountDAO;
    
    @Autowired
    RoleRepository roleDAO;
    
    @Autowired
    SessionService session;
   

    @Bean
    BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

            	Account account = accountDAO.findByUsername(username) != null ? accountDAO.findByUsername(username) : null; 	
            	 
                	if(account == null) {
                		throw new UsernameNotFoundException("User not found");
                	}
                    String userRole1 = account.getUsername();
                    List<String> userRole2 = account.getAuthorities().stream()
                            .map(au -> String.valueOf(au.getRole().getRoleName())) // Chuyển đổi thành số nguyên
                            .collect(Collectors.toList());
                  
                    
                    
                  
                    System.out.println(account.getAccountId());
                    //session.set("account", account.getAccountId());
                    
               
            		
                    GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userRole2);
                    return new User(account.getUsername(), getPasswordEncoder().encode(account.getPassword()), Collections.singletonList(authority));
                }
       
                
            };
        };
    

    @Bean
    @Order(1)
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.cors().disable().csrf().disable();
        http.securityMatcher("/admin/**","/index/**","/processinglogin","/dangnhap/success","/dangnhap/error","/dangnhap","/denied",
        		"/oauth2/authorization","/oauth2/authorization/google","/rest/**","/cart/**",
				"/login/oauth2/code/google","/oauth2/authorization/facebook","/login/oauth2/code/facebook")
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("[Admin]")
						.requestMatchers(new AntPathRequestMatcher("/rest/products/**")).hasAnyRole("[User]", "[Admin]", "[Admin, User]")
						.requestMatchers(new AntPathRequestMatcher("/cart/**")).hasAnyRole("[User]", "[Admin]", "[Admin, User]")
						.requestMatchers(
								new AntPathRequestMatcher("/dangnhap/**"),
								new AntPathRequestMatcher("/index/**")
						)
						
						.permitAll())
						
                .formLogin(
                		login -> {
                    try {
                        login.loginPage("/auth/dangnhap/form")
                                .loginProcessingUrl("/processinglogin")
                                .defaultSuccessUrl("/auth/dangnhap/success", true)
                                .failureUrl("/auth/dangnhap/error")
                                .and()
                                .exceptionHandling(exception -> exception.accessDeniedPage("/auth/dangnhap/denied"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
        )
                
             

                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/signin")
//                        .defaultSuccessUrl("/social/success", true)
                        .failureUrl("/auth/dangnhap/error"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index"));
        return http.build();
    }
    
    
    
    
    

	    @Bean
	     ClientRegistrationRepository clientRegistrationRepository() {
	        List<ClientRegistration> registrations = new ArrayList();
	        registrations.add(this.googleClientRegistration());
//	        registrations.add(this.facebookClientRegistration());
	        return new InMemoryClientRegistrationRepository(registrations);
	    }
	
	    private ClientRegistration googleClientRegistration() {
	        return ClientRegistration.withRegistrationId("google")
	                .clientId("912415040329-i1akj1clt9kp2blrusvn6k8r7fosksgd.apps.googleusercontent.com")
	                .clientSecret("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")
	                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
	                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
	                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
	                .scope("openid", "profile", "email", "address", "phone")
	                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
	                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
	                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
	                .userNameAttributeName(IdTokenClaimNames.SUB)
	                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
	                .clientName("Google")
	                .build();
	    }
	    
	
//	    private ClientRegistration facebookClientRegistration() {
//	        return ClientRegistration.withRegistrationId("facebook")
//	                .clientId("776354867561051")
//	                .clientSecret("43b42a6f3c032746ce26915df52397d6")
//	                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//	                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//	                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
//	                .scope("email", "public_profile")
//	                .authorizationUri("https://www.facebook.com/v12.0/dialog/oauth")
//	                .tokenUri("https://graph.facebook.com/v12.0/oauth/access_token")
//	                .userInfoUri("https://graph.facebook.com/v12.0/me?fields=id,name,email")
//	                .userNameAttributeName("id")
//	                .clientName("Facebook")
//	                .build();
//	    }
}