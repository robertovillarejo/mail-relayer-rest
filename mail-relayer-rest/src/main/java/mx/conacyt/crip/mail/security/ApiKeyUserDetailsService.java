package mx.conacyt.crip.mail.security;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import mx.conacyt.crip.mail.application.port.in.GetUserBySecretKeyQuery;
import mx.conacyt.crip.mail.domain.SecretKey;

public class ApiKeyUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private static final Logger logger = LoggerFactory.getLogger(ApiKeyUserDetailsService.class);

    private final GetUserBySecretKeyQuery getUserBySecretKeyQuery;

    public ApiKeyUserDetailsService(GetUserBySecretKeyQuery getUserBySecretKeyQuery) {
        this.getUserBySecretKeyQuery = getUserBySecretKeyQuery;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) {
        String authorizationHeader = token.getCredentials().toString();
        logger.info("Loading user for X-API-KEY header: {} ", authorizationHeader);
        return loadUserDetails(authorizationHeader);
    }

    private UserDetails loadUserDetails(String apiKey) {
        mx.conacyt.crip.mail.domain.User user = getUserBySecretKeyQuery.getUserBySecretKey(SecretKey.of(apiKey))
                .orElseThrow(() -> new UsernameNotFoundException("Api key not valid"));
        return new User(user.getName(), apiKey, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
