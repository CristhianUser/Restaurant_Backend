package com.codigo.ms_seguridad.service.impl;

import com.codigo.ms_seguridad.aggregates.request.RestauranteRequest;
import com.codigo.ms_seguridad.aggregates.response.ProductMenuResponse;
import com.codigo.ms_seguridad.aggregates.response.RestauranteResponse;
import com.codigo.ms_seguridad.entity.MenuProducto;
import com.codigo.ms_seguridad.entity.Restaurante;
import com.codigo.ms_seguridad.entity.Sede;
import com.codigo.ms_seguridad.repository.RestauranteRepository;
import com.codigo.ms_seguridad.repository.SedeRepository;
import com.codigo.ms_seguridad.service.RestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RestauranteServiceImpl implements RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final SedeRepository sedeRepository;

    @Value("${storage.local.path}")
    private String rutaGuardar;
    @Value("${storage.local.url}")
    private String rutaURL;

    @Override
    public RestauranteResponse createRestaurante(String codigoSede, RestauranteRequest restauranteRequest) {
        Sede sedeAsociadaAlRestaurante = buscarSedeByCodigoUnico(codigoSede);
        Restaurante crearRestaurante = mapEntityRestauranteByRequest(restauranteRequest);
        String nombreRestaurante = sedeAsociadaAlRestaurante.getDistrito() + crearRestaurante.getUbicacion();
        crearRestaurante.setNombreUnico(nombreRestaurante);
        sedeAsociadaAlRestaurante.AsociarRestaurante(crearRestaurante);
        return mapResponseByEntityRestaurante(crearRestaurante);
    }

    @Override
    public List<RestauranteResponse> listRestaurantes(String nombreRestaurante) {
        List<Restaurante> restauranteList;
        List<RestauranteResponse> restauranteResponseList = new ArrayList<>();
        if (!nombreRestaurante.trim().isEmpty() || nombreRestaurante != null){
            restauranteList = restauranteRepository.findByNombreUnicoContainingIgnoreCase(nombreRestaurante);
        }else {
            restauranteList = restauranteRepository.findAll();
        }

        for (Restaurante restaurante:restauranteList){
            RestauranteResponse restauranteResponse = mapResponseByEntityRestaurante(restaurante);
            restauranteResponseList.add(restauranteResponse);
        }

        return restauranteResponseList;
    }

    @Override
    public RestauranteResponse findByNombreResturante(String nombreRestaurante) {
        Restaurante buscarRestauranteByNombre = restauranteRepository.findByNombreUnico(nombreRestaurante).orElseThrow(() -> new UsernameNotFoundException("No existe restaurante registrado con ese nombre"));
        return mapResponseByEntityRestaurante(buscarRestauranteByNombre);
    }

    @Override
    public RestauranteResponse updateRestaurante(String nombreRestaurante, RestauranteRequest restauranteRequest) {
        Restaurante restauranteActualizar = restauranteRepository.findByNombreUnico(nombreRestaurante).orElseThrow(() -> new UsernameNotFoundException("No se encontro un restaurante con ese nombre"));
        restauranteActualizar.setUbicacion(restauranteRequest.getUbicacion());
        restauranteActualizar.setUbicacionURL(restauranteRequest.getUbicacionUrl());
        restauranteActualizar.setFoto(mapStringByFile(restauranteRequest.getFotoRestaurante()));
        restauranteRepository.save(restauranteActualizar);
        return mapResponseByEntityRestaurante(restauranteActualizar);
    }

    @Override
    public void DeleteByCodigoUnicoDelRestaurante(String codigoUnicoRestaurante) {
        Restaurante restauranteBuscar = restauranteRepository.findByCodigo(codigoUnicoRestaurante);
        Sede sedeRelacionada = restauranteBuscar.getSede();
        sedeRelacionada.getRestaurantes().remove(restauranteBuscar);
        restauranteRepository.deleteByCodigo(codigoUnicoRestaurante);
    }

    public Sede buscarSedeByCodigoUnico(String codigoSede){
        Sede consultarSede = sedeRepository.findByCodigoUnico(codigoSede).orElseThrow(() -> new UsernameNotFoundException("No se encontro una sede registrada con ese codigo"));
        return consultarSede;
    }

    public Restaurante mapEntityRestauranteByRequest(RestauranteRequest restauranteRequest){
        Restaurante restauranteMapeado = new Restaurante();
        restauranteMapeado.setUbicacion(restauranteRequest.getUbicacion());
        restauranteMapeado.setUbicacionURL(restauranteRequest.getUbicacionUrl());
        restauranteMapeado.setFoto(mapStringByFile(restauranteRequest.getFotoRestaurante()));
        return restauranteMapeado;
    }

    public RestauranteResponse mapResponseByEntityRestaurante(Restaurante restaurante){
        RestauranteResponse restauranteResponse = new RestauranteResponse();
        restauranteResponse.setCodigo(restaurante.getCodigo());
        restauranteResponse.setNombreRestaurante(restaurante.getNombreUnico());
        restauranteResponse.setUbicacion(restaurante.getUbicacion());
        restauranteResponse.setUbicacionUrl(restaurante.getUbicacionURL());
        restauranteResponse.setImagenReferencia(restaurante.getFoto());
        restauranteResponse.setProductMenuResponseSet(productMenuResponseSet(restaurante.getMenuProductos()));
        return restauranteResponse;
    }

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

    public Set<ProductMenuResponse> productMenuResponseSet(Set<MenuProducto> menuProductos){
        Set<ProductMenuResponse> productMenuResponseSet = new HashSet<>();
        for (MenuProducto menuProducto:menuProductos){
            ProductMenuResponse productMenuResponse = new ProductMenuResponse();
            productMenuResponse.setId(menuProducto.getId());
            productMenuResponse.setCategoria(menuProducto.getCategoria());
            productMenuResponse.setNombre(menuProducto.getProducto().getNombre());
            productMenuResponse.setDescripcion(menuProducto.getDescripcion());
            productMenuResponse.setPrecioBase(menuProducto.getPrecioBase());
            productMenuResponse.setDescuento(menuProducto.getDescuento());
            productMenuResponse.setPrecioFinal(menuProducto.getPrecioFinal());
            productMenuResponse.setFoto(menuProducto.getFoto());
            productMenuResponseSet.add(productMenuResponse);
        }
        return productMenuResponseSet;
    }

}
