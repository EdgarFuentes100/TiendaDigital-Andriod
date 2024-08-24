package com.example.myappstore.Interface;
import com.example.myappstore.CLS.Producto;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IProducto {
    @GET("appStore/v1/producto/{id}")
    Call<List<Producto>> obtenerProductoPorId(@Path("id") String id);
    @GET("appStore/v1/productos")
    Call<List<Producto>> obtenerTodoProducto();
}
