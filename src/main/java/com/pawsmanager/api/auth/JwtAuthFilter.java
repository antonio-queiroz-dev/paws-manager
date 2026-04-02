package com.pawsmanager.api.auth;

import com.pawsmanager.api.user.User;
import com.pawsmanager.api.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
    }
        // remove o prefixo "Bearer "
        String token = authHeader.substring(7);

        try {
            //extrai o email
            String email = jwtService.extractUsername(token);

            // Verifica se o email foi extraído do token
            // Verifica se o usuário está autenticado
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // busca o usuário no banco de dados
                User user = userRepository.findByEmail(email).orElse(null);

                // verifica validade e expiração do token
                if (user != null && jwtService.isTokenValid(token, user)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    user.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // registra o usuário autenticado no SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // token inválido ou expirado continua sem autenticar
            // Spring Security retornará 401 via authenticationEntryPoint
        }

        filterChain.doFilter(request, response);
    }
}
