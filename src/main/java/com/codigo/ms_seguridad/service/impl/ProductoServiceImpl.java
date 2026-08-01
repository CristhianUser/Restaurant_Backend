package com.codigo.ms_seguridad.service.impl;

import com.codigo.ms_seguridad.aggregates.request.ProductMasterRequest;
import com.codigo.ms_seguridad.aggregates.response.ProductMasterResponse;
import com.codigo.ms_seguridad.entity.Categoria;
import com.codigo.ms_seguridad.entity.ProductoMaster;
import com.codigo.ms_seguridad.repository.CategoriaRespository;
import com.codigo.ms_seguridad.repository.ProductoRepository;
import com.codigo.ms_seguridad.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRespository categoriaRespository;

    @Value("${storage.local.path}")
    private String carpetaImg; //C:
    @Value("${storage.local.url}") //localhost
    private String urlImg;

    @Override
    public ProductMasterResponse createProducto(ProductMasterRequest productMasterRequest) {
        ProductoMaster productoMapeado = mapEntityByRequest(productMasterRequest);
        productoRepository.save(productoMapeado);
        return mapResponseByEntity(productoMapeado);
    }

    @Override
    public ProductMasterResponse findByIdProducto(Long id) {
        return buscarProductoById(id);
    }

    @Override
    public ProductMasterResponse findByCodigoProducto(String codigo) {
        return buscarProductoByCodigo(codigo);
    }

    @Override
    public ProductMasterResponse updateByIdProducto(String codigo, ProductMasterRequest productMasterRequest) {
        ProductoMaster productoActualizar = productoRepository.findByCodigo(codigo);
        productoActualizar.setNombre(productoActualizar.getCodigo());
        if(!productMasterRequest.getNombreCategoria().isEmpty() || productMasterRequest.getNombreCategoria().trim()!= null) {
            Categoria nuevaCategoria = categoriaRespository.findByNombreCategoriaContainingIgnoreCase(productMasterRequest.getNombreCategoria());
            Categoria antiguaCategoria = productoActualizar.getCategoria();
            antiguaCategoria.getProductoMasters().remove(productoActualizar);
            nuevaCategoria.añadirProductoListaCategorias(productoActualizar);
        }

        if(!productMasterRequest.getFoto().isEmpty() || productMasterRequest.getFoto() != null ){
            if(!productoActualizar.getFoto().isEmpty() || productoActualizar.getFoto() != null){
                try {
                    String rutaArchivoAntiguo = productoActualizar.getFoto();
                    String nombreImagenAntigua = rutaArchivoAntiguo.substring(rutaArchivoAntiguo.lastIndexOf("/"+1));
                    Path rutaArchivo = Paths.get(carpetaImg + nombreImagenAntigua);
                    Files.deleteIfExists(rutaArchivo);
                    System.out.println("La imagen antigua fue eliminada");
                } catch (Exception e) {
                    System.out.println("No se pudo eliminar la imagen antigua: "+e.getMessage());
                }
            }
        }
        productoActualizar.setFoto(getStringByFile(productMasterRequest.getFoto()));
        productoRepository.save(productoActualizar);
        return mapResponseByEntity(productoActualizar);
    }

    @Override
    public List<ProductMasterResponse> listProductos(String nombreProducto) {
        List<ProductoMaster> productoMasterList;
        List<ProductMasterResponse> productMasterResponseList = new ArrayList<>();
        Boolean inputVacio = nombreProducto == null || nombreProducto.trim().isEmpty();

        productoMasterList = inputVacio ? productoRepository.findAll() : productoRepository.findByNombreContainingIgnoreCase(nombreProducto);

        for (ProductoMaster productoMaster:productoMasterList){
            ProductMasterResponse productMasterResponse = mapResponseByEntity(productoMaster);
            productMasterResponseList.add(productMasterResponse);
        }

        return productMasterResponseList;
    }

    @Override
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    Categoria buscarCategoriaById(Long id){
        Categoria categoriaById = categoriaRespository.findById(id).orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        return categoriaById;
    }

    ProductMasterResponse buscarProductoById(Long id){
        ProductoMaster productoBuscadoxId = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encontro el producto con el ID :"+id));
        return mapResponseByEntity(productoBuscadoxId);
    }

    ProductMasterResponse buscarProductoByCodigo(String codigo){
        ProductoMaster productoBuscadoxCodigo = productoRepository.findByCodigo(codigo);
        return mapResponseByEntity(productoBuscadoxCodigo);
    }

    ProductoMaster mapEntityByRequest(ProductMasterRequest productMasterRequest) {
        try {
            ProductoMaster productoMaster = new ProductoMaster();

            // 1. Validar categoría
            if (productMasterRequest.getNombreCategoria() == null || productMasterRequest.getNombreCategoria().trim().isEmpty()) {
                throw new IllegalArgumentException("La categoría en el request no puede estar vacía.");
            }

            Categoria categoriaSeleccionada = categoriaRespository.findByNombreCategoriaContainingIgnoreCase(productMasterRequest.getNombreCategoria());
            if (categoriaSeleccionada == null) {
                throw new IllegalArgumentException("La categoría '" + productMasterRequest.getNombreCategoria() + "' no existe en el sistema.");
            }

            // 2. Validar foto antes de procesar
            if (productMasterRequest.getFoto() == null || productMasterRequest.getFoto().isEmpty()) {
                throw new IllegalArgumentException("El archivo de imagen no fue enviado o está vacío.");
            }

            productoMaster.setNombre(productMasterRequest.getNombre());
            productoMaster.setFoto(getStringByFile(productMasterRequest.getFoto()));

            // Establecer la relación en ambos sentidos
            productoMaster.setCategoria(categoriaSeleccionada);
            categoriaSeleccionada.añadirProductoListaCategorias(productoMaster);

            return productoMaster;
        } catch (IllegalArgumentException e) {
            // Capturar errores de validación lógica nuestros
            throw new RuntimeException("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            // Esto imprimirá el error real (ya sea de Base de Datos, de Archivos, de IO, etc.) en tu terminal
            e.printStackTrace();
            throw new RuntimeException("No se pudo completar la transaccion de datos debido a un error interno: " + e.getMessage());
        }
    }

    ProductMasterResponse mapResponseByEntity(ProductoMaster productoMaster){
        ProductMasterResponse productMasterResponse = new ProductMasterResponse();
        productMasterResponse.setNombre(productoMaster.getNombre());
        productMasterResponse.setCodigo(productoMaster.getCodigo());
        productMasterResponse.setFoto(productoMaster.getFoto());
        productMasterResponse.setCategoria(productoMaster.getCategoria().getNombreCategoria());
        return productMasterResponse;
    }

    String getStringByFile(MultipartFile imagen){
        try {
            String nombreCompeltoImg = imagen.getOriginalFilename();
            String extension = nombreCompeltoImg.substring(nombreCompeltoImg.lastIndexOf("."));
            String nombreUnico = UUID.randomUUID().toString() + extension;
            Path baseDir = Paths.get(carpetaImg);
            Path rutaEnviar = baseDir.resolve(nombreUnico);
            Files.copy(imagen.getInputStream(),rutaEnviar, StandardCopyOption.REPLACE_EXISTING);
            return nombreUnico;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
