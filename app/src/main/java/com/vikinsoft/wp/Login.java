package com.vikinsoft.wp;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.net.ssl.HttpsURLConnection;


public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    public static Bitmap getFacebookProfilePicture(String url) throws IOException {
        URL facebookProfileURL= new URL(url);
        Bitmap bitmap = BitmapFactory.decodeStream(facebookProfileURL.openConnection().getInputStream());
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestId()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });



        info = (TextView)findViewById(R.id.showOutput);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                try {

                                    System.out.println("ENTREAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                    StrictMode.setThreadPolicy(policy);
                                    String name = object.getString("name");
                                    String mail = object.getString("email");
                                    String id = object.getString("id");
                                    GlobalClass appstate=(GlobalClass) getApplicationContext();
                                    appstate.usuario= new Usuario(getApplicationContext(),appstate.usuario.getActivity());
                                    appstate.usuario.setNombre(name);
                                    appstate.usuario.setEmail(mail);
                                    appstate.usuario.setPassword(id);
                                    appstate.usuario.setFacebook(1);

                                    JSONObject pic = object.getJSONObject("picture");
                                    JSONObject data = pic.getJSONObject("data");
                                    String url = data.getString("url");
                                    //outputView.setText(Uri.parse(url));

                                    ImageView perfil = (ImageView) findViewById(R.id.logo);
                                    URL fb_url = new URL(url);
                                    HttpsURLConnection conn1 = (HttpsURLConnection) fb_url.openConnection();
                                    HttpsURLConnection.setFollowRedirects(true);
                                    conn1.setInstanceFollowRedirects(true);
                                    Bitmap fb_img = BitmapFactory.decodeStream(conn1.getInputStream());
                                    if(fb_img != null) {
                                        appstate.usuario.setImagen(fb_img);
                                        appstate.usuario.setFoto(1);
                                        //appstate.usuario.storeImage(fb_img);
                                    }


                                    try {
                                        boolean result = appstate.usuario.saveUsuario();
                                        if(result)
                                        {
                                            finish();
                                        }

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture.width(150).height(150)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });



        TextView tview =(TextView)findViewById(R.id.link_signup);
        tview.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(Login.this, Registro.class);
                Login.this.startActivity(myIntent);
            }
        });

        final Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                EditText frm_correo  = (EditText)findViewById(R.id.input_email);
                EditText frm_pass = (EditText)findViewById(R.id.input_password);

                String correo_str = frm_correo.getText().toString();
                String pass_str = frm_pass.getText().toString();
                GlobalClass appstate= (GlobalClass) getApplicationContext();
                appstate.usuario = Usuario.logIn(correo_str,pass_str,getApplicationContext(),appstate.usuario.getActivity());

                //aqui va el login normal
                if(appstate.usuario.isLoged())
                {
                    finish();
                }else{
                    info.setText("Credenciales incorrectas , intenta de nuevo.");
                }

            }
        }
        );
    }

      private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);

            GlobalClass appstate= (GlobalClass) getApplicationContext();
            appstate.usuario = new Usuario(getApplicationContext(),appstate.usuario.getActivity());
            appstate.usuario.setNombre(acct.getDisplayName());
            appstate.usuario.setEmail(acct.getEmail());
            appstate.usuario.setPassword(acct.getId());
            appstate.usuario.setGoogle(1);
            try {
                boolean resultG = appstate.usuario.saveUsuario();
                if(resultG)
                {
                    finish();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
