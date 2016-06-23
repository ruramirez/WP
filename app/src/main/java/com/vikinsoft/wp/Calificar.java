package com.vikinsoft.wp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Calificar extends AppCompatActivity {
    Producto producto;
    Usuario usuario;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final GlobalClass appstate = (GlobalClass) getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_calificar);

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

        Bundle b = getIntent().getExtras();
        int producto_id = -1;
        int usuario_id = -1;
        if(b != null)
        {
            producto_id = b.getInt("id_producto");
            usuario_id = b.getInt("id_usuario");

            this.producto = appstate.getProductobyID(producto_id);
            if(this.producto == null)
            {
                this.producto = new Producto(producto_id,getApplicationContext(),appstate);
            }

            this.usuario=appstate.getUsuariobyID(usuario_id);
            if(!this.usuario.isLoaded()) {
                this.usuario = new Usuario(usuario_id, getApplicationContext());
                this.usuario.fillValues();
            }
        }

        TextView experiencia = (TextView) findViewById(R.id.experiencia_usuario);
        TextView moneda = (TextView) findViewById(R.id.calificar_moneda);

        experiencia.setText("Como fue tu experiencia con "+usuario.getNombre());
        moneda.setText(""+producto.getMoneda().getSimbolo());

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
            RatingBar rating = (RatingBar) findViewById(R.id.calificar_rating);
            EditText cantidad = (EditText) findViewById(R.id.calificar_cantidad);
            EditText reseña = (EditText) findViewById(R.id.calificar_reseña);

            if(!(rating.getNumStars() > 0))
            {
                Toast.makeText(Calificar.this, "Favor de seleccionar el rating del usuario", Toast.LENGTH_SHORT).show();
            }

            String cantidadTxt = cantidad.getText().toString();
            if (cantidadTxt.matches("")) {
                cantidad.setError("Ingresa una cantidad.");
            }

            String reseñaTxt = reseña.getText().toString();
            if (reseñaTxt.matches("")) {
                reseña.setError("Ingresa una reseña por favor.");
            }

            Long unixTime = System.currentTimeMillis() / 1000L;

            ValoracionUsuario valoracion = new ValoracionUsuario(producto.getUsuario(),usuario,producto,
                    rating.getNumStars(),Integer.parseInt(cantidadTxt),reseñaTxt, unixTime);

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
