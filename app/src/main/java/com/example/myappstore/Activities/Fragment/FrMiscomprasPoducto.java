package com.example.myappstore.Activities.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.myappstore.MainActivity;
import com.example.myappstore.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FrMiscomprasPoducto extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ImageView imageViewLocation;
    private EditText editTextLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_miscompras_productos, container, false);

        imageViewLocation = view.findViewById(R.id.imageViewLocation);
        editTextLocation = view.findViewById(R.id.editTextLocation);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        imageViewLocation.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Requesting location...", Toast.LENGTH_SHORT).show();
            getLocation();
        });

        ajustarTexto();
        return view;
    }

    private void ajustarTexto() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateTextView("Mis Compras", false);
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                convertLocationToAddress(location);
            } else {
                Toast.makeText(getContext(), "Location not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void convertLocationToAddress(Location location) {
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                android.location.Address address = addresses.get(0);

                // Get address components
                StringBuilder addressText = new StringBuilder();
                String street = address.getThoroughfare(); // Street address
                String city = address.getLocality(); // City
                String state = address.getAdminArea(); // State
                String country = address.getCountryName(); // Country
                String postalCode = address.getPostalCode(); // Postal code

                if (street != null) addressText.append(street).append(", ");
                if (city != null) addressText.append(city).append(", ");
                if (state != null) addressText.append(state).append(", ");
                if (country != null) addressText.append(country).append(", ");
                if (postalCode != null) addressText.append(postalCode).append(", ");

                // Remove the trailing comma and space
                if (addressText.length() > 2) {
                    addressText.setLength(addressText.length() - 2);
                }

                // Set the address in the EditText
                editTextLocation.setText(addressText.toString());
                Toast.makeText(getContext(), "Address: " + addressText.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "No address found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), "Geocoder failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                getLocation();
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
