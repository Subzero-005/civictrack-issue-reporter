package com.civictrack.issuetracker.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    public AppUserPrincipal get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (AppUserPrincipal) authentication.getPrincipal();
    }

    public Long id() {
        return get().getId();
    }
}
