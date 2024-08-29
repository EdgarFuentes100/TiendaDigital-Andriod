package com.example.myappstore.Service;

import com.example.myappstore.CLS.DetallePedido;
import com.example.myappstore.CLS.Usuario;
import com.example.myappstore.Https.ApiCliente;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.Interface.IDetallePedido;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallePedidoService {
    private final IDetallePedido detalleApi;

    public DetallePedidoService() {
        detalleApi = ApiCliente.getRetrofitInstance().create(IDetallePedido.class);
    }

    public void obtenerDetallePedido(String idPedido, final CallBackApi<Map<String, Object>> callback) {
        Call<List<Map<String, Object>>> call = detalleApi.obtenerDetallePedido(idPedido);
        call.enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful()) {
                    List<Map<String, Object>> mapList = response.body();

                    callback.onResponseList(mapList); // Pasar la lista de listas al callback
                } else {
                    callback.onFailure("Error en la respuesta del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                callback.onFailure("Error en conexión de red: " + t.getMessage());
            }
        });
    }

    public void insertarDetalle(DetallePedido detallePedido, final CallBackApi<DetallePedido> callback) {
        Call<DetallePedido> call = detalleApi.insertarDetalle(detallePedido); // Asume que `usuarioApi.insertarUsuario` es tu endpoint para insertar un usuario
        call.enqueue(new Callback<DetallePedido>() {
            @Override
            public void onResponse(Call<DetallePedido> call, Response<DetallePedido> response) {
                if (response.isSuccessful()) {
                    DetallePedido detalle = response.body();
                    callback.onResponse(detalle);
                } else {
                    callback.onFailure("Error en la respuesta del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<DetallePedido> call, Throwable t) {
                callback.onFailure("Error en conexión de red: " + t.getMessage());
            }
        });
    }

    public void obtenerDetalleSinFoto(String idPedido, final CallBackApi<DetallePedido> callback) {
        Call<List<DetallePedido>> call = detalleApi.obtenerDetalleSinFoto(idPedido);
        call.enqueue(new Callback<List<DetallePedido>>() {
            @Override
            public void onResponse(Call<List<DetallePedido>> call, Response<List<DetallePedido>> response) {
                if (response.isSuccessful()) {
                    List<DetallePedido> detallePedidos = response.body();
                    if (detallePedidos != null && !detallePedidos.isEmpty()) {
                        // Suponiendo que solo se espera un usuario con ese email
                        callback.onResponseList(detallePedidos);
                    } else {
                        callback.onResponseList(detallePedidos);
                    }
                } else {
                    // Aquí puedes manejar el caso en que la respuesta no sea exitosa
                    callback.onFailure("Error en la respuesta del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<DetallePedido>> call, Throwable t) {
                callback.onFailure("Error en conexión de red: " + t.getMessage());
            }
        });
    }

    public void actualizarCantidad(DetallePedido detallePedido, final CallBackApi<DetallePedido> callback) {
        Call<DetallePedido> call = detalleApi.actualizarCantidad(detallePedido); // Asume que `usuarioApi.insertarUsuario` es tu endpoint para insertar un usuario
        call.enqueue(new Callback<DetallePedido>() {
            @Override
            public void onResponse(Call<DetallePedido> call, Response<DetallePedido> response) {
                if (response.isSuccessful()) {
                    DetallePedido detalle = response.body();
                    callback.onResponse(detalle);
                } else {
                    callback.onFailure("Error en la respuesta del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<DetallePedido> call, Throwable t) {
                callback.onFailure("Error en conexión de red: " + t.getMessage());
            }
        });
    }
    public void eliminarDetalle(Integer idDetalle, final CallBackApi<Boolean> callback) {
        Call<Boolean> call = detalleApi.eliminarDetalle(idDetalle);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Boolean estado = response.body();
                    callback.onResponse(estado);
                } else {
                    callback.onFailure("Error en la respuesta del servidor: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                callback.onFailure("Error en conexión de red: " + t.getMessage());
            }
        });
    }
}
