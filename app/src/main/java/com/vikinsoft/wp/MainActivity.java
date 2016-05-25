package com.vikinsoft.wp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.vikinsoft.wp.activity.FragmentDrawer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private static int REQUEST_CAMERA = 1;
    private static int SELECT_FILE = 2;
    ImageView productof1,productof2,productof3,productof4;
    Dialog dialog;
    private static int arrow_productos = 0;
    private static int arrow_moneda_num = 0;
    HashMap<String, CompoundButton> hash = new HashMap<String, CompoundButton>();
    HashMap<String, CompoundButton> hash2 = new HashMap<String, CompoundButton>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final GlobalClass appstate =(GlobalClass) getApplicationContext();
        appstate.usuario = Usuario.loadUsuario(this,this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /// INICIO DECLARACION GPS ///

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        /// FIN DECLARACION GPS///

        /////////////INICIO DECLARACION MODAL REGISTRO////////////////////
        final FloatingActionButton botonflotante = (FloatingActionButton) findViewById(R.id.fab_enlistar);
        botonflotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_registrar_producto);
                LinearLayout linear = null;
                linear = (LinearLayout)dialog.findViewById(R.id.contenedor_categorias_producto);
                LinearLayout linearMoneda = null;
                linearMoneda = (LinearLayout)dialog.findViewById(R.id.contenedor_monedas);

                ///////Inicio funciones Moneda//////////
                dialog.findViewById(R.id.arrow_moneda).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(arrow_moneda_num == 0)
                        {
                            ((ImageButton) v).setImageResource(R.drawable.arrowup);
                            dialog.findViewById(R.id.contenedor_imagenes_registro).setVisibility(View.GONE);
                            dialog.findViewById(R.id.producto_titulo).setVisibility(View.GONE);
                            dialog.findViewById(R.id.producto_descripcion).setVisibility(View.GONE);
                            dialog.findViewById(R.id.contenedor_opciones_registro).setVisibility(View.GONE);
                            dialog.findViewById(R.id.producto_boton_guardar).setVisibility(View.GONE);
                            dialog.findViewById(R.id.categoria_layout).setVisibility(View.GONE);
                            dialog.findViewById(R.id.contenedor_monedas).setVisibility(View.VISIBLE);
                            arrow_moneda_num = 1;
                        }else{
                            ((ImageButton) v).setImageResource(R.drawable.arrowdown);
                            dialog.findViewById(R.id.contenedor_imagenes_registro).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_titulo).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_descripcion).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.contenedor_opciones_registro).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_boton_guardar).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.categoria_layout).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.contenedor_monedas).setVisibility(View.GONE);
                            arrow_moneda_num = 0;
                        }
                    }
                });
                dialog.findViewById(R.id.producto_moneda).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(arrow_moneda_num == 0)
                        {
                            ((ImageButton) dialog.findViewById(R.id.arrow_moneda)).setImageResource(R.drawable.arrowup);
                            dialog.findViewById(R.id.contenedor_imagenes_registro).setVisibility(View.GONE);
                            dialog.findViewById(R.id.producto_titulo).setVisibility(View.GONE);
                            dialog.findViewById(R.id.producto_descripcion).setVisibility(View.GONE);
                            dialog.findViewById(R.id.contenedor_opciones_registro).setVisibility(View.GONE);
                            dialog.findViewById(R.id.producto_boton_guardar).setVisibility(View.GONE);
                            dialog.findViewById(R.id.categoria_layout).setVisibility(View.GONE);
                            dialog.findViewById(R.id.contenedor_monedas).setVisibility(View.VISIBLE);
                            arrow_moneda_num = 1;
                        }else{
                            ((ImageButton) dialog.findViewById(R.id.arrow_moneda)).setImageResource(R.drawable.arrowdown);                            dialog.findViewById(R.id.contenedor_imagenes_registro).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_titulo).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_descripcion).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.contenedor_opciones_registro).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_boton_guardar).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.categoria_layout).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.contenedor_monedas).setVisibility(View.GONE);
                            arrow_moneda_num = 0;
                        }
                    }
                });

                for (Moneda moneda : appstate.monedas) {

                    final RelativeLayout relativo = new RelativeLayout(dialog.getContext());
                    relativo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    TextView texto = new TextView(dialog.getContext());
                    texto.setText(moneda.getNombre());
                    texto.setTextSize(18);
                    RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    params1.addRule(RelativeLayout.ALIGN_PARENT_START);
                    texto.setLayoutParams(params1);
                    final CheckBox box = new CheckBox(dialog.getContext());
                    final String nombreMoneda = moneda.getSimbolo();
                    box.setTag(nombreMoneda);
                    box.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ((ImageButton) dialog.findViewById(R.id.arrow_moneda)).setImageResource(R.drawable.arrowdown);
                            ((TextView) dialog.findViewById(R.id.producto_moneda)).setText(nombreMoneda);
                            dialog.findViewById(R.id.contenedor_imagenes_registro).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_titulo).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_descripcion).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.contenedor_opciones_registro).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_boton_guardar).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.categoria_layout).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.contenedor_monedas).setVisibility(View.GONE);
                            arrow_moneda_num = 0;
                        }
                    });
                    box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                if(hash2.size()>0){
                                    hash2.get("1").setChecked(false);
                                }
                                hash2.put("1", buttonView);
                            }else{
                                hash2.clear();
                            }
                        }
                    });
                    RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params2.addRule(RelativeLayout.ALIGN_BOTTOM,texto.getId());
                    params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params2.addRule(RelativeLayout.ALIGN_PARENT_END);
                    box.setLayoutParams(params2);
                    relativo.addView(texto);
                    relativo.addView(box);
                    linearMoneda.addView(relativo);

                }

                /////Fin funciones Moneda/////////////


                ///////////Inicio Funciones Categorias///////////////
                dialog.findViewById(R.id.arrow_categorias).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(arrow_productos == 0)
                        {
                            ((ImageButton) v).setImageResource(R.drawable.arrowup);
                            dialog.findViewById(R.id.contenedor_imagenes_registro).setVisibility(View.GONE);
                            dialog.findViewById(R.id.producto_titulo).setVisibility(View.GONE);
                            dialog.findViewById(R.id.producto_descripcion).setVisibility(View.GONE);
                            dialog.findViewById(R.id.contenedor_moneda_registro).setVisibility(View.GONE);
                            dialog.findViewById(R.id.contenedor_opciones_registro).setVisibility(View.GONE);
                            dialog.findViewById(R.id.producto_boton_guardar).setVisibility(View.GONE);
                            dialog.findViewById(R.id.contenedor_categorias_producto).setVisibility(View.VISIBLE);
                            arrow_productos = 1;
                        }else{
                            ((ImageButton) v).setImageResource(R.drawable.arrowdown);
                            dialog.findViewById(R.id.contenedor_imagenes_registro).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_titulo).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_descripcion).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.contenedor_moneda_registro).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.contenedor_opciones_registro).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_boton_guardar).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.contenedor_categorias_producto).setVisibility(View.GONE);
                            arrow_productos = 0;
                        }
                    }
                });
                dialog.findViewById(R.id.producto_categoria).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(arrow_productos == 0)
                        {
                            ((ImageButton) dialog.findViewById(R.id.arrow_categorias)).setImageResource(R.drawable.arrowup);
                            dialog.findViewById(R.id.contenedor_imagenes_registro).setVisibility(View.GONE);
                            dialog.findViewById(R.id.producto_titulo).setVisibility(View.GONE);
                            dialog.findViewById(R.id.producto_descripcion).setVisibility(View.GONE);
                            dialog.findViewById(R.id.contenedor_moneda_registro).setVisibility(View.GONE);
                            dialog.findViewById(R.id.contenedor_opciones_registro).setVisibility(View.GONE);
                            dialog.findViewById(R.id.producto_boton_guardar).setVisibility(View.GONE);
                            dialog.findViewById(R.id.contenedor_categorias_producto).setVisibility(View.VISIBLE);
                            arrow_productos = 1;
                        }else{
                            ((ImageButton) dialog.findViewById(R.id.arrow_categorias)).setImageResource(R.drawable.arrowdown);
                            dialog.findViewById(R.id.contenedor_imagenes_registro).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_titulo).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_descripcion).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.contenedor_moneda_registro).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.contenedor_opciones_registro).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_boton_guardar).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.contenedor_categorias_producto).setVisibility(View.GONE);
                            arrow_productos = 0;
                        }
                    }
                });

                for (Categoria categoria : appstate.categorias) {

                    final RelativeLayout relativo = new RelativeLayout(dialog.getContext());
                    relativo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    TextView texto = new TextView(dialog.getContext());
                    texto.setText(categoria.getNombre());
                    texto.setTextSize(18);
                    RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    params1.addRule(RelativeLayout.ALIGN_PARENT_START);
                    texto.setLayoutParams(params1);
                    final CheckBox box = new CheckBox(dialog.getContext());
                    final String nombreCat = categoria.getNombre();
                    box.setTag(nombreCat);
                    box.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ((ImageButton) dialog.findViewById(R.id.arrow_categorias)).setImageResource(R.drawable.arrowdown);
                            ((EditText) dialog.findViewById(R.id.producto_categoria)).setText(nombreCat);
                            dialog.findViewById(R.id.contenedor_imagenes_registro).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_titulo).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_descripcion).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.contenedor_moneda_registro).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.contenedor_opciones_registro).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.producto_boton_guardar).setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.contenedor_categorias_producto).setVisibility(View.GONE);
                            arrow_productos = 0;
                        }
                    });
                    box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                if(hash.size()>0){
                                    hash.get("1").setChecked(false);
                                }
                                hash.put("1", buttonView);
                            }else{
                                hash.clear();
                            }
                        }
                    });
                    RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params2.addRule(RelativeLayout.ALIGN_BOTTOM,texto.getId());
                    params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params2.addRule(RelativeLayout.ALIGN_PARENT_END);
                    box.setLayoutParams(params2);
                    relativo.addView(texto);
                    relativo.addView(box);
                    linear.addView(relativo);

                }
                ///////////Fin Funciones Categorias///////////////

                dialog.findViewById(R.id.producto_imagen_1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       REQUEST_CAMERA = 5;
                       SELECT_FILE = 1;
                       modalSelecciona();
                    }
                });
                dialog.findViewById(R.id.producto_imagen_2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        REQUEST_CAMERA = 6;
                        SELECT_FILE = 2;
                        modalSelecciona();
                    }
                });
                dialog.findViewById(R.id.producto_imagen_3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        REQUEST_CAMERA = 7;
                        SELECT_FILE = 3;
                        modalSelecciona();
                    }
                });
                dialog.findViewById(R.id.producto_imagen_4).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        REQUEST_CAMERA = 8;
                        SELECT_FILE = 4;
                        modalSelecciona();
                    }
                });
                dialog.show();
            }
        });


        ///////FIN DECLARACION MODAL REGISTRO/////////

        /// INICIO DECLARACION DEL TOOLBAR///
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /// FIN DECLARACION DEL TOOLBAR///


        /// INICIO DECLARACION DEL MENU IZQUIERDO///
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        /// FIN DECLARACION DEL MENU IZQUIERDO///


        /// INICIO IMAGEN Y LISTENER DEL PLACEHOLDER DE IMAGEN(LLEVA A LOGIN) ///
        ImageView imgview =(ImageView) findViewById(R.id.main_profile);
        imgview.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(MainActivity.this, Login.class);
                MainActivity.this.startActivity(myIntent);


                DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawers();
            }
        });
        /// FIN IMAGEN Y LISTENER DEL PLACEHOLDER DE IMAGEN(LLEVA A LOGIN) ///


        /// INICIO IMAGEN Y LISTENER DEL PLACEHOLDER DE IMAGEN LOGEADO (LLEVA A PERFIL) ///
        RelativeLayout logged_lay =(RelativeLayout) findViewById(R.id.profile_logged);
        logged_lay.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent esta_vista = new Intent(MainActivity.this, PerfilUsuario.class);
                MainActivity.this.startActivity(esta_vista);


                DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawers();
            }
        });
        /// FIN IMAGEN Y LISTENER DEL PLACEHOLDER DE IMAGEN LOGEADO (LLEVA A PERFIL) ///
    }

    public void modalSelecciona(){

        final Integer[] PROFILE_PIC_COUNT = {0};
        final CharSequence[] items = {"Tomar foto", "Seleccionar de la galeria", "Cancelar"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Subir imagen de producto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

               if (items[item].equals("Tomar foto")) {
                    PROFILE_PIC_COUNT[0] = 1;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Seleccionar de la galeria")) {
                    PROFILE_PIC_COUNT[0] = 1;
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_FILE);
                } else if (items[item].equals("Cancelar")) {
                    PROFILE_PIC_COUNT[0] = 0;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    /// INICIO METODOS DE TOOLBAR ///
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /// FIN METODOS DE TOOLBAR ///

    /// INICIO METODOS DE MENU ///
    @Override
    public void onDrawerItemSelected(View view, int position) {

    }
    /// FIN METODOS DE MENU ///

  ///INICIO Metodos de la conexion GPS///
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //...

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MainActivity.this,
                                    REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //...
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode)
        {
            case REQUEST_LOCATION:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // All required changes were successfully made
                        Toast.makeText(MainActivity.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(MainActivity.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
            case 1:
            {

                try {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    productof1 = (ImageView) dialog.findViewById(R.id.producto_imagen_1);
                    productof1.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            }
            case 2:
            {
                try {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    productof2 = (ImageView) dialog.findViewById(R.id.producto_imagen_2);
                    productof2.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;


            }
            case 3:
            {
                try {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    productof3 = (ImageView) dialog.findViewById(R.id.producto_imagen_3);
                    productof3.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            }
            case 4:
            {
                try {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    productof4 = (ImageView) dialog.findViewById(R.id.producto_imagen_4);
                    productof4.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;


            }
            case 5:
            {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                productof1 = (ImageView) dialog.findViewById(R.id.producto_imagen_1);
                productof1.setImageBitmap(imageBitmap);
                break;

            }
            case 6:
            {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                productof2 = (ImageView) dialog.findViewById(R.id.producto_imagen_2);
                productof2.setImageBitmap(imageBitmap);
                break;

            }
            case 7:
            {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                productof3 = (ImageView) dialog.findViewById(R.id.producto_imagen_3);
                productof3.setImageBitmap(imageBitmap);
                break;

            }
            case 8:
            {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                productof4 = (ImageView) dialog.findViewById(R.id.producto_imagen_4);
                productof4.setImageBitmap(imageBitmap);
                break;

            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(MainActivity.this, "Cambie", Toast.LENGTH_SHORT).show();
    }
/////FIN Metodos de la conexion GPS//////
}