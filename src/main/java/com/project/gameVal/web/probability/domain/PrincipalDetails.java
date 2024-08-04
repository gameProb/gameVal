package com.project.gameVal.web.probability.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class PrincipalDetails extends GameCompany implements UserDetails {

    private final GameCompany gameCompany;
    private final Map<String, Object> userAttributes;

    public PrincipalDetails(GameCompany gameCompany) {
        this.gameCompany = gameCompany;
        this.userAttributes = new HashMap<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(gameCompany.getRole().name()));

        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return gameCompany.getName();
    }

    @Override
    public String getPassword() {
        return gameCompany.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
