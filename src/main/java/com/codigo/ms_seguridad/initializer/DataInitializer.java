package com.codigo.ms_seguridad.initializer;

import com.codigo.ms_seguridad.entity.Categoria;
import com.codigo.ms_seguridad.entity.Category;
import com.codigo.ms_seguridad.entity.Rol;
import com.codigo.ms_seguridad.entity.Role;
import com.codigo.ms_seguridad.repository.CategoriaRespository;
import com.codigo.ms_seguridad.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final CategoriaRespository categoriaRespository;

    @Override
    public void run(String... args) throws Exception {
        if(rolRepository.count() == 0){
            Rol rol1 = new Rol();
            Rol rol2 = new Rol();
            Rol rol3 = new Rol();
            Rol rol4 = new Rol();
            rol1.setNombreRol(Role.SUPER_ADMIN.name());
            rol2.setNombreRol(Role.GESTOR_RESTAURANTE.name());
            rol3.setNombreRol(Role.GESTOR_VENTAS.name());
            rol4.setNombreRol(Role.CLIENTE.name());
            Set<Rol> roles = new HashSet<>();
            roles.add(rol1);
            roles.add(rol2);
            roles.add(rol3);
            roles.add(rol4);
            rolRepository.saveAll(roles);
            System.out.println("ROLES INICIALIZADOS DE MANERA CORRECTA");
        }else {
            System.out.println("LOS ROLES YA EXISTEN");
        }

        if(categoriaRespository.count() == 0){
            Categoria cat1 = new Categoria();
            Categoria cat2 = new Categoria();
            Categoria cat3 = new Categoria();
            Categoria cat4 = new Categoria();
            cat1.setNombreCategoria(Category.ENTRADA.name());
            cat2.setNombreCategoria(Category.BEBIDA.name());
            cat3.setNombreCategoria(Category.PIQUEOS.name());
            cat4.setNombreCategoria(Category.MENU.name());
            Set<Categoria> categorias = new HashSet<>();
            categorias.add(cat1);
            categorias.add(cat2);
            categorias.add(cat3);
            categorias.add(cat4);
            categoriaRespository.saveAll(categorias);
            System.out.println("CATEGORIAS INICIALIZADAS CORRECTAMENTE");
        } else{
            System.out.println("LAS CATEGORIAS YA EXISTEN");
        }

    }
}
