package com.vikinsoft.wp;

import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class VerificacionEditarPerfil extends AppCompatActivity {

    RelativeLayout facebook,google,email;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion_editar_perfil);

        GlobalClass appstate = (GlobalClass) this.getApplicationContext();
        this.usuario = appstate.usuario;

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_verificar_editar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomain = new Intent(VerificacionEditarPerfil.this, EditarPerfil.class);
                VerificacionEditarPerfil.this.startActivity(gotomain);
            }
        });

        facebook = (RelativeLayout) findViewById(R.id.verificar_editar_facebook);
        google = (RelativeLayout) findViewById(R.id.verificar_editar_google);
        email = (RelativeLayout) findViewById(R.id.verificar_editar_email);

        ImageView fb = (ImageView) findViewById(R.id.face_ver);
        ImageView gg = (ImageView) findViewById(R.id.gg_ver);
        ImageView em = (ImageView) findViewById(R.id.email_ver);


        if(this.usuario.getFacebook() == 1)
        {
            fb.setImageResource(R.drawable.fb_color);
        }else{
            fb.setImageResource(R.drawable.fb_gris);
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VerificacionEditarPerfil.this,VerificarFacebook.class);
                    VerificacionEditarPerfil.this.startActivity(intent);
                }
            });
        }

        if(this.usuario.getGoogle() == 1)
        {
            gg.setImageResource(R.drawable.g_color);
        }else{
            gg.setImageResource(R.drawable.g_gris);
            gg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VerificacionEditarPerfil.this,VerificarGoogle.class);
                    VerificacionEditarPerfil.this.startActivity(intent);
                }
            });
        }

        if(this.usuario.getEmailValido() == 1)
        {
            em.setImageResource(R.drawable.correo_color);
        }else{
            em.setImageResource(R.drawable.correo_gris);
            em.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VerificacionEditarPerfil.this,VerificarMail.class);
                    VerificacionEditarPerfil.this.startActivity(intent);
                }
            });
        }


        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fb = new Intent(VerificacionEditarPerfil.this,VerificarFacebook.class);
                VerificacionEditarPerfil.this.startActivity(fb);
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gg = new Intent(VerificacionEditarPerfil.this,VerificarGoogle.class);
                VerificacionEditarPerfil.this.startActivity(gg);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(VerificacionEditarPerfil.this,VerificarMail.class);
                VerificacionEditarPerfil.this.startActivity(email);
            }
        });


    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
