package com.codigo.ms_seguridad.service;

import com.codigo.ms_seguridad.aggregates.response.DataResponse;

import java.util.List;

public interface UsuariosService {
    List<DataResponse> dataUsers(String datos);
    DataResponse User(String email);
    void deleteUser(String email);
}
