package com.example.myappstore.Interface;
import com.example.myappstore.CLS.Imagen;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IImagen {
    @GET("appStore/v1/imagenes/images-list")
    Call<List<Imagen>> ObtenerLista(@Query("idProducto") int idProducto);
}
