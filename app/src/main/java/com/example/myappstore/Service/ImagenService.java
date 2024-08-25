package com.example.myappstore.Service;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.example.myappstore.CLS.Imagen;
import com.example.myappstore.Https.ApiCliente;
import com.example.myappstore.Https.OnImageLoadListener;
import com.example.myappstore.Interface.IImagen;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagenService {
    private final IImagen imagenApi;

    public ImagenService() {
        imagenApi = ApiCliente.getRetrofitInstance().create(IImagen.class);
    }

    public void obtenerImagenes(int idProducto, final OnImageLoadListener listener) {
        Call<List<Imagen>> call = imagenApi.ObtenerLista(idProducto);
        call.enqueue(new Callback<List<Imagen>>() {
            @Override
            public void onResponse(Call<List<Imagen>> call, Response<List<Imagen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Imagen> imagenes = response.body();
                    List<Bitmap> bitmaps = new ArrayList<>();

                    // Descargar y decodificar cada imagen en la lista
                    for (Imagen imagen : imagenes) {
                        String base64Image = imagen.getData(); // Obtener la cadena Base64

                        // Decodificar la imagen Base64 en un Bitmap
                        try {
                            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            bitmaps.add(bitmap);

                            // Notificar al listener si todas las imágenes se han descargado
                            if (bitmaps.size() == imagenes.size()) {
                                listener.onImagesLoad(bitmaps);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onImagesLoadError("Error al decodificar la imagen: " + e.getMessage());
                        }
                    }
                } else {
                    listener.onImagesLoadError("Error en la respuesta del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Imagen>> call, Throwable t) {
                listener.onImagesLoadError("Error en conexión de red: " + t.getMessage());
            }
        });
    }
}
