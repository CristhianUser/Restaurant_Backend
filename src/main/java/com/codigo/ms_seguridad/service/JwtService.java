package com.codigo.ms_seguridad.service;

import com.codigo.ms_seguridad.entity.Usuario;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

    String extractUserName(String token);
    String generateToken(UserDetails userDetails, Usuario usuario);
    boolean validateToken(String token, UserDetails userDetails);

    String generateRefreshToken(Map<String, Object> extraClaims,
                                UserDetails userDetails);
    boolean isRefreshToken(String token);
}
