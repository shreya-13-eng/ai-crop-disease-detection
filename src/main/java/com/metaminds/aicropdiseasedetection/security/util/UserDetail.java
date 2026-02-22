package com.metaminds.aicropdiseasedetection.security.util;

import com.metaminds.aicropdiseasedetection.security.models.User;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class UserDetail implements UserDetails {
    private final User user;

    public UserDetail(User user) {
        this.user = user;
    }

    @Override
    public @Nonnull  Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public @Nonnull String getUsername() {
        return user.getEmail();
    }

    public String getFullName (){return user.getFullName();}
}
