package com.example.myappstore.Interface;
import com.example.myappstore.CLS.Pedido;
import com.example.myappstore.CLS.Producto;
import com.example.myappstore.CLS.Usuario;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IUsuario {
    @GET("appStore/v1/usuarios")
    Call<List<Usuario>> obtenerUsuario();
    @GET("appStore/v1/usuarios/activos")
    Call<List<Usuario>> obtenerUsuarioEmail(@Query("correo") String email);
    @POST("appStore/v1/usuarios/insertarUsuario")
    Call<Usuario> insertarUsuario(@Body Usuario usuario);
    @PUT("appStore/v1/usuarios/actualizarUsuario/{idUsuario}")
    Call<Usuario> actualizarUsuario(@Path("idUsuario") int idUsuario, @Body Usuario usuario);
}
