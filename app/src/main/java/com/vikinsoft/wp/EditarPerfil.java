package com.vikinsoft.wp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class EditarPerfil extends AppCompatActivity implements View.OnClickListener {

    private boolean imageChanged= false;

    private Toolbar editToolbar;
    TextView campo_nac,direccion;
    LinearLayout contraseña,correo,categoriasfav;
    ImageView foto;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GlobalClass appstate =(GlobalClass) getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        editToolbar = (Toolbar) findViewById(R.id.toolbar_perfil_editar);

        setSupportActionBar(editToolbar);
        getSupportActionBar().setTitle("Editar perfil");

        editToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        editToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vista = new Intent(EditarPerfil.this,PerfilUsuario.class);
                EditarPerfil.this.startActivity(vista);
            }
        });

        EditText nombre = (EditText) findViewById(R.id.editar_pef_nombre);
        nombre.setText(appstate.usuario.getNombre());


        direccion = (TextView) findViewById(R.id.editar_perf_loc);
        direccion.setText(appstate.usuario.getDireccion());
        direccion.setOnClickListener(this);

        contraseña = (LinearLayout) findViewById(R.id.editar_perfil_contraseña);
        contraseña.setOnClickListener(this);

        correo = (LinearLayout) findViewById(R.id.editar_perfil_correo);
        correo.setOnClickListener(this);

        if(appstate.usuario.getFoto()==1)
        {
            ImageView perfil = (ImageView) findViewById(R.id.editar_perfil_foto);
            perfil.setImageBitmap(appstate.usuario.getImagen());
        }

        foto = (ImageView) findViewById(R.id.editar_perfil_foto);
        foto.setOnClickListener(this);

        categoriasfav = (LinearLayout) findViewById(R.id.categorias_favoritas_boton);
        categoriasfav.setOnClickListener(this);



    }

    private void modalPass()
    {

        final GlobalClass appstate =(GlobalClass) getApplicationContext();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        if(appstate.usuario.getEmailValido() == 1)
        {
            final View dialogView = inflater.inflate(R.layout.activity_modal_contrasena, null);
            alertDialog.setView(dialogView);
            alertDialog.setTitle("Cambiar contraseña");
            alertDialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    EditText oldpass = (EditText) dialogView.findViewById(R.id.contrasena_modal);
                    EditText nuevapass = (EditText) dialogView.findViewById(R.id.nuevacontrasena_modal);
                    EditText confirmarpass = (EditText) dialogView.findViewById(R.id.confirmarnueva_modal);
                    if(oldpass.getText().toString().equals(appstate.usuario.getPassword()))
                    {
                        if(nuevapass.getText().toString().equals(confirmarpass.getText().toString()))
                        {
                            appstate.usuario.setPassword(confirmarpass.getText().toString());
                            appstate.usuario.updateUsuario();
                            Toast.makeText(EditarPerfil.this, "Contraseña guardada.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //pass
                }
            });
            alertDialog.create();
            alertDialog.show();

        }else{
            final View dialogView = inflater.inflate(R.layout.modal_contrasena_fbgoogle, null);
            alertDialog.setView(dialogView);
            alertDialog.setTitle("Cambiar contraseña");
            alertDialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    EditText nueva = (EditText) dialogView.findViewById(R.id.nuevacontrasena_modal_social);
                    EditText confirmar = (EditText) dialogView.findViewById(R.id.confirmarnueva_modal_social);
                    if(nueva.getText().toString().equals(confirmar.getText().toString()))
                    {
                        appstate.usuario.setPassword(confirmar.getText().toString());
                        appstate.usuario.updateUsuario();
                        Toast.makeText(EditarPerfil.this, "Contraseña guardada.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //pass
                }
            });
            alertDialog.create();
            alertDialog.show();
        }
    }

    private void modalCorreo()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.modal_correo, null);
        alertDialog.setView(dialogView);

        final GlobalClass appstate =(GlobalClass) getApplicationContext();

        final EditText mail = (EditText) dialogView.findViewById(R.id.correo_modal);
        mail.setText(appstate.usuario.getEmail());

        alertDialog.setTitle("Cambiar correo");
        alertDialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if(mail != null)
                {
                    appstate.usuario.setEmail(mail.getText().toString());
                    appstate.usuario.updateUsuario();
                    Toast.makeText(EditarPerfil.this, "Correo guardado.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        alertDialog.create();
        alertDialog.show();

    }

    private void modalSelecciona(){

        final Integer[] PROFILE_PIC_COUNT = {0};
        final CharSequence[] items = {"Tomar foto", "Seleccionar de la galeria", "Cancelar"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Cambiar foto de perfil");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Tomar foto")) {
                    PROFILE_PIC_COUNT[0] = 1;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                    System.out.println("Resultado es ");
                } else if (items[item].equals("Seleccionar de la galeria")) {
                    PROFILE_PIC_COUNT[0] = 1;
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,SELECT_FILE);
                } else if (items[item].equals("Cancelar")) {
                    PROFILE_PIC_COUNT[0] = 0;
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        imageChanged = true;
        if(requestCode == 1)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            foto.setImageBitmap(imageBitmap);

        }else if(requestCode == 2){
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                boolean tamañoIncorrecto = true;
                while (tamañoIncorrecto) {
                    if (bm.getWidth() >= 2048 || bm.getHeight() >= 2048) {
                        Double d = new Double((bm.getWidth() * .7));

                        int width = d.intValue();

                        d = new Double((bm.getHeight() * .7));

                        int heigth = d.intValue();
                        bm=bm.createScaledBitmap(bm, width, heigth, false);

                    }
                    else
                    {
                        tamañoIncorrecto = false;
                    }
                }


                foto.setImageBitmap(bm);
            } catch (IOException e) {


                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == direccion)
        {
            //Aqui tendremos el metodo a implementar cuando tengamos la parte del GPS
        }else if(v == contraseña){
            modalPass();
        }else if(v == correo){
            modalCorreo();
        }else if(v == foto){
            modalSelecciona();
        }else if(v == categoriasfav){
            Intent categorias = new Intent(EditarPerfil.this,CategoriasFavoritas.class);
            EditarPerfil.this.startActivity(categorias);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil_editar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_guardar_perfil) {

            GlobalClass appstate =(GlobalClass) getApplicationContext();

            ImageView imagen = (ImageView) findViewById(R.id.editar_perfil_foto);
            if(imageChanged)
            {
                appstate.usuario.setFoto(1);
                Bitmap bitmap = ((BitmapDrawable)imagen.getDrawable()).getBitmap();

                appstate.usuario.setImagen(bitmap);
                appstate.usuario.storeImage(appstate.usuario.getImagen());
            }
            EditText nombre = (EditText) findViewById(R.id.editar_pef_nombre);
            appstate.usuario.setNombre(nombre.getText().toString());
            appstate.usuario.updateUsuario();

            Toast.makeText(EditarPerfil.this, "Datos guardados", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


}
