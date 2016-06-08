package com.vikinsoft.wp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by ricardo on 23/05/16.
 */
public class Producto extends AsyncTask<Integer, Void, Integer> {

    private int id = -1;
    private String nombre="";
    private String descripcion= "";
    private Usuario usuario;
    private int envio=0;
    private int precio_negociable = 0;
    private double precio = 0.0;
    private int vistas =0;
    private int favoritos =0;
    private String simbolo;
    private Moneda moneda;
    //private EstadoProducto estadoProducto;
    private int estadoProducto = 0;
    private Categoria categoria;
    private Context applicationContext;
    private Dialog dialog;
    private List<FotosProductos> fotos = new ArrayList<>();
    private List<Integer> idsProductos = new ArrayList<>();
    private String imagenURL;
    private ImageView imagen;
    private boolean loaded=false;



    public Producto(List<FotosProductos> fotosProd, Usuario usuario,Categoria categoria,Moneda moneda, String nombre, String descripcion, int envio , int precio_negociable , float precio, Context applicationContext, Dialog dialog)
    {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.envio = envio;
        this.precio = precio;
        this.precio_negociable = precio_negociable;
        this.vistas = 0;
        this.favoritos = 0;
        this.applicationContext = applicationContext;
        this.dialog = dialog;
        this.categoria = categoria;
        this.usuario = usuario;
        this.moneda = moneda;
        this.fotos = fotosProd;

    }

    public Producto(Usuario usuario) {
        this.usuario = usuario;
    }


    public Producto(int id)
    {
        this.id = id;

        this.execute(2);

    }

    public boolean isLoaded() {
        if(this.moneda.isLoaded()&& this.usuario.isLoaded()) {
            return true;
        }else
        {
            return false;
        }
    }

    public int saveOnWeb() {

        try {
            URL url = new URL("http://vikinsoft.com/weplay/index.php?r=productos/create");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String urlParameters =
                            "nombre=" + this.nombre +
                            "&descripcion=" + this.descripcion +
                            "&id_usuario=" + this.usuario.getId() +
                            "&id_estado=" +this.estadoProducto +
                            "&envio=" + this.envio +
                            "&precio_negociable=" + this.precio_negociable +
                            "&precio=" + this.precio +
                            "&id_moneda=" + this.moneda.getId() +
                            "&vistas=" + this.vistas +
                            "&favoritos=" + this.favoritos +
                            "&id_categoria=" + this.categoria.getId()
                            ;
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
                this.id=Integer.parseInt(jsonObject.getString("id"));
                for (FotosProductos fotosProductos : this.fotos)
                {
                    fotosProductos.setId_productp(this.id);
                    fotosProductos.saveOnWeb();
                }
                return this.id;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(responseOutput.toString());

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<FotosProductos> getFotos() {
        return fotos;
    }

    public int loadWeb() {
        try {

            URL url = new URL("http://vikinsoft.com/weplay/index.php?r=productos/load");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String urlParameters =
                    "id=" + this.id;
            connection.setRequestMethod("POST");
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
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
                System.out.println("LA respuestaaaaaaaaaaaaaaaa"+responseOutput.toString());
                jsonObject = new JSONObject(responseOutput.toString());
                //this.id=Integer.parseInt(jsonObject.getString("id"));
                if (this.id != -1) {
                    this.nombre = jsonObject.getString("nombre");
                    this.descripcion = jsonObject.getString("descripcion");
                    this.moneda= new Moneda(Integer.parseInt(jsonObject.getString("id_moneda")));
                    this.usuario= new Usuario(Integer.parseInt(jsonObject.getString("id_usuario")));



                    JSONArray jsonArray = new JSONArray(jsonObject.getString("fotos"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject row = jsonArray.getJSONObject(i);
                        this.fotos.add(new FotosProductos(this.id, Integer.valueOf(row.getString("id"))));
                    }

                    this.precio = Double.parseDouble(jsonObject.getString("precio"));
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

    public Moneda getMoneda() {
        return moneda;
    }

    protected Integer doInBackground(Integer... integers) {
        if(integers[0] == 2)
        {
            return this.loadWeb();
        }
        if(integers[0] == 3)
        {
            return this.saveOnWeb();
        }

        return -1;
    }


    public int getId()
    {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEnvio() {
        return envio;
    }

    public void setEnvio(int envio) {
        this.envio = envio;
    }

    public int getPrecio_negociable() {
        return precio_negociable;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getVistas() {
        return vistas;
    }

    public void setVistas(int vistas) {
        this.vistas = vistas;
    }

    public int getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(int favoritos) {
        this.favoritos = favoritos;
    }

    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public int getMonedaId(Moneda moneda)
    {
        return moneda.getId();
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setIdsProductos(List<Integer> idsProductos) {
        this.idsProductos = idsProductos;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public ImageView getImagen() {
        return imagen;
    }

    public void setImagen(ImageView imagen) {
        this.imagen = imagen;
    }


}
