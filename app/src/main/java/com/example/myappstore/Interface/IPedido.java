package com.example.myappstore.Interface;

import com.example.myappstore.CLS.Categoria;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IPedido {
    @GET("appStore/v1/pedidos")
    Call<List<Categoria>> obtenerPedidos();
}
