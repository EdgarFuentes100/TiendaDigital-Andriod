package com.example.myappstore.Interface;

import com.example.myappstore.CLS.Categoria;
import com.example.myappstore.CLS.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ICategoria {
    @GET("appStore/v1/categorias")
    Call<List<Categoria>> obtenerTodoCategoria();
    @POST("appStore/v1/categorias/insertarCategoria")
    Call<Categoria> insertarCategoria(@Body Categoria categoria);

    @PUT("appStore/v1/categorias/actualizarCategoria/{idCategoria}")
    Call<Categoria> actualizarCategoria(@Path("idCategoria") int idCategoria, @Body Categoria categoria);
}
