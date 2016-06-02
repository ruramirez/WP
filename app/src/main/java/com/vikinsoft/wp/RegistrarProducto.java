package com.vikinsoft.wp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class RegistrarProducto extends AppCompatActivity implements View.OnClickListener{

    ImageView productof1,productof2,productof3,productof4;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    Button botonguardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);

        productof1 = (ImageView) findViewById(R.id.producto_imagen_1);
        productof2 = (ImageView) findViewById(R.id.producto_imagen_2);
        productof3 = (ImageView) findViewById(R.id.producto_imagen_3);
        productof4 = (ImageView) findViewById(R.id.producto_imagen_4);



        productof1.setOnClickListener(this);
        productof2.setOnClickListener(this);
        productof3.setOnClickListener(this);
        productof4.setOnClickListener(this);

        botonguardar.setOnClickListener(this);

    }


    public void modalSelecciona(View v){

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

        System.out.println("Datos del request "+requestCode);
        System.out.println("Datos del result "+resultCode);
        System.out.println("Datos del intent "+data);
        if(requestCode == 1)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            productof1.setImageBitmap(imageBitmap);

        }else if(requestCode == 2){
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                productof1.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v==productof1 || v==productof2 || v==productof3 || v==productof4)
        {
            //modalSelecciona(v);
            Toast.makeText(RegistrarProducto.this, "Seleccione "+v, Toast.LENGTH_SHORT).show();
        }else if( v== botonguardar) {

            EditText titulo = (EditText) findViewById(R.id.producto_titulo);
            EditText descripcion = (EditText) findViewById(R.id.producto_descripcion);
            EditText precio = (EditText) findViewById(R.id.producto_precio);
            EditText categoria = (EditText) findViewById(R.id.producto_categoria);
            TextView moneda = (TextView) findViewById(R.id.producto_moneda);
            CheckBox envio = (CheckBox) findViewById(R.id.registrar_check_envio);
            CheckBox precioNegociable = (CheckBox) findViewById(R.id.registrar_check_precio);

            int enviar;
            if(envio.isChecked())
            {
                enviar = 1;
            }else{
                enviar = 0;
            }

            int negociable;
            if(precioNegociable.isChecked())
            {
                negociable = 1;
            }else{
                negociable = 0;
            }

            float precio_final = Float.parseFloat(precio.getText().toString());

            //Producto producto = new Producto(titulo.getText().toString(),descripcion.getText().toString(),enviar,negociable,precio_final,categoria.getText().toString(),moneda.getText().toString());
            //Toast.makeText(RegistrarProducto.this, "Hehe", Toast.LENGTH_SHORT).show();
        }


    }
}
