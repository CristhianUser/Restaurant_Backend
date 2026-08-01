package com.codigo.ms_seguridad.controller.Admin;

import com.codigo.ms_seguridad.aggregates.response.DataResponse;
import com.codigo.ms_seguridad.entity.Usuario;
import com.codigo.ms_seguridad.service.UsuariosService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class UserController {

    private final UsuariosService usuariosService;

    @GetMapping("/users")
    private List<DataResponse> listaUsers(@RequestParam("data") String datos){
        return usuariosService.dataUsers(datos);
    }

    @GetMapping("/user/{email}")
    private DataResponse userData(@PathVariable String email){
        return usuariosService.User(email);
    }

    @DeleteMapping("/user/{email}")
    private void deleteUser(@PathVariable String email){
        usuariosService.deleteUser(email);
    }

}
