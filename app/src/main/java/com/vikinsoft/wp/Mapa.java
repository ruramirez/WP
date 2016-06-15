package com.vikinsoft.wp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapa extends AppCompatActivity implements OnMapReadyCallback {

    Usuario usuario;


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
        Bundle b = getIntent().getExtras();
        int value = -1;
        if(b != null)
        {
            value = b.getInt("id_usuario");
            usuario = appstate.getUsuariobyID(value);
        }else{
            usuario = appstate.usuario;
        }

        Double latitud = usuario.getLatitud();
        Double longitud = usuario.getLongitud();

        googleMap.addMarker(new MarkerOptions().position(new LatLng(longitud,latitud)).title(usuario.getDireccion()));

        LatLng pos=new LatLng(longitud,latitud);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 18.0f));

    }
}
