package com.example.myappstore.Activities.Fragment;
import com.example.myappstore.MainActivity;
import com.example.myappstore.R;
import com.example.myappstore.Utils.FragmentTransactionHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AdminFragment extends Fragment {

    private FragmentTransactionHelper fragmentTransactionHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frame_admin, container, false);

        // Inicializa el FragmentTransactionHelper
        FragmentManager fragmentManager = getParentFragmentManager(); // O usa requireActivity().getSupportFragmentManager()
        fragmentTransactionHelper = new FragmentTransactionHelper(fragmentManager);

        LinearLayout linearLayout = view.findViewById(R.id.IdCrudCategoria);
        LinearLayout linearLayout1 = view.findViewById(R.id.IdCrudProducto);
        LinearLayout linearLayout2 = view.findViewById(R.id.IdCrudUsuario);

        linearLayout.setOnClickListener(v -> openDetailFragment(new FrListaCategoria()));
        linearLayout1.setOnClickListener(v -> openDetailFragment(new FrListaProductos()));
        linearLayout2.setOnClickListener(v -> openDetailFragment(new FrListaUsuarios()));
        ajustarTexto();

        return view;
    }

    private void openDetailFragment(Fragment fragment) {
        fragmentTransactionHelper.replaceFragment(fragment, true);
    }
    private void ajustarTexto(){
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateTextView("Informacion", true);
        }
    }
}
