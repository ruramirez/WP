package com.vikinsoft.wp;

import android.content.Context;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by ruben on 20/06/16.
 */
public class ListaMensajes extends AsyncTask<Integer, Void, Integer> {

    List<Mensaje> mensajes = new ArrayList<>();
    private Context contexto;
    Producto producto;
    Usuario usuario;


    public ListaMensajes(Producto producto,Usuario comprador,Context contexto)
    {
        this.producto = producto;
        this.usuario = comprador;
        this.contexto = contexto;

            System.out.println("Voy a hacer el service");
        //try {
            this.execute(1);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //} catch (ExecutionException e) {
        //    e.printStackTrace();
       // }


    }

    public int loadAllWeb() {
        try {
            URL url = new URL("http://vikinsoft.com/weplay/index.php?r=mensajes/getAll");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String urlParameters =
                    "id_producto="+producto.getId() +
                    "&id_comprador="+usuario.getId()+
                    "&id_vendedor="+producto.getUsuario().getId();
            ;
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
            JSONArray jsonObject = null;
            System.out.println("Codigo "+responseCode);
            System.out.println("Helooooooooooooooooo "+responseOutput.toString());
            try {
                System.out.println(responseOutput.toString());
                jsonObject = new JSONArray(responseOutput.toString());
                if (responseOutput.toString().isEmpty() == false) {
                    //
                    for(int i=0;i<jsonObject.length();i++){
                        JSONObject obj3=jsonObject.getJSONObject(i);
                        this.mensajes.add(new Mensaje(Integer.parseInt(obj3.getString("id"))));
                    }
                    return 1;
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
        if(integers[0] == 1)
        {
            return this.loadAllWeb();
        }
        return null;
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public Context getContexto() {
        return contexto;
    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }
}
