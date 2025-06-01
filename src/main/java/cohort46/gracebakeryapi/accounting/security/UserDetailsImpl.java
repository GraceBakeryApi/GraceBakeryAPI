package cohort46.gracebakeryapi.accounting.security;

import cohort46.gracebakeryapi.accounting.model.RoleEnum;
import cohort46.gracebakeryapi.accounting.model.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserDetailsImpl implements UserDetails, Principal, OAuth2User {
    private UserAccount user;

    public UserDetailsImpl(UserAccount user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + (user.getRole().name())));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {    //time of account
        return UserDetails.super.isAccountNonExpired();
    }
    //int z = x<y? (x+y) : (x-y);
    @Override
    public boolean isAccountNonLocked() {
        return (!(user.getRole().equals(RoleEnum.BLOCKED)));
    }

    @Override
    public boolean isCredentialsNonExpired() {//time of password and/or another data
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public String getRole() {
        return user.getRole().name();
    }

    public Long getId() {
        return user.getId();
    }

    public UserAccount getUser() {
        return user;
    }

    @Override
    public String getName() {
        return user.getLogin();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }
}
