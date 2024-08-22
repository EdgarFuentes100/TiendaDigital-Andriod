package com.example.myappstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myappstore.Utils.AuthManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private AuthManager authManager;
    private BottomSheetBehavior<FrameLayout> bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authManager = new AuthManager(this);

        Button signInButton = findViewById(R.id.bttIngresar);
        signInButton.setOnClickListener(v -> authManager.signIn(RC_SIGN_IN));

        // Configurar el BottomSheet
        FrameLayout bottomSheet = findViewById(R.id.bottom_sheet_container);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        // Configurar la altura mínima visible del BottomSheet (por ejemplo, 100dp)
        bottomSheetBehavior.setPeekHeight(dpToPx(100));

        // Configurar si el BottomSheet puede ocultarse completamente
        bottomSheetBehavior.setHideable(true);

        // Opcional: Configurar el estado inicial del BottomSheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authManager.handleSignInResult(requestCode, resultCode, data);
    }

    // Convertir dp a píxeles
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
