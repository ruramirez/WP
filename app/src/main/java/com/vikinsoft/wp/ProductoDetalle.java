package com.vikinsoft.wp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ProductoDetalle extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, OnMapReadyCallback {

    private SliderLayout mDemoSlider;
    private Producto producto;
    private Toolbar mToolbar;
    private boolean editPerfil = false;
    Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final GlobalClass appstate = (GlobalClass) getApplicationContext();



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalle);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_detalle_final);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomain = new Intent(ProductoDetalle.this, MainActivity.class);
                ProductoDetalle.this.startActivity(gotomain);
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmento_mapa_detalle);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(true);


        Bundle b = getIntent().getExtras();
        int value = -1;
        if(b != null)
        {

            value = b.getInt("id_producto");
            System.out.println("el id"+value);
            producto = appstate.getProductobyID(value);
        }
        final RelativeLayout botones = (RelativeLayout) findViewById(R.id.reservar_vender_layout);

        usuario = producto.getUsuario();
        if(usuario.getId() == appstate.usuario.getId())
        {
            botones.setVisibility(View.VISIBLE);
        }


        HashMap<String,String> url_maps = new HashMap<String, String>();
        for (FotosProductos fotos : producto.getFotos())
        {
            url_maps.put(String.valueOf(fotos.getId()),fotos.getUrl());
        }

        for(String name : url_maps.keySet()){
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //.description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.stopAutoCycle();

        TextView precio = (TextView) findViewById(R.id.precioymoneda_detalle);
        precio.setText(producto.getMoneda().getSimbolo()+" "+producto.getPrecio());
        TextView titulo = (TextView) findViewById(R.id.titulo_detalle);
        titulo.setText(producto.getNombre());
        TextView descr = (TextView) findViewById(R.id.descripcion_detalle);
        descr.setText(producto.getDescripcion());

        ImageView imagen = (ImageView) findViewById(R.id.imagen_perfil_detalle);
        if(producto.getUsuario().getFoto() == 1)
        {
            imagen.setImageBitmap(producto.getUsuario().getImagen());
        }

        TextView nombre = (TextView) findViewById(R.id.detalle_usuario_nombre);
        nombre.setText(producto.getUsuario().getNombre());

        if(producto.getEnvio() == 0)
        {
           //ImageButton botonenvio =  (ImageButton) findViewById(R.id.detalle)
        }



        imagen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductoDetalle.this,PerfilUsuario.class);
                Bundle b = new Bundle();
                b.putInt("id_usuario", producto.getUsuario().getId());
                intent.putExtras(b);
                ProductoDetalle.this.startActivity(intent);
            }
        });

        final Button botonDesreservar = (Button) findViewById(R.id.boton_removerreserva);
        final Button botonReservar = (Button) findViewById(R.id.boton_reservar);
        final Button botonVender = (Button) findViewById(R.id.boton_vendido);

        botonReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                botonDesreservar.setVisibility(View.VISIBLE);
                producto.setEstadoProducto(appstate.getElementoByID(2));
                producto.update();
            }
        });

        botonDesreservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                botonReservar.setVisibility(View.VISIBLE);
                producto.setEstadoProducto(appstate.getElementoByID(0));
                producto.update();
            }
        });

        botonVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botones.setVisibility(View.GONE);
                producto.setEstadoProducto(appstate.getElementoByID(1));
                producto.update();
                appstate.usuario.ChangeProductoVendiendoAvendido(producto);


            }
        });


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Intent intent = new Intent(ProductoDetalle.this,Slider_full.class);
        Bundle b = new Bundle();
        b.putInt("id_producto", producto.getId());
        intent.putExtras(b);
        ProductoDetalle.this.startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

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
                Intent myIntent = new Intent(ProductoDetalle.this, Mapa.class);
                Bundle b = new Bundle();
                b.putInt("id_usuario", producto.getUsuario().getId());
                myIntent.putExtras(b);
                ProductoDetalle.this.startActivity(myIntent);
            }
        });


        Double latitud = producto.getUsuario().getLatitud();
        Double longitud = producto.getUsuario().getLongitud();

        System.out.println("latituti "+latitud);
        System.out.println("longitutu "+longitud);


      /*  CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(longitud,latitud));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);*/
/*
        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle, menu);
        return true;



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

}
