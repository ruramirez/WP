package com.vikinsoft.wp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class PerfilUsuario extends AppCompatActivity implements OnMapReadyCallback {

    public Integer visible = 0;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        GlobalClass appstate =(GlobalClass) getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_user);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_perfil);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Perfil de usuario");

        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomain = new Intent(PerfilUsuario.this, MainActivity.class);
                PerfilUsuario.this.startActivity(gotomain);
            }
        });

        if(appstate.usuario.getFoto()==1) {
            ImageView perfil = (ImageView) findViewById(R.id.imagen_perfil);
            perfil.setImageBitmap(appstate.usuario.getImagen());
        }

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmento_mapa);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(true);

        TextView nombre_perfil = (TextView)findViewById(R.id.logged_nombre);
        nombre_perfil.setText(appstate.usuario.getNombre());

        final LinearLayout verifi = (LinearLayout)findViewById(R.id.lay_verificacion);

        final ImageButton expand = (ImageButton) findViewById(R.id.expansor);
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visible == 0)
                {
                    expand.setImageResource(R.drawable.arrowup);
                    visible = 1;
                    verifi.setVisibility(View.VISIBLE);
                }else{
                    expand.setImageResource(R.drawable.arrowdown);
                    visible = 0;
                    verifi.setVisibility(View.GONE);
                }

            }
        });

        LinearLayout boton_enventa = (LinearLayout) findViewById(R.id.perf_venta);
        LinearLayout boton_vendidos = (LinearLayout) findViewById(R.id.perf_vendidos);
        LinearLayout boton_opinion = (LinearLayout) findViewById(R.id.perf_opiniones);
        LinearLayout boton_fav = (LinearLayout) findViewById(R.id.perf_favoritos);

        final LinearLayout vista_enventa = (LinearLayout) findViewById(R.id.lay_perf_enventa);
        final LinearLayout vista_vendidos = (LinearLayout) findViewById(R.id.lay_perf_vendidos);
        final LinearLayout vista_opiniones = (LinearLayout) findViewById(R.id.lay_perf_opiniones);
        final LinearLayout vista_favoritos = (LinearLayout) findViewById(R.id.lay_perf_fav);


        boton_enventa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vista_enventa.setVisibility(View.VISIBLE);
                vista_vendidos.setVisibility(View.INVISIBLE);
                vista_opiniones.setVisibility(View.INVISIBLE);
                vista_favoritos.setVisibility(View.INVISIBLE);
            }
        });

        boton_vendidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vista_enventa.setVisibility(View.INVISIBLE);
                vista_vendidos.setVisibility(View.VISIBLE);
                vista_opiniones.setVisibility(View.INVISIBLE);
                vista_favoritos.setVisibility(View.INVISIBLE);
            }
        });

        boton_opinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vista_enventa.setVisibility(View.INVISIBLE);
                vista_vendidos.setVisibility(View.INVISIBLE);
                vista_opiniones.setVisibility(View.VISIBLE);
                vista_favoritos.setVisibility(View.INVISIBLE);
            }
        });

        boton_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vista_enventa.setVisibility(View.INVISIBLE);
                vista_vendidos.setVisibility(View.INVISIBLE);
                vista_opiniones.setVisibility(View.INVISIBLE);
                vista_favoritos.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_editar) {
            Intent editarPerfil = new Intent(PerfilUsuario.this, EditarPerfil.class);
            PerfilUsuario.this.startActivity(editarPerfil);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        GlobalClass appstate = (GlobalClass)getApplicationContext();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent myIntent = new Intent(PerfilUsuario.this, Mapa.class);
                PerfilUsuario.this.startActivity(myIntent);
            }
        });
        System.out.println("Latitud "+appstate.usuario.getLatitud());
        System.out.println("Longitud "+appstate.usuario.getLongitud());

        Double latitud = appstate.usuario.getLatitud();
        Double longitud = appstate.usuario.getLongitud();

        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(longitud,latitud));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);



    }


}
