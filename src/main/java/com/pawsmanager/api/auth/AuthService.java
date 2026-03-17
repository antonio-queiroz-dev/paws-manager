package com.pawsmanager.api.auth;

import com.pawsmanager.api.user.User;
import com.pawsmanager.api.user.UserRole;
import com.pawsmanager.api.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(AuthRequest request) {
        // verifica se email já está cadastrado
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        // cria um usuário com os dados da requisição email e senha encriptada
        User user = new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                UserRole.USER
        );

        // salva no banco de dados
        userRepository.save(user);

        //gera e retorna o token
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);

    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // busca o objeto no banco de dados
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // gera e retorna o token
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}