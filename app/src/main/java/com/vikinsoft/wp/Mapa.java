package com.vikinsoft.wp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(true);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GlobalClass appstate = (GlobalClass)getApplicationContext();

        Double latitud = appstate.usuario.getLatitud();
        Double longitud = appstate.usuario.getLongitud();

        googleMap.addMarker(new MarkerOptions().position(new LatLng(longitud,latitud)).title(appstate.usuario.getDireccion()));

        LatLng pos=new LatLng(longitud,latitud);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 18.0f));

    }
}
