package com.example.myappstore.Interface;

import com.example.myappstore.CLS.Categoria;
import com.example.myappstore.CLS.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ICategoria {
    @GET("appStore/v1/categorias")
    Call<List<Categoria>> obtenerTodoCategoria();
}
