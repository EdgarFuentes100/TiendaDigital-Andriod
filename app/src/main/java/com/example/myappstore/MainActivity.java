package com.example.myappstore;
import android.view.Gravity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import com.example.myappstore.Utils.AuthManager;
import com.example.myappstore.Utils.MainActivityHelper;

public class MainActivity extends AppCompatActivity {

    private AuthManager authManager;
    private TextView textViewName, textViewEmail, myTextView, textViewCantidad;
    private Button bttLogout;
    private ImageView imageViewMenu, imageUser;
    private MainActivityHelper helper;
    private FrameLayout frameContainer;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_content);

        authManager = new AuthManager(this);
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewCantidad = findViewById(R.id.textViewCantidad);
        imageViewMenu = findViewById(R.id.Compra);
        imageUser = findViewById(R.id.foto);
        bttLogout = findViewById(R.id.bttLogout);
        frameContainer = findViewById(R.id.frameContainer); // Ensure this ID is in your layout
        myTextView = findViewById(R.id.idInformacion); // Asumiendo que tienes un TextView con este ID
        // Get the FragmentManager from the Activity
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Initialize the helper with the UI components and FragmentManager
        helper = new MainActivityHelper(this, textViewName, textViewEmail, textViewCantidad, imageViewMenu, imageUser, fragmentManager);

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
    public void closeDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START); // Cierra el drawer desde el lado izquierdo
        }
    }
    public void updateTextCantidad(String text) {
        if (textViewCantidad != null) {
            textViewCantidad.setText(text);
        }
    }
}

