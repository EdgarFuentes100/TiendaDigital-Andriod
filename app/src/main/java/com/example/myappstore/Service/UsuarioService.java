package com.example.myappstore.Service;

import com.example.myappstore.CLS.Categoria;
import com.example.myappstore.CLS.Pedido;
import com.example.myappstore.CLS.Usuario;
import com.example.myappstore.Https.ApiCliente;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.Interface.ICategoria;
import com.example.myappstore.Interface.IUsuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioService {
    private final IUsuario usuarioApi;

    public UsuarioService() {
        usuarioApi = ApiCliente.getRetrofitInstance().create(IUsuario.class);
    }
    public void obtenerUsuarios(final CallBackApi<Usuario> callback) {
        Call<List<Usuario>> call = usuarioApi.obtenerUsuario();
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    List<Usuario> usuarios = response.body();
                    callback.onResponseList(usuarios);
                } else {
                    // Aquí puedes manejar el caso en que la respuesta no sea exitosa
                    callback.onFailure("Error en la respuesta del servidor: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                callback.onFailure("Error en conexión de red: " + t.getMessage());
            }
        });
    }
    public void obtenerUsuariosPorEmail(String email, final CallBackApi<Usuario> callback) {
        Call<List<Usuario>> call = usuarioApi.obtenerUsuarioEmail(email);
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    List<Usuario> usuarios = response.body();
                    if (usuarios != null && !usuarios.isEmpty()) {
                        // Suponiendo que solo se espera un usuario con ese email
                        callback.onResponseList(usuarios);
                    } else {
                        callback.onResponseList(usuarios);
                    }
                } else {
                    // Aquí puedes manejar el caso en que la respuesta no sea exitosa
                    callback.onFailure("Error en la respuesta del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                callback.onFailure("Error en conexión de red: " + t.getMessage());
            }
        });
    }
    public void insertarUsuario(Usuario usuario, final CallBackApi<Usuario> callback) {
        Call<Usuario> call = usuarioApi.insertarUsuario(usuario); // Asume que `usuarioApi.insertarUsuario` es tu endpoint para insertar un usuario
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Usuario usuario = response.body();
                    callback.onResponse(usuario);
                } else {
                    callback.onFailure("Error en la respuesta del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                callback.onFailure("Error en conexión de red: " + t.getMessage());
            }
        });
    }
}
