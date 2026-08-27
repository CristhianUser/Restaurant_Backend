package com.codigo.ms_seguridad.service.impl;

import com.codigo.ms_seguridad.aggregates.response.DataResponse;
import com.codigo.ms_seguridad.entity.Usuario;
import com.codigo.ms_seguridad.repository.UsuarioRepository;
import com.codigo.ms_seguridad.service.UsuariosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuariosServiceImpl implements UsuariosService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public List<DataResponse> dataUsers(String datos) {
        List<DataResponse> responseList = new ArrayList<>();
        List<Usuario> usuarios;

        if (datos.trim() != null || !datos.trim().isEmpty()){
            usuarios = usuarioRepository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrEmailContainingIgnoreCase(datos,datos,datos);
        }else{
            usuarios = usuarioRepository.findAll();
        }

        for (Usuario user:usuarios){
            DataResponse dataResponse = mapDataByEntity(user);
            responseList.add(dataResponse);
        }

        return responseList;
    }

    @Override
    public DataResponse User(String email) {
        Usuario user = usuarioRepository.findByEmail(email).orElseThrow();
        return mapDataByEntity(user);
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    DataResponse mapDataByEntity(Usuario usuario){
        DataResponse dataUser = DataResponse.builder()
                .nombres(usuario.getNombres()+" "+usuario.getApellidos())
                .email(usuario.getEmail())
                .roles(usuario.getRoles().stream().map(rol -> rol.getNombreRol()).toList())
                .build();
        return dataUser;
    }

}
