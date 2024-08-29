package com.example.myappstore.Interface;

import com.example.myappstore.CLS.DetallePedido;
import com.example.myappstore.CLS.Pedido;
import com.example.myappstore.CLS.Usuario;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IDetallePedido {
    @GET("appStore/v1/pedidodetalle/detalles")
    Call<List<Map<String, Object>>> obtenerDetallePedido(@Query("idPedido") String idPedido);
    @POST("appStore/v1/pedidodetalle/insertarDetalle")
    Call<DetallePedido> insertarDetalle(@Body DetallePedido detallePedido);
    @GET("appStore/v1/pedidodetalle/detallesSinImagen")
    Call<List<DetallePedido>> obtenerDetalleSinFoto(@Query("idPedido") String idPedido);
    @PUT("appStore/v1/pedidodetalle/actualizarCantidad")
    Call<DetallePedido> actualizarCantidad(@Body DetallePedido detallePedido);
    @DELETE("appStore/v1/pedidodetalle/eliminarDetalle")
    Call<Boolean> eliminarDetalle(@Query("idDetalle") Integer idDetalle);
}
