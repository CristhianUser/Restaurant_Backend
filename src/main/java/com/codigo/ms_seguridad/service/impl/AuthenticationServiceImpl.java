package com.codigo.ms_seguridad.service.impl;

import com.codigo.ms_seguridad.aggregates.constants.Constants;
import com.codigo.ms_seguridad.aggregates.request.RestauranteMasterRequest;
import com.codigo.ms_seguridad.aggregates.request.RestauranteRequest;
import com.codigo.ms_seguridad.aggregates.request.SignInRequest;
import com.codigo.ms_seguridad.aggregates.request.SignUpRequest;
import com.codigo.ms_seguridad.aggregates.response.DataResponse;
import com.codigo.ms_seguridad.aggregates.response.SignInResponse;
import com.codigo.ms_seguridad.config.ExceptionMessage;
import com.codigo.ms_seguridad.entity.*;
import com.codigo.ms_seguridad.repository.RestauranteRepository;
import com.codigo.ms_seguridad.repository.RolRepository;
import com.codigo.ms_seguridad.repository.SedeRepository;
import com.codigo.ms_seguridad.repository.UsuarioRepository;
import com.codigo.ms_seguridad.service.AuthenticationService;
import com.codigo.ms_seguridad.service.JwtService;
import com.codigo.ms_seguridad.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final SedeRepository sedeRepository;
    private final RestauranteRepository restauranteRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @Override
    public Usuario signUpUser(SignUpRequest signUpRequest) {
        //validar el token x segunda vez,
        //abrir el token para extrer los permisos
        // validar que cuente con el scope de crear
        if(buscarUsuarioByEmail(signUpRequest.getEmail())){
            throw new ExceptionMessage("El email ya se encuentra registrado con otra cuenta");
        }

        Usuario usuario = getUsuarioEntity(signUpRequest);
        usuario.setRoles(Collections.singleton(getRoles(Role.CLIENTE)));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario signUpAdmin(SignUpRequest signUpRequest) {
        if(buscarUsuarioByEmail(signUpRequest.getEmail())){
            throw new ExceptionMessage("El email ya se encuentra registrado con otra cuenta");
        }
        Usuario usuario = getUsuarioEntity(signUpRequest);
        usuario.setRoles(Collections.singleton(getRoles(Role.SUPER_ADMIN)));
        //usuario.setRoles(Collections.singleton(getRoles(Role.ADMIN)));
        //usuario.setRoles(Collections.singleton(getRoles(Role.USER)));
        return usuarioRepository.save(usuario);
    }

    @Transactional
    @Override
    public void singUpGestorRestaurante(RestauranteMasterRequest restauranteMasterRequest) {

        boolean emailPresent = usuarioRepository.existsByEmail(restauranteMasterRequest.getUser().getEmail());

        boolean rucPresent = restauranteRepository.existsByRucRestaurante(restauranteMasterRequest.getRestaurante().getRucRestaurante());

        boolean sedePresent = sedeRepository.existsByDepartamento(restauranteMasterRequest.getRestaurante().getDepartamento());

        if (rucPresent || emailPresent){
            throw new ExceptionMessage("El ruc o el email ya se encuentra registrado !!!");
        }

        Sede sede;

        if(sedePresent){
            sede = sedeRepository.findByDepartamento(restauranteMasterRequest.getRestaurante().getDepartamento());
        }else {
            sede = getSede(restauranteMasterRequest.getRestaurante().getDepartamento());
        }

        Usuario usuario = getUsuarioEntity(restauranteMasterRequest.getUser());
        usuario.setRoles(Collections.singleton(getRoles(Role.GESTOR_RESTAURANTE)));
        Restaurante restaurante = getRestaurante(
                    restauranteMasterRequest.getRestaurante().getRucRestaurante(),
                    restauranteMasterRequest.getRestaurante().getNombreRestaurante(),
                    restauranteMasterRequest.getRestaurante().getUbicacionRestaurante(),
                    restauranteMasterRequest.getRestaurante().getDistrito()
                );
        sede.AsociarRestaurante(restaurante);
        restaurante.vincularUsuario(usuario);
        sedeRepository.save(sede);
    }

    @Override
    public Usuario singUpGestorVentas(SignUpRequest signUpRequest) {
        if(buscarUsuarioByEmail(signUpRequest.getEmail())){
            throw new ExceptionMessage("El email ya se encuentra registrado con otra cuenta");
        }
        Usuario usuario = getUsuarioEntity(signUpRequest);
        usuario.setRoles(Collections.singleton(getRoles(Role.GESTOR_VENTAS)));
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> todos() {
        return usuarioRepository.findAll();
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.getEmail(),signInRequest.getPassword()));

        var usuario = usuarioRepository.findByEmail(signInRequest.getEmail()).orElseThrow(
                () -> new UsernameNotFoundException("ERROR NO SE ENCONTRO AL USUARIO"));

        var token = jwtService.generateToken(usuario,usuario);
        var refresh = jwtService.generateRefreshToken(new HashMap<>(),usuario);

        DataResponse data = DataResponse.builder()
                .nombres(usuario.getNombres()+" "+usuario.getApellidos())
                .email(usuario.getUsername())
                .roles(usuario.getRoles().stream().map(Rol::getNombreRol).toList())
                .build();
        
        return SignInResponse.builder()
                .accessToken(token)
                .refreshToken(refresh)
                .dataResponse(data)
                .build();
    }

    @Override
    public SignInResponse getTokenByRefreshToken(String refreshToken) throws IllegalAccessException {
        //Validar que es unn refreshtoken
        if(!jwtService.isRefreshToken(refreshToken)){
            throw new RuntimeException("Eror el token ingresado no es un Refresh");
        }
        //extraer el usuario
        String userEMail = jwtService.extractUserName(refreshToken);

        //buscamos al usuario en BD
        Usuario usuario = usuarioRepository.findByEmail(userEMail).orElseThrow(
                () -> new UsernameNotFoundException("Error el usuario del token no se encontro en la BD"));

        //Obteniendo UserDetail para la generación del access
        UserDetails userDetails = usuarioService.userDetailsService().loadUserByUsername(usuario.getUsername());


        //Validar que el refresh le pertenece a un usuario y validar la exp
        if(!jwtService.validateToken(refreshToken,usuario)){
            throw new IllegalAccessException("Error el Refreshtoken no le pertenece al usuario o esta vencido");
        }

        //generar el access
        String newAccess = jwtService.generateToken(userDetails,usuario);
        List<String> roles = usuario.getRoles().stream().map(rol -> rol.getNombreRol()).collect(Collectors.toList());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", usuario.getId());
        claims.put("roles", roles);

        String newRefresh = jwtService.generateRefreshToken(claims,userDetails);
        DataResponse dataResponse = DataResponse.builder()
                .nombres(usuario.getNombres()+" "+usuario.getApellidos()).email(usuario.getEmail()).roles(roles).build();


        return SignInResponse.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .dataResponse(dataResponse)
                .build();
    }

    //GENERAR UN ACCESS TOKEN A PARTIR DE UN REFRESHTOKEN

    private Usuario getUsuarioEntity(SignUpRequest signUpRequest){
        return Usuario.builder()
                .nombres(signUpRequest.getNombres().trim())
                .apellidos(signUpRequest.getApellidos().trim())
                .email(signUpRequest.getEmail().trim())
                .password(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()))
                .numDoc(signUpRequest.getNumDoc())
                .isAccountNonExpired(Constants.STATUS_ACTIVE)
                .isAccountNonLocked(Constants.STATUS_ACTIVE)
                .isCredentialsNonExpired(Constants.STATUS_ACTIVE)
                .isEnabled(Constants.STATUS_ACTIVE)
                .build();
    }

    Boolean buscarUsuarioByEmail(String email){
        Optional<Usuario> UsuarioByEmail = usuarioRepository.findByEmail(email);
        boolean usuarioExiste = UsuarioByEmail.isPresent() ? true : false;
        return usuarioExiste;
    }

    private Rol getRoles(Role rolBuscado){
        return rolRepository.findByNombreRol(rolBuscado.name())
                .orElseThrow(() -> new RuntimeException("Error el rol no exixte: " + rolBuscado.name()));
    }

    public Restaurante getRestaurante(String rucRestaurante, String nombreRestaurante, String ubicacionRestaurante, String distritoRestaurante){
        Restaurante restauranteEntity = new Restaurante();
        restauranteEntity.setRucRestaurante(rucRestaurante);
        restauranteEntity.setNombreRestaurante(nombreRestaurante);
        restauranteEntity.setUbicacionRestaurante(ubicacionRestaurante);
        restauranteEntity.setDistrito(distritoRestaurante);
        return  restauranteEntity;
    }

    public Sede getSede(String departamento){
        Sede sedeEntity = new Sede();
        sedeEntity.setDepartamento(departamento);
        return sedeEntity;
    }

}
