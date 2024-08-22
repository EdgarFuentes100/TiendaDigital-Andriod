package com.example.myappstore.Activities.Fragment;

import com.example.myappstore.MainActivity;
import com.example.myappstore.R;
import com.example.myappstore.Utils.FragmentTransactionHelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ClienteFragment extends Fragment {

    private FragmentTransactionHelper fragmentTransactionHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frame_cliente, container, false);

        // Inicializa el FragmentTransactionHelper
        FragmentManager fragmentManager = getParentFragmentManager(); // O usa requireActivity().getSupportFragmentManager()
        fragmentTransactionHelper = new FragmentTransactionHelper(fragmentManager);

        // Configura el botón para abrir el fragmento
        Button openFragmentButton = view.findViewById(R.id.open_fragment_button);
        openFragmentButton.setOnClickListener(v -> openDetailFragment());
        ajustarTexto();

        return view;
    }

    private void openDetailFragment() {
        Fragment fragment = new FrClienteProducto(); // Usa el fragmento simple para pruebas
        fragmentTransactionHelper.replaceFragment(fragment, true);
    }
    private void ajustarTexto(){
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateTextView("Categorias", false);
        }
    }
}
