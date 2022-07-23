package com.yaphet.springboottemplate.utilities;

import com.yaphet.springboottemplate.models.AppUser;
import com.yaphet.springboottemplate.models.Privilege;
import com.yaphet.springboottemplate.models.Role;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@EqualsAndHashCode
public class AppUserDetails implements UserDetails {

    private final AppUser appUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = appUser.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for(Role role : roles){
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            for(Privilege privilege : role.getPrivileges()){
                authorities.add(new SimpleGrantedAuthority(privilege.getPrivilegeName()));
            }
        }
        return authorities;
    }

    public Long getId(){
        return appUser.getId();
    }

    @Override
    public String getPassword() {
        return appUser.getPassword();
    }

    @Override
    public String getUsername() {
        return appUser.getEmail();
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
        return appUser.getEnabled();
    }
}
