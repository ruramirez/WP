package com.vikinsoft.wp;

import android.os.AsyncTask;

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
import java.util.concurrent.ExecutionException;

/**
 * Created by ruben on 22/06/16.
 */
public class ValoracionUsuario extends AsyncTask<Integer,Void,Integer>{

    private int id = -1;
    private Usuario valorador;
    private Usuario valorado;
    private Producto producto;
    private int calificacion = 0;
    private String mensaje = "";
    private Long timestamp;
    private int cantidad = 0;

    public ValoracionUsuario(Usuario valorador,Usuario valorado,
                             Producto producto,int calificacion,int cantidad,String mensaje,Long timestamp)
    {
        this.valorador = valorador;
        this.valorado = valorado;
        this.producto = producto;
        this.calificacion = calificacion;
        this.mensaje = mensaje;
        this.timestamp = timestamp;

        try {
            this.execute(1).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }



    private int save(){
        try {
            URL url = new URL("http://vikinsoft.com/weplay/index.php?r=valoraciones/create");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String urlParameters =
                            "id_valorador=" + this.valorador.getId() +
                            "&id_valorado=" + this.valorado.getId()+
                            "&id_producto=" + this.producto.getId()+
                            "&calificacion=" +this.calificacion +
                            "&timestamp=" +this.timestamp +
                            "&mensaje=" +this.mensaje;
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
            System.out.println("Respuesta de chat salvado " +responseOutput.toString());
            try {
                jsonObject = new JSONObject(responseOutput.toString());

                this.id=Integer.parseInt(jsonObject.getString("id"));
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

        return null;
    }
}
