package com.codigo.ms_seguridad.service.impl;

import com.codigo.ms_seguridad.aggregates.request.SedeRequest;
import com.codigo.ms_seguridad.aggregates.response.RestauranteResponse;
import com.codigo.ms_seguridad.aggregates.response.SedeResponse;
import com.codigo.ms_seguridad.entity.Restaurante;
import com.codigo.ms_seguridad.entity.Sede;
import com.codigo.ms_seguridad.repository.SedeRepository;
import com.codigo.ms_seguridad.service.SedeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SedeServiceImpl implements SedeService {

    private final SedeRepository sedeRepository;

    @Value("${storage.local.path}")
    private String rutaGuardar;
    @Value("${storage.local.url}")
    private String urlBuscar;

    @Override
    public SedeResponse createSede(SedeRequest sedeRequest) {
        Sede sedeCreada = mapSedeToRequest(sedeRequest);
        sedeRepository.save(sedeCreada);
        return mapResponseToSed(sedeCreada);
    }

    @Override
    public SedeResponse findByCodigo(String codigo) {
        Sede sedeBuscar = sedeRepository.findByCodigoUnico(codigo).orElseThrow(() -> new RuntimeException("El codigo no coincide con ninguna de la sedes"));
        return mapResponseToSed(sedeBuscar);
    }

    @Override
    public List<SedeResponse> listSedes(String nombreSede) {
        List<Sede> sedes;
        List<SedeResponse> sedeResponseList = new ArrayList<>();
        if (nombreSede.trim() != null || !nombreSede.trim().isEmpty()){
            sedes = sedeRepository.findByNombreContainingIgnoreCase(nombreSede);
        }else {
            sedes = sedeRepository.findAll();
        }

        for (Sede sede:sedes){
            SedeResponse sedeResponse = mapResponseToSed(sede);
            sedeResponseList.add(sedeResponse);
        }
        return sedeResponseList;
    }

    Sede mapSedeToRequest(SedeRequest sedeRequest){
        Sede sedeMapeada = new Sede();
        sedeMapeada.setDistrito(sedeRequest.getDistrito());
        sedeMapeada.setUbicacion(sedeRequest.getUbicacion());
        sedeMapeada.setFotoReferencia(mapStringtoFile(sedeRequest.getReferencia()));
        sedeMapeada.setNombre(nombreSede(sedeRequest.getDistrito(), sedeMapeada.getCodigoUnico()));
        return sedeMapeada;
    }

    SedeResponse mapResponseToSed(Sede sede){
        SedeResponse mapResponse = new SedeResponse();
        mapResponse.setDistrito(sede.getDistrito());
        mapResponse.setCodigo(sede.getCodigoUnico());
        mapResponse.setUbicacion(sede.getUbicacion());
        mapResponse.setFoto(sede.getFotoReferencia());
        mapResponse.setRestauranteResponseSet(listaResponse(sede.getRestaurantes()));
        return mapResponse;
    }

    public Set<RestauranteResponse> listaResponse(Set<Restaurante> restaurantes){
        Set<RestauranteResponse> restauranteResponseList = new HashSet<>();
        for (Restaurante restaurante:restaurantes){
            RestauranteResponse restauranteResponse = new RestauranteResponse();
            restauranteResponse.setCodigo(restaurante.getCodigo());
            restauranteResponse.setNombreRestaurante(restaurante.getNombreUnico());
            restauranteResponse.setUbicacion(restaurante.getUbicacion());
            restauranteResponse.setUbicacionUrl(restaurante.getUbicacionURL());
            restauranteResponse.setImagenReferencia(restaurante.getFoto());
            restauranteResponseList.add(restauranteResponse);
        }
        return restauranteResponseList;
    }

    String mapStringtoFile(MultipartFile imagen){
        try {
            String imagenOriginal = imagen.getOriginalFilename();
            String extension = imagenOriginal.substring(imagenOriginal.lastIndexOf("."));
            String nombreUnicoImg = UUID.randomUUID().toString() + extension;
            Path rutaEnviar = Paths.get(rutaGuardar + nombreUnicoImg);
            String rutaEncontrar = urlBuscar + nombreUnicoImg;
            Files.copy(imagen.getInputStream(), rutaEnviar, StandardCopyOption.REPLACE_EXISTING);
            return rutaEncontrar;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    String nombreSede(String distrito, String codigoUnico){
        String nombreConcatenado = distrito + " " + codigoUnico;
        return nombreConcatenado;
    }

}
