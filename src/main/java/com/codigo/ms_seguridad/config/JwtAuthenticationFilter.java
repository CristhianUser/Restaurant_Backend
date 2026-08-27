package com.codigo.ms_seguridad.config;

import com.codigo.ms_seguridad.service.JwtService;
import com.codigo.ms_seguridad.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioService usuarioService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String tokenExtraidoHeader = request.getHeader("Authorization");
        //Bearer apis-token-11190.XuZgRhBonrjpjA5JmQdGWN5mlOfUhujN

        final String tokenLimpio;
        //apis-token-11190.XuZgRhBonrjpjA5JmQdGWN5mlOfUhujN
        final String userEmail;

        if(!StringUtils.hasText(tokenExtraidoHeader)
                || !StringUtils.startsWithIgnoreCase(tokenExtraidoHeader,"Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        //Limpiando nuestro token
        tokenLimpio = tokenExtraidoHeader.substring(7);
        userEmail = jwtService.extractUserName(tokenLimpio);

        if(Objects.nonNull(userEmail) &&
                SecurityContextHolder.getContext().getAuthentication() == null){

            //Definir un contexto de seguridad vacio;
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

            //Recuperamos los datos del usuario de la BD usando UserDetails de SS
            UserDetails userDetails = usuarioService.userDetailsService().loadUserByUsername(userEmail);

            //Validación del token
            if(jwtService.validateToken(tokenLimpio, userDetails)
               && !jwtService.isRefreshToken(tokenLimpio)){

                //Objeto que representa al usuario autenticado
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null, userDetails.getAuthorities());

                //Agregamos ldetalles adicionales del reqeuest sobre el objeto de autenticación
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Establecemos el contexto de seguridad creado anteriormente como vacio
                securityContext.setAuthentication(authenticationToken);

                //Pasando el contexto creado al global
                SecurityContextHolder.setContext(securityContext);
            }

        }

        System.out.println("DEBUG SECURITY - Usuario: " + userEmail + " | Autoridades en Contexto: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        //Continuar con el resto de filtros
        filterChain.doFilter(request,response);
    }
}
