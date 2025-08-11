package com.osp.bttp.common.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class UserExt extends org.springframework.security.core.userdetails.User {
      private static final long serialVersionUID = 1L;
      
      public UserExt(String username, String password, boolean enabled, boolean accountNonExpired,
                  boolean credentialsNonExpired, boolean accountNonLocked,
                  Collection<? extends GrantedAuthority> authorities) {
            super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
      }

      public UserExt(String username, String password, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
      }

      

      
}
