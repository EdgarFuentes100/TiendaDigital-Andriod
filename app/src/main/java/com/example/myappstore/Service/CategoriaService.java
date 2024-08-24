package com.example.myappstore.Service;

import android.util.Log;

import com.example.myappstore.CLS.Categoria;
import com.example.myappstore.CLS.Producto;
import com.example.myappstore.Https.ApiCliente;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.Interface.ICategoria;
import com.example.myappstore.Interface.IProducto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriaService {

    private final ICategoria categoriaApi;

    public CategoriaService() {
        categoriaApi = ApiCliente.getRetrofitInstance().create(ICategoria.class);
    }
    public void obtenerTodoCategoria(final CallBackApi<Categoria> callback) {
        Call<List<Categoria>> call = categoriaApi.obtenerTodoCategoria();
        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful()) {
                    List<Categoria> categorias = response.body();
                    callback.onResponseList(categorias);
                } else {
                    // Aquí puedes manejar el caso en que la respuesta no sea exitosa
                    callback.onFailure("Error en la respuesta del servidor: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                callback.onFailure("Error en conexión de red: " + t.getMessage());
            }
        });
    }
}
