package com.codigo.ms_seguridad.aggregates.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class CategoryResponse {
    private Long id;
    private String nombreCategoria;
    private Set<ProductMasterResponse> productMasterResponseSet = new HashSet<>();
}
