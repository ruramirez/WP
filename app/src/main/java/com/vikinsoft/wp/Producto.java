package com.vikinsoft.wp;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ricardo on 23/05/16.
 */
public class Producto extends AsyncTask<Integer, Void, Integer> {

    private int id = -1;
    private String nombre="";
    private String descripcion= "";
    private Usuario usuario;
    //private EstadoProducto estadoProducto;
    private int envio=0;
    private int precio_negociable = 0;
    private double precio = 0.0;
    //FALTA MONEDAS
    private int vistas =0;
    private Categoria categoria;
    private Context applicationContext;




    public int loadWeb() {
        try {
            URL url = new URL("http://vikinsoft.com/weplay/index.php?r=productos/load");

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
                    this.descripcion = jsonObject.getString("descripcion");
                    //this.estadoProducto = new EstadoProducto(Integer.parseInt(jsonObject.getString("id_estado")));
                    this.usuario = new Usuario(Integer.parseInt(jsonObject.getString("id_estado")),this.applicationContext);
                    this.categoria = new Categoria(Integer.parseInt(jsonObject.getString("id_estado")));

                    //this.emailValido = Integer.parseInt(jsonObject.getString("emailValido"));
                    //this.facebook = Integer.parseInt(jsonObject.getString("facebook"));
                    //this.google = Integer.parseInt(jsonObject.getString("google"));
                    //this.latitud = Double.parseDouble(jsonObject.getString("latitud"));
                    //this.longitud = Double.parseDouble(jsonObject.getString("longitud"));
                    //this.foto = Integer.parseInt(jsonObject.getString("foto"));
                    //this.isLoged = true;
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


    protected Integer doInBackground(Integer... integers) {
        if(integers[0] == 2)
        {
            return this.loadWeb();
        }

        return -1;
    }


}
