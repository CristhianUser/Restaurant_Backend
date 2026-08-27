package com.codigo.ms_seguridad.controller.Admin;

import com.codigo.ms_seguridad.aggregates.request.SedeRequest;
import com.codigo.ms_seguridad.aggregates.response.SedeResponse;
import com.codigo.ms_seguridad.service.SedeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/admin")
@RestController
@RequiredArgsConstructor
public class SedeController {

    public final SedeService sedeService;

    @GetMapping("/sedes")
    private List<SedeResponse> listSedes(@RequestParam("filtro") String filtro){
        return sedeService.listSedeResponses(filtro);
    }

}
