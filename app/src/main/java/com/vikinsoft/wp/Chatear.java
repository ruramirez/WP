package com.vikinsoft.wp;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vikinsoft.wp.adapter.ChatAdapter;

import java.util.concurrent.ExecutionException;

public class Chatear extends AppCompatActivity {

    Toolbar mToolbar;
    Producto producto;
    ImageView productofoto,botonmensaje;
    TextView productotexto;
    EditText textomensaje;
    ChatAdapter chatAdapter;
    ListView messagesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GlobalClass appstate = (GlobalClass) getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle b = getIntent().getExtras();
        System.out.println("el producto"+b.getInt("id_producto"));

        producto = appstate.getProductobyID(b.getInt("id_producto"));

        appstate.getChatByComprador(appstate.usuario,producto);
        //chat = producto.getChatByComprador(appstate.usuario);

        productofoto = (ImageView) findViewById(R.id.producto_chat_imagen);
        productotexto = (TextView) findViewById(R.id.producto_chat_texto);

        Glide.with(getApplicationContext()).load(producto.getFotos().get(0).getUrl()).into(productofoto);

        productotexto.setText(producto.getNombre());

        botonmensaje = (ImageView) findViewById(R.id.chat_boton_mensaje);
        textomensaje = (EditText) findViewById(R.id.chat_texto_mensaje);


        mToolbar = (Toolbar) findViewById(R.id.toolbar_chat_final);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent gotomain = new Intent(Chatear.this, PerfilUsuario.class);
                //Chatear.this.startActivity(gotomain);
            }
        });

        System.out.println("Antes del adaptadorrrrrrrrrrr");
        int conteo = appstate.getChatByComprador(appstate.usuario,producto).getMensajes().size();
        System.out.println("Conteo es "+conteo);
        chatAdapter = new ChatAdapter(this, appstate.getChatByComprador(appstate.usuario,producto).getMensajes());
        messagesView = (ListView) findViewById(R.id.vista_mensajes);
        messagesView.setAdapter(chatAdapter);
        System.out.println("Despues del adaptador");
        botonmensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(textomensaje.getText() != null || !textomensaje.getText().toString().equals(""))
                {
                    GlobalClass appstate = (GlobalClass) getApplicationContext();
                    appstate.getChatByComprador(appstate.usuario,producto).addMensaje(textomensaje.getText().toString());
                    textomensaje.setText("");
                    chatAdapter.notifyDataSetChanged();
                    messagesView.smoothScrollToPosition(messagesView.getCount());
                }
            }
        });
        loop.start();

    }


    Thread loop = new Thread()
    {
        @Override
        public void run() {
            try {
                while(true) {
                    sleep(2000);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GlobalClass appstate = (GlobalClass) getApplicationContext();
                            if(appstate.getChatByComprador(appstate.usuario,producto).getNuevos()==1)
                            {
                                chatAdapter = new ChatAdapter(getApplicationContext(), appstate.getChatByComprador(appstate.usuario,producto).getMensajes());
                                messagesView.setAdapter(chatAdapter);
                                chatAdapter.notifyDataSetChanged();
                                messagesView.smoothScrollToPosition(messagesView.getCount());
                                appstate.getChatByComprador(appstate.usuario,producto).setNuevos(0);
                            }

                        }
                    });
                }
            } catch (InterruptedException e) {
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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





