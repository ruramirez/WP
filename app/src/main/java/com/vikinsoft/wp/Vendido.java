package com.vikinsoft.wp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class Vendido extends AppCompatActivity {

    Toolbar mToolbar;
    Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final GlobalClass appstate = (GlobalClass) getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendido);

        Bundle b = getIntent().getExtras();
        int value = -1;
        if(b != null)
        {
            value = b.getInt("id_producto");
            producto = appstate.getProductobyID(value);
            if(producto == null)
            {
                producto = new Producto(value,getApplicationContext(),appstate);
            }
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar_vendido);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent gotomain = new Intent(ProductoDetalle.this, MainActivity.class);
                //ProductoDetalle.this.startActivity(gotomain);
            }
        });

        TextView textoVendido = (TextView) findViewById(R.id.vendido_textovendido);
        textoVendido.setText("Vendiste ' "+producto.getNombre()+" '");

        LinearLayout linear = (LinearLayout) findViewById(R.id.vendido_linear);
        if(appstate.getChatByProducto(producto).size() > 0)
        {
            linear.setVisibility(View.VISIBLE);
        }else{
            linear.setVisibility(View.GONE);
        }

        for (final Chat chat : appstate.getChatByProducto(producto))
        {
            System.out.println("chaaaaaaaat "+chat.getComprador().getId());
            final RelativeLayout relativo = new RelativeLayout(this.getApplicationContext());
            relativo.setBackgroundDrawable(getResources().getDrawable(R.drawable.borde));
            relativo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params1.addRule(RelativeLayout.CENTER_VERTICAL);
            //params1.addRule(RelativeLayout.ALIGN_PARENT_START);

            TextView texto = new TextView(this.getApplicationContext());
            texto.setText(chat.getComprador().getNombre());
            texto.setTextSize(18);
            texto.setTextColor(Color.parseColor("#000000"));
            texto.setLayoutParams(params1);

            CircleImageView imageView = new CircleImageView(this.getApplicationContext());

            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params2.addRule(RelativeLayout.ALIGN_PARENT_START);
            params2.setMargins(100,50,0,0);

            imageView.setImageBitmap(chat.getComprador().getImagen());
            imageView.setLayoutParams(params2);

            relativo.getLayoutParams().height = 250;

            relativo.addView(texto);
            relativo.addView(imageView);
            linear.addView(relativo);

            relativo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent calificar = new Intent(Vendido.this,Calificar.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("id_producto", producto.getId());
                    mBundle.putInt("id_usuario", chat.getComprador().getId());
                    calificar.putExtras(mBundle);
                    Vendido.this.startActivity(calificar);
                    finish();
                }
            });
        }

        final RelativeLayout relativo = new RelativeLayout(this.getApplicationContext());
        relativo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params1.addRule(RelativeLayout.CENTER_VERTICAL);
        params1.setMargins(0,50,0,0);

        TextView texto = new TextView(this.getApplicationContext());
        texto.setText("Vendi este producto fuera de WePlay");
        texto.setTextSize(15);
        texto.setTextColor(Color.parseColor("#000000"));
        texto.setLayoutParams(params1);
        texto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        relativo.addView(texto);
        linear.addView(relativo);

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
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
