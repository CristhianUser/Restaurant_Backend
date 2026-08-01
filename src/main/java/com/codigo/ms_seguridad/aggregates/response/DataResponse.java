package com.codigo.ms_seguridad.aggregates.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class DataResponse {
    private String nombres;
    private String email;
    private List<String> roles = new ArrayList<>();
}
