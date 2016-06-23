package com.vikinsoft.wp;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import org.apache.http.util.ByteArrayBuffer;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import bolts.Bolts;

/**
 * Created by ricardo on 10/05/16.
 */
public class Usuario extends AsyncTask<Integer, Void, Integer> implements LocationListener {

    private int id = -1;
    private String nombre = "";
    private String email = "";
    private String password = "";
    private int facebook = 0;
    private int emailValido = 0;
    private int google = 0;
    private double longitud = 0.0;
    private double latitud = 0.0;
    private int foto = 0;
    private boolean isLoged = false;
    private Context applicationContext;
    private Application appstate;
    private LocationManager locationManager;
    private String direccion = "";
    private Activity activity;
    private Bitmap imagen;
    private boolean locationUpdated = false;
    private boolean imageLoaded = false;
    private boolean loaded = false;
    public List<Producto> productosVendiendo = new ArrayList<>();
    public List<Producto> productosVendidos = new ArrayList<>();


    public void setAppstate(Application appstate) {
        this.appstate = appstate;
    }

    public Usuario(int id, Context applicationContex) {
        this.id=id;
        this.applicationContext = applicationContex;


    }


    public void fillValues() {


        final GlobalClass appstate = (GlobalClass) this.applicationContext;
        this.loadWeb();
        if (this.getFoto() == 1) {
            webUsuarios web = new webUsuarios(this);
            web.execute(5);
            this.loadImage();
            this.imageLoaded=true;
        }
        else
        {
            this.imageLoaded=true;
        }
        this.isLoged=false;
        appstate.usuarios.add(this);

    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setImageLoaded(boolean imageLoaded) {
        this.imageLoaded = imageLoaded;
    }

    private Usuario(String Email, String Password, Context applicationContex, Activity activity) {
        try {
            this.activity = activity;
            this.email = Email;
            this.password = Password;
            int result = this.execute(2).get();
            this.applicationContext = applicationContex;
            if (result != -1) {
                if (this.getFoto() == 1) {
                    webUsuarios web = new webUsuarios(this);
                    web.execute(5).get();
                    this.loadImage();
                    //Bitmap img = BitmapFactory.decodeStream(conn1.getInputStream());
                    //this.storeImage(this.getImagen());
                }
                DatabaseHandler db = new DatabaseHandler(applicationContex);
                db.saveUsuario(this);

            }
            //this.startLocationListener(activity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public Usuario(Context applicationContext, Activity activity) {
        this.applicationContext = applicationContext;
        this.activity = activity;
        //this.startLocationListener(activity);
    }

    public Usuario(int id, String nombre, String email, String password, int facebook, int emailValido, int google, double longitud, double latitud, int foto, Context applicationContext, Activity activity) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.facebook = facebook;
        this.emailValido = emailValido;
        this.google = google;
        this.longitud = longitud;
        this.latitud = latitud;
        this.foto = foto;
        this.applicationContext = applicationContext;
        this.isLoged = true;
        this.activity = activity;
        //this.startLocationListener(activity);
    }

    public boolean isLoaded() {
        if(this.imageLoaded && this.loaded) {
            return true;
        }else
        {
            return false;
        }
    }

    public boolean startLocationListener(Activity activity) {
        this.locationManager = (LocationManager) this.applicationContext.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 10000, this);
        return true;
    }

    public static Usuario logIn(String Email, String Password, Context applicationContex, Activity activity) {
        Usuario usuario = new Usuario(Email, Password, applicationContex, activity);
        return usuario;
    }

    public void logOut() {

        DatabaseHandler db = new DatabaseHandler(this.applicationContext);
        db.deleteUsuaro(this.id);
        if(facebook==1)
        {
            FacebookSdk.sdkInitialize(this.applicationContext);
            LoginManager.getInstance().logOut();
        }

        this.isLoged = false;
    }

    public Producto getProductoVendiendoByID(int id){
        for (Producto producto : this.productosVendiendo) {
            if(producto.getId()== id)
                return producto;
        }
        return  null;

    }


    public Producto getProductoVendidoByID(int id){
        for (Producto producto : this.productosVendidos) {
            if(producto.getId()== id)
                return producto;
        }
        return  null;

    }

    public static Usuario loadUsuario(Context applicationContext, Activity activity) {
        DatabaseHandler db = new DatabaseHandler(applicationContext);

        Usuario usuario = db.getUsuario(activity);
        if (usuario.getFoto() == 1) {
            usuario.loadImage();
        }

        return usuario;
    }

    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public boolean saveUsuario() throws InterruptedException, ExecutionException {

        webUsuarios web = new webUsuarios(this);
        int result = web.execute(1).get();
        if (result != -1) {

            this.id = result;
            if (this.foto == 1) {
                this.storeImage(this.imagen);
            }
            web = new webUsuarios(this);
            result = web.execute(4).get();
            DatabaseHandler db = new DatabaseHandler(this.applicationContext);

            return db.saveUsuario(this);
        }
        return false;
    }

    public boolean updateUsuario() {
        webUsuarios web = new webUsuarios(this);
        int result = -1;
        try {
            result = web.execute(3).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (result != -1) {
            DatabaseHandler db = new DatabaseHandler(this.applicationContext);
            db.updateusuario(this);
            return true;
        } else {
            return false;
        }

    }

    public void setLoged(boolean loged) {
        isLoged = loged;
    }

    public Bitmap getImagen() {


        return this.imagen;
    }

    public void addProductoVendiendo(Producto producto)
    {
        this.productosVendiendo.add(0,producto);
    }


    public void loadProductosVendidos(GlobalClass appstate){
        webUsuarios web = new webUsuarios(this);
        web.usuario.setAppstate(appstate);
        try {
            int result = web.execute(6).get();
            this.productosVendidos=web.usuario.getProductosVendidos();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }


    public void ChangeProductoVendiendoAvendido(Producto productovendido)
    {

        int contador=0;
        for (Producto producto : this.productosVendiendo) {
            if(producto.getId()== productovendido.getId()) {

                this.productosVendiendo.remove(contador);
                this.productosVendidos.add(0,producto);
                contador++;

                break;
            }
        }


    }



    public void loadProductosVendiendo(GlobalClass appstate){

        webUsuarios web = new webUsuarios(this);
        web.usuario.setAppstate(appstate);

        try {
            int result = web.execute(7).get();
            this.productosVendiendo=web.usuario.getProductosVendiendo();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }


    public void addProductoVendido(Producto producto)
    {
        this.productosVendidos.add(0,producto);
    }


    public int getCountPoductosVendidos()
    {
        return this.productosVendidos.size();
    }

    public int getCountAllProductos()
    {
        return this.getCountProductosVendiendo()+this.getCountPoductosVendidos();
    }

    public int getCountProductosVendiendo()
    {
        return this.productosVendiendo.size();
    }

    public List<Producto> getProductosVendiendo() {
        return productosVendiendo;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public boolean isLoged() {
        return this.isLoged;
    }

    public Activity getActivity() {
        return activity;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFacebook() {
        return this.facebook;
    }

    public void setFacebook(int facebook) {
        this.facebook = facebook;
    }

    public int getEmailValido() {
        return this.emailValido;
    }

    public void setEmailValido(int emailValido) {
        this.emailValido = emailValido;
    }

    public int getGoogle() {
        return this.google;
    }

    public void setGoogle(int google) {
        this.google = google;
    }

    public double getLongitud() {
        return this.longitud;
    }

    public double getLatitud() {
        return this.latitud;
    }

    public void setLocation(double latitud, double longitud) throws IOException {
        if (this.latitud != latitud || this.longitud != longitud) {

            this.longitud = longitud;
            this.latitud = latitud;
            int result = 0;
            Geocoder geocoder = new Geocoder(this.applicationContext, Locale.getDefault());
            List<Address> addresses = null;
            if (!this.locationUpdated) {
                webUsuarios web = new webUsuarios(this);
                try {
                    result = web.execute(3).get();
                    if (result != -1) {
                        this.locationUpdated = true;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            addresses = geocoder.getFromLocation(
                    this.latitud,
                    this.longitud,

                    // In this sample, get just a single address.
                    1);
            if (addresses == null || addresses.size() == 0) {

            } else {
                Address address = addresses.get(0);
                this.direccion = address.getAddressLine(3);
            }

        }
        this.updateUsuario();

    }

    public String getDireccion() {

        Geocoder geocoder = new Geocoder(this.applicationContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    this.longitud,
                    this.latitud,

                    // In this sample, get just a single address.
                    1);
        } catch (IOException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        if (addresses == null || addresses.size() == 0) {

        } else {
            Address address = addresses.get(0);
            this.direccion = address.getAddressLine(2);
        }
        return direccion;
    }

    public int getFoto() {
        return this.foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public int loadWeb() {
        try {
            URL url = new URL("http://vikinsoft.com/weplay/index.php?r=usuarios/load");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String urlParameters =
                    "id=" + this.id;
            connection.setRequestMethod("POST");
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            //connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
            connection.setDoOutput(true);
            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
            dStream.writeBytes(urlParameters);
            dStream.flush();
            dStream.close();
            int responseCode = connection.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseOutput.append(line);
            }
            br.close();
            JSONObject jsonObject = null;
            try {

                jsonObject = new JSONObject(responseOutput.toString());
                //this.id=Integer.parseInt(jsonObject.getString("id"));
                if (this.id != -1) {
                    this.nombre = jsonObject.getString("nombre");
                    this.password = jsonObject.getString("password");
                    this.email = jsonObject.getString("email");
                    this.emailValido = Integer.parseInt(jsonObject.getString("emailValido"));
                    this.facebook = Integer.parseInt(jsonObject.getString("facebook"));
                    this.google = Integer.parseInt(jsonObject.getString("google"));
                    this.latitud = Double.parseDouble(jsonObject.getString("latitud"));
                    this.longitud = Double.parseDouble(jsonObject.getString("longitud"));
                    this.foto = Integer.parseInt(jsonObject.getString("foto"));
                    this.isLoged = true;
                    this.loaded=true;
                    return this.id;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    public int loginWeb() {
        try {
            URL url = new URL("http://vikinsoft.com/weplay/index.php?r=usuarios/login");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String urlParameters =
                    "&email=" + this.email +
                            "&password=" + this.password;
            connection.setRequestMethod("POST");
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            //connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
            connection.setDoOutput(true);
            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
            dStream.writeBytes(urlParameters);
            dStream.flush();
            dStream.close();
            int responseCode = connection.getResponseCode();
            final StringBuilder output = new StringBuilder("Request URL " + url);
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseOutput.append(line);
            }
            br.close();
            JSONObject jsonObject = null;
            try {
                System.out.println(responseOutput.toString());
                jsonObject = new JSONObject(responseOutput.toString());
                //JSONObject newJSON = jsonObject.getJSONObject("id");
                this.id = Integer.parseInt(jsonObject.getString("id"));
                System.out.println("el id q me llego es " + jsonObject.getString("id"));
                this.loadWeb();
                return this.id;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }


    public void loadAllChats()
    {
        webUsuarios webUsuarios = new webUsuarios(this);
        webUsuarios.execute(8);

    }



    public void storeImage(Bitmap image) {
        File pictureFile = this.getOutputMediaFile();
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private void loadImage() {
        File image = this.getOutputMediaFile();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        this.setImagen(bitmap);
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + this.applicationContext.getPackageName()
                + "/Files");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String mImageName = this.id + ".jpg";
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    @Override
    public void onLocationChanged(Location loc) {
        if (loc.getAccuracy() < 100) {
            try {
                this.setLocation(loc.getLatitude(), loc.getLongitude());
                if (ActivityCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                this.locationManager.removeUpdates(this);
            this.locationManager = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    protected Integer doInBackground(Integer... integers) {
        if(integers[0] == 2)
        {
            return this.loginWeb();
        }
        else if(integers[0]== 6)
        {
            return this.loadWeb();
        }

        return -1;
    }

    public List<Producto> getProductosVendidos() {
        return productosVendidos;
    }

    private class webUsuarios  extends AsyncTask<Integer, Void, Integer> {
        private Usuario usuario;

        public webUsuarios(Usuario usuario)
        {
            this.usuario=usuario;
        }

        private int loadWEBimage(){
            try {
                URL url = new URL("http://vikinsoft.com/WP_avatares/"+this.usuario.id+".jpg");
                InputStream in = new BufferedInputStream(url.openStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n = 0;
                while (-1!=(n=in.read(buf)))
                {
                    out.write(buf, 0, n);
                }
                out.close();
                in.close();
                byte[] response = out.toByteArray();
                FileOutputStream fos = new FileOutputStream(this.usuario.getOutputMediaFile());
                fos.write(response);
                fos.close();

//catch some possible errors...
            }catch (IOException e) {
                Log.d("DownloadManager", "Error: " + e);
            }

            return 1;
        }

        private int loadAllChats(){
            try {
                URL url = new URL("http://vikinsoft.com/weplay/index.php?r=chats/loadAll");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters =
                        "id_usuario=" + this.usuario.id;
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                //connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();
                JSONArray arreglo = null;

                System.out.println("Respuesta de chat loaded " +responseOutput.toString());
                GlobalClass appstate = (GlobalClass) this.usuario.applicationContext;
                arreglo = new JSONArray(responseOutput.toString());

                //this.id=Integer.parseInt(jsonObject.getString("id"));
                //JSONArray msjs = jsonObject.getJSONArray("mensajes");
                for (int j = 0; j < arreglo.length(); j++) {

                    Usuario comprador;
                    comprador=appstate.getUsuariobyID(Integer.parseInt(arreglo.getJSONObject(j).getString("id_comprador")));
                    if(!comprador.isLoaded()) {
                        comprador = new Usuario(Integer.parseInt(arreglo.getJSONObject(j).getString("id_comprador")), this.usuario.applicationContext);
                        comprador.fillValues();
                    }

                    appstate.chats.add(new Chat(
                            Integer.parseInt(arreglo.getJSONObject(j).getString("id")),
                            appstate.getProductobyID(Integer.parseInt(
                                    arreglo.getJSONObject(j).getString("id_producto"))),comprador));


                }

                return 1;

            } catch (MalformedURLException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 1;
        }


        private int storeImageWEB() {
            try {

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                String mImageName=this.usuario.id +".jpg";
                File sourceFile = this.usuario.getOutputMediaFile();
                System.out.println(sourceFile.isFile());
                if (sourceFile.isFile()) {

                    try {
                        String upLoadServerUri = "http://vikinsoft.com/weplay/index.php?r=usuarios/uploadFoto";

                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(
                                sourceFile);
                        URL url = new URL(upLoadServerUri);

                        // Open a HTTP connection to the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE",
                                "multipart/form-data");
                        conn.setRequestProperty("Content-Type",
                                "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("bill", this.usuario.getOutputMediaFile().getAbsolutePath()+"/"+this.usuario.id+".jpg");

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                                +  this.usuario.getOutputMediaFile().getAbsolutePath()+"/"+this.usuario.id+".jpg" + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

                        // create a buffer of maximum size
                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math
                                    .min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0,
                                    bufferSize);

                        }

                        // send multipart form data necesssary after file
                        // data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens
                                + lineEnd);

                        // Responses from the server (code and message)
                        int serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn
                                .getResponseMessage();

                        System.out.println(serverResponseCode);

                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line = "";
                        StringBuilder responseOutput = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            responseOutput.append(line);
                        }
                        System.out.println(responseOutput.toString());

                        if (serverResponseCode == 200) {

                            // messageText.setText(msg);
                            //Toast.makeText(ctx, "File Upload Complete.",
                            //      Toast.LENGTH_SHORT).show();

                            // recursiveDelete(mDirectory1);

                        }

                        // close the streams //
                        fileInputStream.close();
                        dos.flush();
                        dos.close();

                    } catch (Exception e) {

                        // dialog.dismiss();
                        System.out.println(e.toString());

                    }
                    // dialog.dismiss();

                } // End else block


            } catch (Exception ex) {
                // dialog.dismiss();
                System.out.println(ex.toString());

                ex.printStackTrace();
            }
            return 1;


        }

        public int updateWeb() {
            try {
                URL url = new URL("http://vikinsoft.com/weplay/index.php?r=usuarios/update");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters =
                        "id=" + this.usuario.id +
                                "&nombre=" + this.usuario.nombre +
                                "&email=" + this.usuario.email +
                                "&password=" + this.usuario.password +
                                "&facebook=" + this.usuario.facebook +
                                "&emailValido=" + this.usuario.emailValido +
                                "&google=" + this.usuario.google +
                                "&latitud=" + this.usuario.latitud +
                                "&longitud=" + this.usuario.longitud +
                                "&foto=" + this.usuario.foto;
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                //connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseOutput.toString());
                    //this.id=Integer.parseInt(jsonObject.getString("id"));
                    return Integer.parseInt(jsonObject.getString("updated"));

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return -1;

        }

        public int saveOnWeb() {
            try {
                URL url = new URL("http://vikinsoft.com/weplay/index.php?r=usuarios/create");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters =
                        "nombre=" + this.usuario.nombre +
                                "&email=" + this.usuario.email +
                                "&password=" + this.usuario.password +
                                "&facebook=" + this.usuario.facebook +
                                "&emailValido=" + this.usuario.emailValido +
                                "&google=" + this.usuario.google +
                                "&latitud=" + this.usuario.latitud +
                                "&longitud=" + this.usuario.longitud +
                                "&foto=" + this.usuario.foto;
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("Accept-Charset", "UTF-8");

                //connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseOutput.toString());
                    //JSONObject newJSON = jsonObject.getJSONObject("id");
                    this.usuario.id=Integer.parseInt(jsonObject.getString("id"));

                    return this.usuario.id;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return -1;
        }



        public int loadProductosVendiendo() {
            try {

                URL url = new URL("http://vikinsoft.com/weplay/index.php?r=usuarios/getProductos");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters =
                        "usuario=" + this.usuario.getId();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                //connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();
                JSONArray jsonObject = null;

                try {
                    jsonObject = new JSONArray(responseOutput.toString());
                    this.usuario.productosVendiendo=new ArrayList<>();
                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject row = jsonObject.getJSONObject(i);
                        this.usuario.addProductoVendiendo(new Producto(Integer.valueOf(row.getString("id")),this.usuario.appstate,(GlobalClass)this.usuario.appstate));
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return -1;

        }



        public int loadProductosVendidos() {
            try {

                URL url = new URL("http://vikinsoft.com/weplay/index.php?r=usuarios/getProductosVendidos");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters =
                        "usuario=" + this.usuario.getId() ;
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                //connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();
                JSONArray jsonObject = null;

                try {
                    jsonObject = new JSONArray(responseOutput.toString());
                    this.usuario.productosVendidos=new ArrayList<>();
                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject row = jsonObject.getJSONObject(i);
                        this.usuario.addProductoVendido(new Producto(Integer.valueOf(row.getString("id")),this.usuario.applicationContext,(GlobalClass)this.usuario.appstate));
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return -1;

        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            if (integers[0] == 1) {
                return this.saveOnWeb();
            }
            else if(integers[0]== 3)
            {
                return this.updateWeb();
            }
            else if(integers[0]== 4)
            {
                return this.storeImageWEB();
            }
            else if(integers[0]== 5)
            {
                return this.loadWEBimage();
            }
            else if(integers[0]== 6)
            {
                return this.loadProductosVendidos();
            }
            else if(integers[0]== 7)
            {
                return this.loadProductosVendiendo();
            }else if(integers[0]== 8)
            {
                return this.loadAllChats();
            }
            return -1;
        }
    }
}
