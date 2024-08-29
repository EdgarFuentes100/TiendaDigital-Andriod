package com.example.myappstore.Interface;

import com.example.myappstore.CLS.Categoria;
import com.example.myappstore.CLS.Pedido;
import com.example.myappstore.CLS.Producto;
import com.example.myappstore.CLS.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IPedido {
    @GET("appStore/v1/pedidos/activos")
    Call<List<Pedido>> obtenerPedidoUsuario(@Query("email") String email);
    @GET("appStore/v1/pedidos/pagados")
    Call<List<Pedido>> obtenerHistorialUsuario(@Query("email") String email);
    @POST("appStore/v1/pedidos/insertarPedido")
    Call<Pedido> insertarPedido(@Body Pedido pedido);
}
