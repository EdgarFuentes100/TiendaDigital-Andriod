package com.example.myappstore.Utils;
import  com.example.myappstore.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

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

                    // Mostrar mensaje de éxito y redirigir al usuario
                    Toast.makeText(context, "Sign in successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    if (context instanceof LoginActivity) {
                        ((LoginActivity) context).finish();
                    }
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
}
