package com.example.myappstore.Utils;
import  com.example.myappstore.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import com.example.myappstore.CLS.Usuario;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.Service.UsuarioService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.List;

import retrofit2.Response;

public class AuthManager {

    private GoogleSignInClient googleSignInClient;
    private Context context;

    public AuthManager(Context context) {
        this.context = context;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.server_client_id)) // Asegúrate de que este ID sea correcto
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public void signIn(int requestCode) {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        if (context instanceof LoginActivity) {
            ((LoginActivity) context).startActivityForResult(signInIntent, requestCode);
        }
    }

    public void handleSignInResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null && account.getIdToken() != null && !account.getIdToken().isEmpty()) {
                    // Guardar información del usuario en SharedPreferences
                    SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", account.getDisplayName());
                    editor.putString("email", account.getEmail());
                    editor.putString("id", account.getId());
                    editor.putString("token", account.getIdToken());
                    editor.putString("aut", account.getServerAuthCode());
                    Uri photoUrl = account.getPhotoUrl();
                    if (photoUrl != null) {
                        editor.putString("photoUrl", photoUrl.toString());
                    }
                    editor.apply();
                    obtenerUsuarioRol(account.getEmail());
                } else {
                    // Token está vacío o nulo
                    Toast.makeText(context, "Sign in failed: Token is empty", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                // Manejar el error de autenticación
                Toast.makeText(context, "Sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void obtenerUsuarioRol(String email) {
        UsuarioService us = new UsuarioService();
        us.obtenerUsuariosPorEmail(email, new CallBackApi<Usuario>() {
            @Override
            public void onResponse(Usuario response) {
                // Este método no se usa actualmente
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {
                // Manejo de respuesta de la inserción
            }

            @Override
            public void onResponseList(List<Usuario> response) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (response != null && !response.isEmpty()) {
                    // Obtener el primer usuario de la lista
                    Usuario usuario = response.get(0);
                    editor.putString("rol", usuario.getRol()); // Suponiendo que `getRol()` devuelve el rol del usuario
                    int idUsuario = usuario.getIdUsuario();
                    editor.putString("idUsuario", String.valueOf(idUsuario));
                    editor.apply();
                    proceedToNextActivity();
                } else {
                    // Usuario no existe, inserción del nuevo usuario
                    Usuario usuario = new Usuario();
                    usuario.setCorreo(email);
                    usuario.setNombre(sharedPreferences.getString("name", "N/A"));
                    usuario.setIdRol(2); // Establecer un rol por defecto si es necesario
                    insertarUsuario(usuario);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(context, "Authentication Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertarUsuario(Usuario usuario) {
        UsuarioService us = new UsuarioService();
        us.insertarUsuario(usuario, new CallBackApi<Usuario>() {
            @Override
            public void onResponse(Usuario response) {
                if (response != null) {
                    int idUsuario = response.getIdUsuario();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("idUsuario", String.valueOf(idUsuario));
                    editor.putString("rol", usuario.getRol());
                    editor.apply();
                    proceedToNextActivity();
                } else {
                    // Manejar el caso cuando la respuesta es null
                    Toast.makeText(context, "Respuesta vacía del servidor.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onResponseBool(Response<Boolean> response) {

            }

            @Override
            public void onResponseList(List<Usuario> response) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    public void signOut() {
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            // Eliminar datos de SharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();  // Borra todos los datos
            editor.apply();

            // Mostrar mensaje de cierre de sesión
            Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show();

            // Redirigir a la pantalla de inicio de sesión
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            if (context instanceof MainActivity) {
                ((MainActivity) context).finish();
            }
        });
    }
    private void proceedToNextActivity() {
        // Acción a realizar después de insertar o encontrar el usuario.
        Toast.makeText(context, "Sign in successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        if (context instanceof LoginActivity) {
            ((LoginActivity) context).finish();
        }
    }
}
