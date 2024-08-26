package com.example.myappstore.Interface;
import com.example.myappstore.CLS.Usuario;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IUsuario {
    @GET("appStore/v1/usuarios")
    Call<List<Usuario>> obtenerUsuario();
}
