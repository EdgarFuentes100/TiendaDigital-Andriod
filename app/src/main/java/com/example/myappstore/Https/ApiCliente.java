package com.example.myappstore.Https;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiCliente {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.211.52:8011/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit;
        }
        return retrofit;
    }
}
