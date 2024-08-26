package com.example.myappstore.Service;
import com.example.myappstore.CLS.Rol;
import com.example.myappstore.Https.ApiCliente;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.Interface.IRol;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RolService {
    private final IRol rolApi;

    public RolService() {
        rolApi = ApiCliente.getRetrofitInstance().create(IRol.class);
    }
    public void obtenerRol(final CallBackApi<Rol> callback) {
        Call<List<Rol>> call = rolApi.obtenerRol();
        call.enqueue(new Callback<List<Rol>>() {
            @Override
            public void onResponse(Call<List<Rol>> call, Response<List<Rol>> response) {
                if (response.isSuccessful()) {
                    List<Rol> resultado = response.body();
                    callback.onResponseList(resultado);
                } else {
                    // Aquí puedes manejar el caso en que la respuesta no sea exitosa
                    callback.onFailure("Error en la respuesta del servidor: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Rol>> call, Throwable t) {
                callback.onFailure("Error en conexión de red: " + t.getMessage());
            }
        });
    }
}
