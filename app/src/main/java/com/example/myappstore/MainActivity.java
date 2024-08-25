package com.example.myappstore;
import android.util.Log;
import android.view.Gravity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myappstore.CLS.Producto;
import com.example.myappstore.Https.ApiCliente12;
import com.example.myappstore.Https.CallBackApi;
import com.example.myappstore.Service.ProductoService;
import com.example.myappstore.Utils.AuthManager;
import com.example.myappstore.Utils.MainActivityHelper;

import java.util.List;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AuthManager authManager;
    private TextView textViewName, textViewEmail, myTextView;
    private Button bttLogout;
    private ImageView imageViewMenu, imageUser;
    private MainActivityHelper helper;
    private FrameLayout frameContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authManager = new AuthManager(this);
        new ApiCliente12().execute();

        Toast.makeText(this, "Main Started", Toast.LENGTH_SHORT).show(); // Verifica si esta línea se ejecuta
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        imageViewMenu = findViewById(R.id.Compra);
        imageUser = findViewById(R.id.foto);
        bttLogout = findViewById(R.id.bttLogout);
        frameContainer = findViewById(R.id.frameContainer); // Ensure this ID is in your layout
        myTextView = findViewById(R.id.idInformacion); // Asumiendo que tienes un TextView con este ID
        // Get the FragmentManager from the Activity
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Initialize the helper with the UI components and FragmentManager
        helper = new MainActivityHelper(this, textViewName, textViewEmail, imageViewMenu, imageUser, fragmentManager);

        // Use the helper to display user data
        helper.displayUserData();

        // Set up the PopupMenu using the helper
        helper.setupPopupMenu();

        // Set up the logout button
        bttLogout.setOnClickListener(view -> authManager.signOut());

        // Load the appropriate fragment
        helper.loadAppropriateFragment();
    }

    // Método para actualizar el TextView
    public void updateTextView(String text, boolean center) {
        if (myTextView != null) {
            myTextView.setText(text);
            if (center){
                myTextView.setGravity(Gravity.CENTER);
            }
        }
    }
}

