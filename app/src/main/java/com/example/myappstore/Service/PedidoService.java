package com.example.myappstore.Service;
import com.example.myappstore.CLS.Pedido;
import com.example.myappstore.CLS.Producto;
import com.example.myappstore.Https.ApiCliente;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.Interface.IPedido;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidoService {
    private final IPedido pedidoApi;

    public PedidoService() {
        pedidoApi = ApiCliente.getRetrofitInstance().create(IPedido.class);
    }
    public void obtenerPedidosUsuario(String email, final CallBackApi<Pedido> callback) {
        Call<List<Pedido>> call = pedidoApi.obtenerPedidoUsuario(email);
        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful()) {
                    List<Pedido> pedidos = response.body();
                    callback.onResponseList(pedidos);
                } else {
                    // Aquí puedes manejar el caso en que la respuesta no sea exitosa
                    callback.onFailure("Error en la respuesta del servidor: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                callback.onFailure("Error en conexión de red: " + t.getMessage());
            }
        });
    }

    public void obtenerHistorialsuario(String email, final CallBackApi<Pedido> callback) {
        Call<List<Pedido>> call = pedidoApi.obtenerHistorialUsuario(email);
        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful()) {
                    List<Pedido> pedidos = response.body();
                    callback.onResponseList(pedidos);
                } else {
                    // Aquí puedes manejar el caso en que la respuesta no sea exitosa
                    callback.onFailure("Error en la respuesta del servidor: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                callback.onFailure("Error en conexión de red: " + t.getMessage());
            }
        });
    }

    public void insertarPedido(Pedido pedido, final CallBackApi<Pedido> callback) {
        Call<Pedido> call = pedidoApi.insertarPedido(pedido);
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful()) {
                    Pedido pedido = response.body();
                    callback.onResponse(pedido);
                } else {
                    callback.onFailure("Error en la respuesta del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                callback.onFailure("Error en conexión de red: " + t.getMessage());
            }
        });
    }
}
