package com.example.myappstore.Activities.Fragment;
import com.example.myappstore.Utils.AlertDialogBase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myappstore.R;
public class FrListaProductos extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lista_productos, container, false);

        ImageView imageView = view.findViewById(R.id.idElementoProducto);
        imageView.setOnClickListener(v -> showCustomDialog());

        return view;
    }

    private void showCustomDialog() {
        AlertDialogBase dialogFragment = new AlertDialogBase(R.layout.activity_crud_productos, false);
        dialogFragment.show(getParentFragmentManager(), "customDialog");
    }
}

