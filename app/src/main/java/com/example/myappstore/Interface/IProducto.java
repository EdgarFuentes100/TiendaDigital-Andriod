package com.example.myappstore.Interface;
import com.example.myappstore.CLS.Producto;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IProducto {
    @GET("appStore/v1/productos/products")
    Call<List<Producto>> obtenerProductoPorId(@Query("categoria_id") String categoriaId);
    @GET("appStore/v1/productos")
    Call<List<Producto>> obtenerTodoProducto();
    @GET("appStore/v1/productos/productos")
    Call<List<Producto>> obtenerTodoProductoSinImagen();
    @POST("appStore/v1/productos/insertarProducto")
    Call<Producto> insertarProducto(@Body Producto producto);

    @PUT("appStore/v1/productos/actualizarProducto/{idProducto}")
    Call<Producto> actualizarProducto(@Path("idProducto") int idProducto, @Body Producto producto);
}
