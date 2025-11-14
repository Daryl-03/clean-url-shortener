package dev.richryl.identity.adapters.filter;

import dev.richryl.identity.application.ports.in.CreateUserUseCase;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class EnsureUserProvisioningFilter extends OncePerRequestFilter {

    private final CreateUserUseCase createUserUseCase;

    public EnsureUserProvisioningFilter(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String id = authentication.getName();
            createUserUseCase.handle(id);
        }
        filterChain.doFilter(request, response);
    }
}
