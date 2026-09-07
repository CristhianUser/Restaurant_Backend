package com.codigo.ms_seguridad.service.impl;

import com.codigo.ms_seguridad.aggregates.constants.Constants;
import com.codigo.ms_seguridad.aggregates.request.RestauranteRequest;
import com.codigo.ms_seguridad.aggregates.response.DataResponse;
import com.codigo.ms_seguridad.aggregates.response.ProductMenuResponse;
import com.codigo.ms_seguridad.aggregates.response.RestauranteResponse;
import com.codigo.ms_seguridad.config.ExceptionMessage;
import com.codigo.ms_seguridad.entity.*;
import com.codigo.ms_seguridad.repository.RestauranteRepository;
import com.codigo.ms_seguridad.repository.RolRepository;
import com.codigo.ms_seguridad.repository.SedeRepository;
import com.codigo.ms_seguridad.repository.UsuarioRepository;
import com.codigo.ms_seguridad.service.RestauranteService;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestauranteServiceImpl implements RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;
    private final SedeRepository sedeRepository;
    private final RolRepository rolRepository;

    @Value("${storage.local.path}")
    private String rutaGuardar;
    @Value("${storage.local.url}")
    private String rutaURL;

    public String mapStringByFile(MultipartFile imagen){
        try {
            String originalFile = imagen.getOriginalFilename();
            String formato = originalFile.substring(originalFile.lastIndexOf("."));
            String nombreUnico = UUID.randomUUID().toString() + formato;
            Path rutaEnviar = Paths.get(rutaGuardar + nombreUnico);
            String rutaEncontrar = rutaURL + nombreUnico;
            Files.copy(imagen.getInputStream(), rutaEnviar, StandardCopyOption.REPLACE_EXISTING);
            return rutaEncontrar;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RestauranteResponse> listRestaurantes(String filtro) {
        boolean isSearch = filtro.trim().isEmpty();

        List<RestauranteResponse> responses = isSearch ?
                restauranteRepository.findAll().stream().map(this::getResponseRestaurante).toList() :
                restauranteRepository.findByNombreRestauranteContainingIgnoreCaseOrSedeDepartamentoContainingIgnoreCase(filtro, filtro)
                        .stream().map(this::getResponseRestaurante).toList();

        return responses;
    }

    @Override
    public RestauranteResponse getRestaurante() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioAutenticado = usuarioRepository.findByEmail(username).orElseThrow();
        Restaurante restaurante = usuarioAutenticado.getRestaurante();
        if(restaurante == null){
            throw new ExceptionMessage("El usuario no esta vinculado a ningun restaurante");
        }
        return getResponseRestaurante(restaurante);
    }

//    @Override
//    public RestauranteResponse getResponse(String codigoRestaurante) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Usuario usuario = usuarioRepository.findByEmail(username).orElseThrow();
//        Restaurante restaurante = restauranteRepository.findByCodigo(codigoRestaurante);
//        RestauranteResponse restauranteResponse;
//        if(usuario.getRestaurante() == null)){
//            throw new ExceptionMessage("El usuario no esta vinculado a ningun restaurante");
//        }else{
//            if (usuario.getRestaurante().equals(restaurante)){
//                restauranteResponse = getResponseRestaurante(restaurante);
//            }
//        }
//        return restauranteResponse;
//    }

    @Transactional
    @Override
    public void eliminarRestaurante(String codigoRestaurante) {
        restauranteRepository.deleteByCodigo(codigoRestaurante);
    }

    public RestauranteResponse getResponseRestaurante(Restaurante restaurante){
        RestauranteResponse newRestauranteResponse = new RestauranteResponse();
        newRestauranteResponse.setCodigo(restaurante.getCodigo());
        newRestauranteResponse.setSedeRestaurante(restaurante.getSede().getDepartamento());
        newRestauranteResponse.setRucRestaurante(restaurante.getRucRestaurante());
        newRestauranteResponse.setNombreRestaurante(restaurante.getNombreRestaurante());
        newRestauranteResponse.setFotoRestaurante(restaurante.getFoto());
        newRestauranteResponse.setUbicacionRestaurante(restaurante.getUbicacionRestaurante());
        newRestauranteResponse.setUsuariosReponsResponses(getListReponseUser(restaurante.getUsuarios()));
        newRestauranteResponse.setProductMenuResponses(getListResponse(restaurante.getMenuProductos()));
        return newRestauranteResponse;
    }

    public Set<ProductMenuResponse> getListResponse(Set<MenuProducto> productos){
        Set<ProductMenuResponse> responses = new HashSet<>();
        for (MenuProducto menu:productos){
            ProductMenuResponse productMenuResponse = getResponseProduct(menu);
            responses.add(productMenuResponse);
        }
        return responses;
    }

    public Set<DataResponse> getListReponseUser(Set<Usuario> usuarios){
        Set<DataResponse> responses = new HashSet<>();
        for (Usuario usu:usuarios){
            DataResponse dataUsuario = getResponseUsuario(usu);
            responses.add(dataUsuario);
        }
        return responses;
    }

    public ProductMenuResponse getResponseProduct(MenuProducto menuProducto){
        ProductMenuResponse productMenuResponse = new ProductMenuResponse();
        productMenuResponse.setNombre(menuProducto.getProducto().getNombre());
        productMenuResponse.setCategoria(menuProducto.getCategoria());
        productMenuResponse.setFoto(menuProducto.getFoto());
        productMenuResponse.setDescripcion(menuProducto.getDescripcion());
        productMenuResponse.setPrecioBase(menuProducto.getPrecioBase());
        productMenuResponse.setDescuento(menuProducto.getDescuento());
        productMenuResponse.setPrecioFinal(menuProducto.getPrecioFinal());
        return productMenuResponse;
    }

    public DataResponse getResponseUsuario(Usuario usuario){
        return DataResponse.builder()
                .email(usuario.getEmail())
                .nombres(usuario.getNombres()+' '+ usuario.getApellidos())
                .roles(usuario.getRoles().stream().map(rol -> rol.getNombreRol()).collect(Collectors.toList()))
                .build();
    }

}
