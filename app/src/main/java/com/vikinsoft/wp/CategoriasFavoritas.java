package com.vikinsoft.wp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CategoriasFavoritas extends AppCompatActivity {

    private Toolbar categoriasToolbar;
    List<Categoria> categorias = new ArrayList<>();
    List<Categoria> categoriasDel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final GlobalClass appstate =(GlobalClass) getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias_favoritas);

        categoriasToolbar = (Toolbar) findViewById(R.id.toolbar_categorias_favoritas);

        setSupportActionBar(categoriasToolbar);
        getSupportActionBar().setTitle("Elige tus categorias");

        categoriasToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        categoriasToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vista = new Intent(CategoriasFavoritas.this,EditarPerfil.class);
                CategoriasFavoritas.this.startActivity(vista);
            }
        });


        LinearLayout linear = (LinearLayout)findViewById(R.id.categoriasfavoritas_layout);

        for (final Categoria categoria : appstate.categorias) {

            final RelativeLayout relativo = new RelativeLayout(this.getApplicationContext());
            relativo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            TextView texto = new TextView(this.getApplicationContext());
            texto.setText(categoria.getNombre());
            texto.setTextSize(18);
            texto.setTextColor(Color.parseColor("#000000"));
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params1.addRule(RelativeLayout.ALIGN_PARENT_START);
            texto.setLayoutParams(params1);
            final CheckBox box = new CheckBox(this.getApplicationContext());
            final String nombreCat = categoria.getNombre();
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            if(db.isFavorito(appstate.usuario,categoria))
            {
                box.setChecked(true);
            }
            box.setTag(categoria.getId());
            box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        categorias.add(appstate.getCategoriaID(Integer.parseInt(box.getTag().toString())));
                    }else{
                        categoriasDel.add(appstate.getCategoriaID(Integer.parseInt(box.getTag().toString())));
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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categorias_favoritas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_guardar_categorias) {

            GlobalClass appstate = (GlobalClass) getApplicationContext();
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            if(categorias.size() > 0)
            {
                db.truncateDb();
                for (Categoria categ : categorias)
                {
                    db.saveFavorito(appstate.usuario,categ);
                }
            }
            if(categoriasDel.size() > 0){
                for (Categoria categDel : categoriasDel)
                {
                    db.deleteFavorito(appstate.usuario,categDel);
                }
            }
            Toast.makeText(CategoriasFavoritas.this, "Categorias guardadas", Toast.LENGTH_SHORT).show();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



}
