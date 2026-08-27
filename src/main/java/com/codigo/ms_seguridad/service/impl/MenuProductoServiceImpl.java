package com.codigo.ms_seguridad.service.impl;

import com.codigo.ms_seguridad.aggregates.request.ProductMenuRequest;
import com.codigo.ms_seguridad.aggregates.response.ProductMenuResponse;
import com.codigo.ms_seguridad.config.ExceptionMessage;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuProductoServiceImpl implements MenuProductoService {

    private final ProductoRepository productoRepository;
    private final MenuProductoRepository menuProductoRepository;
    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<ProductMenuResponse> listProductMenuResponses(String producto, String categoria) {
        boolean isFilter = producto.trim() != null || categoria.trim() != null;
        List<ProductMenuResponse> menuResponses = isFilter ?
                menuProductoRepository.findByProducto_NombreContainingIgnoreCaseOrCategoriaContainingIgnoreCase(producto, categoria).stream().map(this::getMenuResponse).collect(Collectors.toList())
                : menuProductoRepository.findAll().stream().map(this::getMenuResponse).collect(Collectors.toList());

        return menuResponses;
    }

    @Override
    public ProductMenuResponse createProductMenu(ProductMenuRequest productMenuRequest) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(userName).orElseThrow();
        Restaurante restaurante = usuario.getRestaurante();

        MenuProducto menu = getMenuEntity(productMenuRequest);

        if(restaurante == null){
            throw new RuntimeException("No tiene el rol indicado para realizar esta transaccion");
        }

        menu.setRestaurante(restaurante);

        restauranteRepository.save(restaurante);
        return getMenuResponse(menu);
    }

    public MenuProducto getMenuEntity(ProductMenuRequest productMenuRequest){
        MenuProducto menu = new MenuProducto();
        ProductoMaster productoMaster = productoRepository.findByCodigo(productMenuRequest.getCodigo());

        if (productoMaster == null) {
            throw new ExceptionMessage("Error al añadir el producto a tu empresa: ");
        }

        menu.mapeoDatos(productoMaster);
        menu.setPrecioBase(productMenuRequest.getPrecioBase());
        menu.setDescuento(productMenuRequest.getDescuento());
        Double precioCalculado = productMenuRequest.getPrecioBase() - (productMenuRequest.getPrecioBase() * productMenuRequest.getDescuento());
        menu.setPrecioFinal(precioCalculado);
        return menu;
    }

    public ProductMenuResponse getMenuResponse(MenuProducto menuProducto){
        ProductMenuResponse menuResponse = new ProductMenuResponse();
        menuResponse.setId(menuProducto.getId());
        menuResponse.setNombre(menuProducto.getProducto().getNombre());
        menuResponse.setDescripcion(menuProducto.getDescripcion());
        menuResponse.setPrecioBase(menuProducto.getPrecioBase());
        menuResponse.setDescuento(menuProducto.getDescuento());
        menuResponse.setPrecioFinal(menuProducto.getPrecioFinal());
        return menuResponse;
    }

}
