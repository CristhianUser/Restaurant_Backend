package com.codigo.ms_seguridad.service.impl;

import com.codigo.ms_seguridad.aggregates.request.ProductMenuRequest;
import com.codigo.ms_seguridad.aggregates.response.ProductMenuResponse;
import com.codigo.ms_seguridad.entity.MenuProducto;
import com.codigo.ms_seguridad.entity.ProductoMaster;
import com.codigo.ms_seguridad.entity.Restaurante;
import com.codigo.ms_seguridad.entity.Usuario;
import com.codigo.ms_seguridad.repository.MenuProductoRepository;
import com.codigo.ms_seguridad.repository.ProductoRepository;
import com.codigo.ms_seguridad.repository.RestauranteRepository;
import com.codigo.ms_seguridad.repository.UsuarioRepository;
import com.codigo.ms_seguridad.service.MenuProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuProductoServiceImpl implements MenuProductoService {

    private final ProductoRepository productoRepository;
    private final MenuProductoRepository menuProductoRepository;
    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public ProductMenuResponse agregarMenu(String codigoProductoCatalogo, ProductMenuRequest productMenuRequest) {
        ProductoMaster productoCatalogo = productoRepository.findByCodigo(codigoProductoCatalogo);
        MenuProducto menuProducto = new MenuProducto();
        menuProducto.vincularConProductoCatalogo(productoCatalogo);
        Restaurante restauranteByUsuario = restauranteByEmailUsuario();
        restauranteByUsuario.agregarAlMenu(menuProducto);
        menuProducto.setPrecioBase(productMenuRequest.getPrecioBase());
        menuProducto.setDescuento(productMenuRequest.getDescuento());
        menuProducto.setPrecioFinal(calculoPrecioFinal(productMenuRequest.getPrecioBase(), productMenuRequest.getDescuento()));
        menuProducto.setDescripcion(productMenuRequest.getDescripcion());
        menuProductoRepository.save(menuProducto);
        return getResponseByEntity(menuProducto);
    }

    @Override
    public ProductMenuResponse actualizarMenu(String codigoMenu) {
        return null;
    }

    @Override
    public ProductMenuResponse verProducto(String codigoProductoMenu) {
        return findByIdCodigo(codigoProductoMenu);
    }

    @Override
    public void eliminarPlatoDelMenu(String codigoProductoMenu) {
        MenuProducto menuProducto = menuProductoRepository.findById(codigoProductoMenu).orElseThrow(() -> new UsernameNotFoundException("No se logro eliminar el producto"));
        Restaurante restauranteVinculado = menuProducto.getRestaurante();
        restauranteVinculado.getMenuProductos().remove(menuProducto);
        menuProductoRepository.deleteById(codigoProductoMenu);
    }

    public Restaurante restauranteByEmailUsuario(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null || email.equals("anonymousUser") || email.trim().isEmpty()){
            throw new RuntimeException("Error: El usuario no se encuentra autenticado en el sistema.");
        }
        Usuario usuarioVinculadoAlRestaurante = usuarioRepository.findByEmail(email).orElseThrow();
        return usuarioVinculadoAlRestaurante.getRestauranteByUsuario();
    }

    ProductMenuResponse getResponseByEntity(MenuProducto menuProducto){
        ProductMenuResponse productMenuResponse = new ProductMenuResponse();
        productMenuResponse.setId(menuProducto.getId());
        productMenuResponse.setNombre(menuProducto.getProducto().getNombre());
        productMenuResponse.setCategoria(menuProducto.getCategoria());
        productMenuResponse.setPrecioBase(menuProducto.getPrecioBase());
        return productMenuResponse;
    }

    Double calculoPrecioFinal(Double precioBase, Double descuento){
        Double precioMostrar = descuento != null ? precioBase * (descuento/100) : precioBase;
        return precioMostrar;
    }

    ProductMenuResponse findByIdCodigo(String codigo){
        MenuProducto menuProducto = menuProductoRepository.findById(codigo).orElseThrow(() -> new UsernameNotFoundException("No se encontro con ese ID en el menu"));
        return getResponseByEntity(menuProducto);
    }

}
