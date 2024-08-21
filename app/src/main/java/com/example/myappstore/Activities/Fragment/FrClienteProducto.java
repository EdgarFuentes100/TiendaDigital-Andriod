package com.example.myappstore.Activities.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myappstore.Utils.AlertDialogBase;
import com.example.myappstore.R;

public class FrClienteProducto extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frame_cliente_productos, container, false);

        GridLayout gridLayout = view.findViewById(R.id.grid_layout);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View itemView = gridLayout.getChildAt(i);
            if (itemView instanceof LinearLayout) {
                itemView.setOnClickListener(v -> showCustomDialog());
            }
        }

        return view;
    }

    private void showCustomDialog() {
        AlertDialogBase dialogFragment = new AlertDialogBase(R.layout.dialog_producto, true);
        dialogFragment.show(getParentFragmentManager(), "customDialog");
    }
}
