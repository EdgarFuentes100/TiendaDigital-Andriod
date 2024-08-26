package com.example.myappstore.Service;
import com.example.myappstore.CLS.Pedido;
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
}
