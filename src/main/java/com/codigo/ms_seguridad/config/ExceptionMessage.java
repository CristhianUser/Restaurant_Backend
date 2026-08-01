package com.codigo.ms_seguridad.config;

public class ExceptionMessage extends RuntimeException{
    public ExceptionMessage(String mensaje){
        super(mensaje);
    }
}
