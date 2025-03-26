package com.poliqlo.controllers.common.auth.service;

import com.poliqlo.models.TaiKhoan;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    public Optional<String> getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((TaiKhoan) authentication.getPrincipal()).getEmail().describeConstable();
        }
        return Optional.empty();
    }
    public Optional<Integer> getAccountId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((TaiKhoan) authentication.getPrincipal()).getId().describeConstable();
        }
        return Optional.empty();
    }

    public Optional<TaiKhoan> getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            try {
                var taiKhoan=(TaiKhoan)authentication.getPrincipal();
                return Optional.of(taiKhoan);
            } catch (ClassCastException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal());
    }
}
