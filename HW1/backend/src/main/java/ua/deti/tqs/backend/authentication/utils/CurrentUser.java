package ua.deti.tqs.backend.authentication.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ua.deti.tqs.backend.entities.utils.UserRole;

@Slf4j
@Component
public class CurrentUser {

    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            log.info("Authenticated user ID: {}", userDetails.getId());
            return userDetails.getId();
        }

        return null;
    }

    public UserRole getAuthenticatedUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            log.info("Authenticated user role: {}", userDetails.getUserRole());
            return userDetails.getUserRole();
        }

        return null;
    }
}