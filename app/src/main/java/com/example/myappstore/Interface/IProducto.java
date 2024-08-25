package com.example.myappstore.Interface;
import com.example.myappstore.CLS.Producto;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IProducto {
    @GET("appStore/v1/productos/products")
    Call<List<Producto>> obtenerProductoPorId(@Query("categoria_id") String categoriaId);
    @GET("appStore/v1/productos")
    Call<List<Producto>> obtenerTodoProducto();
}
