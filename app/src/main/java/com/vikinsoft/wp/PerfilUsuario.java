package com.vikinsoft.wp;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.vikinsoft.wp.adapter.ProductosAdaptador;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;

public class PerfilUsuario extends AppCompatActivity implements OnMapReadyCallback {

    public Integer visible = 0;
    private Toolbar mToolbar;
    private static int REQUEST_CAMERA = 1;
    private static int SELECT_FILE = 2;
    ImageView productof1,productof2,productof3,productof4;
    Dialog dialog;
    private static int arrow_productos = 0;
    private static int arrow_moneda_num = 0;
    HashMap<String, CompoundButton> hash = new HashMap<String, CompoundButton>();
    HashMap<String, CompoundButton> hash2 = new HashMap<String, CompoundButton>();
    private Usuario usuario;
    private boolean editPerfil = false;
    private RecyclerView recyclerView;
    private ProductosAdaptador adaptador;


    public PerfilUsuario(Usuario usuario)
    {
        this.usuario=usuario;
    }

    public PerfilUsuario()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final GlobalClass appstate =(GlobalClass) getApplicationContext();

        Bundle b = getIntent().getExtras();
        int value = -1;
        if(b != null)
        {
            value = b.getInt("id_usuario");
            usuario = appstate.getUsuariobyID(value);
            this.editPerfil = true;
        }else{
            usuario = appstate.usuario;
            this.editPerfil = false;
            System.out.println("siawebo");
        }
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_user);

        LinearLayout boton_enventa = (LinearLayout) findViewById(R.id.perf_venta);
        LinearLayout boton_vendidos = (LinearLayout) findViewById(R.id.perf_vendidos);
        LinearLayout boton_opinion = (LinearLayout) findViewById(R.id.perf_opiniones);
        LinearLayout boton_fav = (LinearLayout) findViewById(R.id.perf_favoritos);

        final LinearLayout vista_enventa = (LinearLayout) findViewById(R.id.lay_perf_enventa);
        final LinearLayout vista_vendidos = (LinearLayout) findViewById(R.id.lay_perf_vendidos);
        final LinearLayout vista_opiniones = (LinearLayout) findViewById(R.id.lay_perf_opiniones);
        final LinearLayout vista_favoritos = (LinearLayout) findViewById(R.id.lay_perf_fav);

        TextView textoenventa = (TextView) findViewById(R.id.contador_prod);
        TextView textovendido = (TextView) findViewById(R.id.contador_vendido);
        TextView textoopinion = (TextView) findViewById(R.id.contador_opinion);
        TextView textofavoritos = (TextView) findViewById(R.id.contador_prod);


        //usuario.getProductosVendiendo(this.getApplicationContext());
        System.out.println("Creados con exito!");
        System.out.println("Creados con exito!"+appstate.usuario.getProductosVendidos().size());

        if(!usuario.getProductosVendiendo().isEmpty())
        {
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view_productos_vendiendo);
            adaptador = new ProductosAdaptador(this, usuario.getProductosVendiendo());

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adaptador);
            textoenventa.setText(""+adaptador.getItemCount());
        }


        //usuario.getProductosVendidos(this.getApplicationContext());
        if(!usuario.getProductosVendidos().isEmpty())
        {
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view_productos_vendidos);
            adaptador = new ProductosAdaptador(this, usuario.getProductosVendidos());

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adaptador);
            textovendido.setText(""+adaptador.getItemCount());
        }




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

        if(usuario.getFoto()==1) {
            ImageView perfil = (ImageView) findViewById(R.id.imagen_perfil_detalle);
            perfil.setImageBitmap(usuario.getImagen());
        }

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmento_mapa);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(true);

        TextView nombre_perfil = (TextView)findViewById(R.id.logged_nombre);
        nombre_perfil.setText(usuario.getNombre());

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





        /////////////INICIO DECLARACION MODAL REGISTRO////////////////////
        final FloatingActionButton botonflotante = (FloatingActionButton) findViewById(R.id.fab_enlistar);
        if(this.editPerfil)
        {
            botonflotante.setVisibility(View.GONE);
        }

        boton_enventa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vista_enventa.setVisibility(View.VISIBLE);
                vista_vendidos.setVisibility(View.INVISIBLE);
                vista_opiniones.setVisibility(View.INVISIBLE);
                vista_favoritos.setVisibility(View.INVISIBLE);
                if(editPerfil)
                {
                    botonflotante.setVisibility(View.GONE);
                }else{
                    botonflotante.setVisibility(View.VISIBLE);
                }
            }
        });

        boton_vendidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vista_enventa.setVisibility(View.INVISIBLE);
                vista_vendidos.setVisibility(View.VISIBLE);
                vista_opiniones.setVisibility(View.INVISIBLE);
                vista_favoritos.setVisibility(View.INVISIBLE);
                botonflotante.setVisibility(View.GONE);

            }
        });

        boton_opinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vista_enventa.setVisibility(View.INVISIBLE);
                vista_vendidos.setVisibility(View.INVISIBLE);
                vista_opiniones.setVisibility(View.VISIBLE);
                vista_favoritos.setVisibility(View.INVISIBLE);
                botonflotante.setVisibility(View.GONE);

            }
        });

        boton_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vista_enventa.setVisibility(View.INVISIBLE);
                vista_vendidos.setVisibility(View.INVISIBLE);
                vista_opiniones.setVisibility(View.INVISIBLE);
                vista_favoritos.setVisibility(View.VISIBLE);
                botonflotante.setVisibility(View.GONE);

            }
        });


        botonflotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(PerfilUsuario.this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_perfil, menu);
        //return true;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_perfil, menu);
        MenuItem fav = menu.findItem(R.id.action_editar);
        MenuItem share = menu.findItem(R.id.action_perfil_logout);
        if (this.editPerfil) {
            fav.setVisible(false);
            share.setVisible(false);
        } else {
            fav.setVisible(true);
            share.setVisible(true);        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        GlobalClass appstate = (GlobalClass) getApplicationContext();

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_editar) {
            Intent editarPerfil = new Intent(PerfilUsuario.this, EditarPerfil.class);
            PerfilUsuario.this.startActivity(editarPerfil);
        }else if (id == R.id.action_perfil_logout){
            usuario.logOut();
            Intent main = new Intent(PerfilUsuario.this,MainActivity.class);
            PerfilUsuario.this.startActivity(main);
            finish();
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
        System.out.println("Latitud "+usuario.getLatitud());
        System.out.println("Longitud "+usuario.getLongitud());

        Double latitud = usuario.getLatitud();
        Double longitud = usuario.getLongitud();

        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(longitud,latitud));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);



    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode)
        {
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


}
