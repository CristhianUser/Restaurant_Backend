package com.codigo.ms_seguridad.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                //Las 3 barras (file:///) para evitar errores de acceso directo
                .addResourceLocations("file:///C:/restaurante_uploads/");
    }
}
