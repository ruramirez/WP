package com.vikinsoft.wp;

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
import java.util.concurrent.ExecutionException;

/**
 * Created by ricardo on 20/06/16.
 */
public class Mensaje extends AsyncTask<Integer, Void, Integer> {
    private int id= -1;
    private Producto producto;
    private Usuario vendedor;
    private Usuario comprador;
    private String mensaje;
    private int estado;
    private Long timestamp;


    public Mensaje (int id, Usuario vendedor,Usuario Comrador,Producto producto,String mensaje,int estado, Long timestamp)
    {
        this.id=id;
        this.producto=producto;
        this.vendedor=vendedor;
        this.comprador=comprador;
        this.mensaje=mensaje;
        this.estado= estado;
        this.timestamp=timestamp;
    }

    public Mensaje (Usuario vendedor,Usuario Comrador,Producto producto,String mensaje,int estado, Long timestamp)
    {
        //this.id=id;
        this.producto=producto;
        this.vendedor=vendedor;
        this.comprador=comprador;
        this.mensaje=mensaje;
        this.estado= estado;
        this.timestamp=timestamp;

        this.execute(1);

    }

    private int save(){
        try {
            URL url = new URL("http://vikinsoft.com/weplay/index.php?r=mensajes/save");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String urlParameters =
                    "id_producto=" + this.producto.getId() +
                            "&id_comprador=" + this.comprador.getId()+
                            "&id_vendedor=" + this.vendedor.getId()+
                            "&timestamp=" + this.timestamp+
                            "&estado=" + this.estado+
                            "&mensaje=" + this.mensaje;
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
                return Integer.parseInt(jsonObject.getString("id"));

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
        return this.id;
    }


    @Override
    protected Integer doInBackground(Integer... integers) {
        if(integers[0] == 1)
        {
            return this.save();
        }


        return -1;
    }
}
