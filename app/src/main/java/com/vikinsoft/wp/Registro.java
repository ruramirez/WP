package com.vikinsoft.wp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        TextView rview =(TextView)findViewById(R.id.link_login);
        rview.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent changeToLogin = new Intent(Registro.this, Login.class);
                Registro.this.startActivity(changeToLogin);
            }
        });

        final Button btnLogin = (Button) findViewById(R.id.btn_signup);
        btnLogin.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    EditText frm_nombre  = (EditText)findViewById(R.id.input_name);
                    EditText frm_apellido = (EditText)findViewById(R.id.input_apellido);
                    EditText frm_correo = (EditText)findViewById(R.id.input_email_registro);
                    EditText frm_contraseña = (EditText)findViewById(R.id.input_password_registro);

                    String nombre_str = frm_nombre.getText().toString();
                    String apellido_str = frm_apellido.getText().toString();
                    String correo_str = frm_correo.getText().toString();
                    String contrasena_str = frm_contraseña.getText().toString();

                    GlobalClass appstate = (GlobalClass)getApplicationContext();

                    appstate.usuario = new Usuario(getApplicationContext(),appstate.usuario.getActivity());
                    appstate.usuario.setEmailValido(1);
                    appstate.usuario.setEmail(correo_str);
                    appstate.usuario.setNombre(nombre_str+" "+apellido_str);
                    appstate.usuario.setPassword(contrasena_str);
                    try {
                        boolean resultG = appstate.usuario.saveUsuario();
                        appstate.usuario.startLocationListener(appstate.usuario.getActivity());

                        if(resultG)
                        {
                            Intent main = new Intent(Registro.this,MainActivity.class);
                            Registro.this.startActivity(main);
                            finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        );
    }
}
