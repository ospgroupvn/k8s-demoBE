package com.osp.bttp.common.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * TODO: write you class description here
 *
 * @author
 */

public class TokenBasedAuthentication extends AbstractAuthenticationToken {

    private String token;
    private final UserDetails principle;
    private boolean authenticated = false;

    public TokenBasedAuthentication(UserDetails principle ) {
        super( principle.getAuthorities() );
        this.principle = principle;
    }
    
    public TokenBasedAuthentication(UserDetails principle, boolean authenticated ) {
        super( principle.getAuthorities() );
        this.principle = principle;
        this.authenticated = authenticated;
    }


    public String getToken() {
        return this.token;
    }

    public void setToken( String token ) {
        this.token = token;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	@Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public UserDetails getPrincipal() {
        return this.principle;
    }

}
