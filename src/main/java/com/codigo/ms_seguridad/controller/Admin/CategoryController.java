package com.codigo.ms_seguridad.controller.Admin;

import com.codigo.ms_seguridad.aggregates.response.CategoryResponse;
import com.codigo.ms_seguridad.entity.Categoria;
import com.codigo.ms_seguridad.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoriaService categoriaService;

    @GetMapping("/all-categorias")
    private List<CategoryResponse> listaCategoria(){
        return categoriaService.allCategorias();
    }

    @PostMapping("/create-categoria")
    private Categoria createCategoria(@RequestParam("nuevaCategoria") String textCategoria){
        return categoriaService.createCategoria(textCategoria);
    }
}
