package com.vikinsoft.wp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.List;


public class VerificarFacebook extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_verificar_facebook);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_verificar_facebook);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomain = new Intent(VerificarFacebook.this, PerfilUsuario.class);
                VerificarFacebook.this.startActivity(gotomain);
            }
        });


        loginButton = (LoginButton) findViewById(R.id.login_button_validacion);
        List < String > permissionNeeds = Arrays.asList("email",
                "public_profile", "AccessToken");
        loginButton.registerCallback(callbackManager,
                new FacebookCallback < LoginResult > () {

                @Override
                public void onSuccess(LoginResult loginResult) {
                        GlobalClass appstate = (GlobalClass) getApplicationContext();
                        appstate.usuario.setFacebook(1);
                        appstate.usuario.updateUsuario();
                        Toast.makeText(VerificarFacebook.this, "Verificado con exito!!", Toast.LENGTH_SHORT).show();
                        finish();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(VerificarFacebook.this, "Inicio de sesion , cancelado", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(VerificarFacebook.this, ""+exception.getCause().toString(), Toast.LENGTH_SHORT).show();
                }
    });

}
    @Override
    protected void onActivityResult ( int requestCode, int responseCode,
                                      Intent data){
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    };
}