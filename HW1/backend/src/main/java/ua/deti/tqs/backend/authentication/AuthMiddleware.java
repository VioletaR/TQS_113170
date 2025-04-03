package ua.deti.tqs.backend.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.deti.tqs.backend.authentication.utils.CustomUserDetails;
import ua.deti.tqs.backend.entities.User;
import ua.deti.tqs.backend.services.interfaces.UserService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Component
@AllArgsConstructor
public class AuthMiddleware extends OncePerRequestFilter {

    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            String base64Credentials = authorizationHeader.substring(6);
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
            String[] parts = credentials.split(":", 2);
            String name = parts[0];
            String password = parts[1];

            User found = userService.loginUser(name, password);
            if (found == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
                return;
            }
            CustomUserDetails userDetails = new CustomUserDetails(found.getId(), found.getRole(), found.getUsername(), "");
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
