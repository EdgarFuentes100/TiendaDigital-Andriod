package com.example.myappstore.Activities.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myappstore.MainActivity;
import com.example.myappstore.R;

public class FrHistorialProducto  extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_historial_productos, container, false);
        ajustarTexto();
        return view;
    }
    private void ajustarTexto(){
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateTextView("Historial", false);
        }
    }
}
