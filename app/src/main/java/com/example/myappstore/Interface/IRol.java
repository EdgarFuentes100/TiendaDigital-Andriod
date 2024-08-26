package com.example.myappstore.Interface;
import com.example.myappstore.CLS.Rol;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IRol {
    @GET("appStore/v1/roles")
    Call<List<Rol>> obtenerRol();
}
