package com.example.myappstore.Https;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiCliente extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... voids) {
        String result = "";
        try {
            URL url = new URL("http://192.168.211.52:8010/cursos/v1/usuarios/Rol/1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            reader.close();
        } catch (Exception e) {
            Log.e("ApiClient", "Error al conectar con la API", e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // Muestra el resultado en la consola de Logcat
        Log.d("ApiClient", "Resultado de la API: " + result);
    }
}
